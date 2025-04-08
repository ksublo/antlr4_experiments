import java.util.*;

public class TypeChecker extends KSU_langBaseVisitor<String> {
    private final Map<String, String> symbolTable = new HashMap<>();
    private final List<String> errors = new ArrayList<>();

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void printErrors() {
        errors.forEach(System.err::println);
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

        if (!symbolTable.containsKey(varName)) {
            errors.add("Line " + lineNumber + ": Variable '" + varName + "' used before declaration.");
            return null;
        }

        String declaredType = symbolTable.get(varName);

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
        if (!symbolTable.containsKey(name)) {
            errors.add("Variable '" + name + "' used before declaration.");
            return null;
        }
        return symbolTable.get(name);
    }

    @Override
    public String visitType(KSU_langParser.TypeContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitStat(KSU_langParser.StatContext ctx) {
        if (ctx.type() != null && ctx.idList() != null) {
            String declaredType = ctx.type().getText();
            for (var id : ctx.idList().ID()) {
                String varName = id.getText();
                if (symbolTable.containsKey(varName)) {
                    errors.add("Variable '" + varName + "' already declared.");
                } else {
                    symbolTable.put(varName, declaredType);
                }
            }
        }
        return super.visitStat(ctx);
    }

    private boolean isAssignable(String targetType, String sourceType) {
        if (targetType == null || sourceType == null) return false;
        if (targetType.equals(sourceType)) return true;
        return targetType.equals("float") && sourceType.equals("int");
    }

    @Override
    public String visitMod(KSU_langParser.ModContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));

        if ("int".equals(leftType) && "int".equals(rightType)) {
            return "int";
        }

        errors.add("Line " + ctx.getStart().getLine() + ": Modulo operation can only be used with integers.");
        return null;
    }



}
