import java.util.*;

public class TypeChecker extends KSU_langBaseVisitor<String> {
    private final Stack<Map<String, String>> scopes = new Stack<>();
    private final List<String> errors = new ArrayList<>();

    public TypeChecker() {
        scopes.push(new HashMap<>());
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void printErrors() {
        errors.forEach(System.err::println);
    }

    private void enterScope() {
        scopes.push(new HashMap<>());
    }

    private void exitScope() {
        scopes.pop();
    }

    private boolean isDeclared(String varName) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(varName)) {
                return true;
            }
        }
        return false;
    }

    private String getType(String varName) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(varName)) {
                return scopes.get(i).get(varName);
            }
        }
        return null;
    }

    private void declare(String varName, String type) {
        scopes.peek().put(varName, type);
    }


    @Override
    public String visitProg(KSU_langParser.ProgContext ctx) {
        for (var stat : ctx.stat()) {
            visit(stat);
        }
        return null;
    }

    @Override
    public String visitAssign(KSU_langParser.AssignContext ctx) {
        String varName = ctx.ID().getText();
        String valueType = visit(ctx.expr());

        int lineNumber = ctx.getStart().getLine();

        if (!isDeclared(varName)) {
            errors.add("Line " + lineNumber + ": Variable '" + varName + "' used before declaration.");
            return null;
        }

        String declaredType = getType(varName);

        if (!isAssignable(declaredType, valueType)) {
            errors.add("Line " + lineNumber + ": Type mismatch: cannot assign " + valueType + " to " + declaredType + " variable '" + varName + "'");
        }

        return declaredType;
    }

    @Override
    public String visitLiteralExpr(KSU_langParser.LiteralExprContext ctx) {
        return visit(ctx.literal());
    }

    @Override
    public String visitLiteral(KSU_langParser.LiteralContext ctx) {
        if (ctx.INT() != null) return "int";
        if (ctx.FLOAT() != null) return "float";
        if (ctx.BOOL() != null) return "bool";
        if (ctx.STRING() != null) return "string";
        return null;
    }

    @Override
    public String visitVar(KSU_langParser.VarContext ctx) {
        String name = ctx.ID().getText();
        if (!isDeclared(name)) {
            errors.add("Variable '" + name + "' used before declaration.");
            return null;
        }
        return getType(name);
    }

    @Override
    public String visitType(KSU_langParser.TypeContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitStat(KSU_langParser.StatContext ctx) {
        if (ctx.getChild(0).getText().equals("{")) {
            enterScope();
            for (var innerStat : ctx.stat()) {
                visit(innerStat);
            }
            exitScope();
            return null;
        }

        if (ctx.type() != null && ctx.idList() != null) {
            String declaredType = ctx.type().getText();
            for (var id : ctx.idList().ID()) {
                String varName = id.getText();
                if (isDeclared(varName)) {
                    errors.add("Variable '" + varName + "' already declared.");
                } else {
                    declare(varName, declaredType);
                }
            }
        }

        if (ctx.getChild(0).getText().equals("read") && ctx.idList() != null) {
            for (var id : ctx.idList().ID()) {
                String varName = id.getText();
                if (!isDeclared(varName)) {
                    errors.add("Variable '" + varName + "' used in 'read' before declaration.");
                }
            }
        }

        if (ctx.getChild(0).getText().equals("if")) {
            String condType = visit(ctx.expr(0));

            if (!"bool".equals(condType)) {
                errors.add("Condition in 'if' must be of type bool.");
            }

            visit(ctx.stat(0));
            if (ctx.stat().size() > 1) {
                visit(ctx.stat(1));
            }
        }

        if (ctx.getChild(0).getText().equals("for")) {

            if (ctx.expr(0) != null) {
                visit(ctx.expr(0));
            }

            if (ctx.expr(1) != null) {
                String condType = visit(ctx.expr(1));
                if (!"bool".equals(condType)) {
                    errors.add("2nd condition in 'for' must be of type bool.");
                }
            }

            if (ctx.expr(2) != null) {
                visit(ctx.expr(2));
            }

            visit(ctx.stat(0));
            return null;
        }

        return super.visitStat(ctx);
    }



    @Override
    public String visitAddSub(KSU_langParser.AddSubContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));

        if (!isCompatible(leftType, rightType)) {
            errors.add("Line " + ctx.getStart().getLine() + ": Type mismatch in addition/subtraction operation.");
            return null;
        }

        return leftType.equals("int") ? "int" : "float";
    }

    @Override
    public String visitMulDiv(KSU_langParser.MulDivContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));

        if (!isCompatible(leftType, rightType)) {
            errors.add("Line " + ctx.getStart().getLine() + ": Type mismatch in multiplication/division operation.");
            return null;
        }

        return leftType.equals("int") ? "int" : "float";
    }

    @Override
    public String visitMod(KSU_langParser.ModContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));

        if (!"int".equals(leftType) || !"int".equals(rightType)) {
            errors.add("Line " + ctx.getStart().getLine() + ": Modulo operation can only be used with integers.");
            return null;
        }

        return "int";
    }

    private boolean isAssignable(String targetType, String sourceType) {
        if (targetType == null || sourceType == null) return false;
        if (targetType.equals(sourceType)) return true;
        return targetType.equals("float") && sourceType.equals("int");
    }

    private boolean isCompatible(String leftType, String rightType) {
        return leftType.equals(rightType) || (leftType.equals("int") && rightType.equals("float")) || (leftType.equals("float") && rightType.equals("int"));
    }

    public Map<String, String> getSymbolTable() {
        Map<String, String> flat = new HashMap<>();
        for (Map<String, String> scope : scopes) {
            flat.putAll(scope);
        }
        return flat;
    }

    @Override
    public String visitRelational(KSU_langParser.RelationalContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));
        int line = ctx.getStart().getLine();

        if (leftType == null || rightType == null) {
            errors.add("Line " + line + ": Cannot determine types in relational expression.");
            return null;
        }

        if (!leftType.equals(rightType)) {
            errors.add("Line " + line + ": Relational operands must be of the same type.");
        }

        return "bool";
    }


}
