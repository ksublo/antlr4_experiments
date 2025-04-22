import java.util.*;

public class EvalVisitor extends KSU_langBaseVisitor<Object> {

    private final Map<String, Object> memory = new HashMap<>();
    private final Map<String, String> symbolTable = new HashMap<>();
    private final List<String> instructions = new ArrayList<>();

    public EvalVisitor(Map<String, String> symbolTable) {
        this.symbolTable.putAll(symbolTable);
    }

    public EvalVisitor() {}

    private int labelCounter = 0;

    private String newLabel(String prefix) {
        return prefix + "_" + (labelCounter++);
    }

    private void generatePushInstruction(String type, String value) {
        instructions.add("PUSH " + type + " " + value);
    }

    private void generateSaveInstruction(String varName) {
        instructions.add("SAVE " + varName);
    }

    private void generateLoadInstruction(String varName) {
        instructions.add("LOAD " + varName);
    }

    private void generatePrintInstruction(String type) {
        instructions.add("PRINT " + type);
    }

    @Override
    public Object visitProg(KSU_langParser.ProgContext ctx) {
        for (KSU_langParser.StatContext stat : ctx.stat()) {
            visit(stat);
        }
        return null;
    }

    private String getInstructionType(Object value) {
        if (value instanceof Integer) return "I";
        if (value instanceof Float || value instanceof Double) return "F";
        if (value instanceof String) return "S";
        if (value instanceof Boolean) return "B";
        return "U";
    }

    @Override
    public Object visitAssign(KSU_langParser.AssignContext ctx) {
        String varName = ctx.ID().getText();

        if (ctx.expr() instanceof KSU_langParser.AssignContext) {
            Object rightValue = visit(ctx.expr());
            generateLoadInstruction(((KSU_langParser.AssignContext) ctx.expr()).ID().getText());
            generateSaveInstruction(varName);
            memory.put(varName, rightValue);
            return rightValue;
        }

        Object value = visit(ctx.expr());
        String declaredType = symbolTable.get(varName);
        if ("float".equals(declaredType) && value instanceof Integer) {
            value = ((Integer) value).floatValue();
        }
        memory.put(varName, value);
        generateSaveInstruction(varName);
        return value;
    }

    @Override
    public Object visitAddSub(KSU_langParser.AddSubContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        String op = ctx.op.getText();
        instructions.add(op.equals("+") ? "ADD" : "SUB");

        if (left instanceof Integer && right instanceof Integer) {
            return op.equals("+") ? (Integer) left + (Integer) right : (Integer) left - (Integer) right;
        } else {
            float l = ((Number) left).floatValue();
            float r = ((Number) right).floatValue();
            return op.equals("+") ? l + r : l - r;
        }
    }

    @Override
    public Object visitMulDiv(KSU_langParser.MulDivContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        String op = ctx.op.getText();
        instructions.add(op.equals("*") ? "MUL" : "DIV");

        if (left instanceof Integer && right instanceof Integer) {
            return op.equals("*") ? (Integer) left * (Integer) right : (Integer) left / (Integer) right;
        } else {
            float l = ((Number) left).floatValue();
            float r = ((Number) right).floatValue();
            return op.equals("*") ? l * r : l / r;
        }
    }

    @Override
    public Object visitMod(KSU_langParser.ModContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        instructions.add("MOD");

        if (left instanceof Integer && right instanceof Integer) {
            return (Integer) left % (Integer) right;
        } else {
            throw new RuntimeException("Modulo operation can only be used with integers.");
        }
    }

    @Override
    public Object visitStrConcat(KSU_langParser.StrConcatContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));

        String s1 = String.valueOf(left);
        String s2 = String.valueOf(right);
        String result = s1 + s2;

        generatePushInstruction("S", result);
        return result;
    }

    @Override
    public Object visitLiteral(KSU_langParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            int value = Integer.parseInt(ctx.INT().getText());
            generatePushInstruction("I", String.valueOf(value));
            return value;
        } else if (ctx.FLOAT() != null) {
            float value = Float.parseFloat(ctx.FLOAT().getText());
            generatePushInstruction("F", String.valueOf(value));
            return value;
        } else if (ctx.STRING() != null) {
            String value = ctx.STRING().getText().replace("\"", "");
            generatePushInstruction("S", value);
            return value;
        } else if (ctx.BOOL() != null) {
            boolean value = Boolean.parseBoolean(ctx.BOOL().getText());
            generatePushInstruction("B", String.valueOf(value));
            return value;
        }
        return null;
    }


    @Override
    public Object visitVar(KSU_langParser.VarContext ctx) {
        String varName = ctx.ID().getText();
        if (!memory.containsKey(varName)) {
            throw new RuntimeException("Variable '" + varName + "' used before it was assigned a value.");
        }
        generateLoadInstruction(varName);
        return memory.get(varName);
    }

    public List<String> getInstructions() {
        return instructions;
    }

    @Override
    public Object visitStat(KSU_langParser.StatContext ctx) {

        if (ctx.getChild(0).getText().equals("read") && ctx.idList() != null) {
            for (var id : ctx.idList().ID()) {
                String varName = id.getText();
                String type = symbolTable.get(varName);
                instructions.add("READ " + type + " " + varName);
            }
            return null;
        }

        if (ctx.getChild(0).getText().equals("write") && ctx.exprList() != null) {
            for (var expr : ctx.exprList().expr()) {
                Object value = visit(expr);
                String typeCode = getInstructionType(value);
                generatePrintInstruction(typeCode);
            }
            return null;
        }

        if (ctx.getChild(0).getText().equals("if")) {
            String labelElse = newLabel("else");
            String labelEnd = newLabel("endif");

            Object condition = visit(ctx.expr(0));
            instructions.add("JZ " + labelElse);
            visit(ctx.stat(0));
            instructions.add("JMP " + labelEnd);

            instructions.add("LABEL " + labelElse);
            if (ctx.stat().size() > 1) {
                visit(ctx.stat(1));
            }

            instructions.add("LABEL " + labelEnd);
            return null;
        }

        if (ctx.getChild(0).getText().equals("while")) {
            String labelStart = newLabel("while_start");
            String labelEnd = newLabel("while_end");

            instructions.add("LABEL " + labelStart);

            Object condition = visit(ctx.expr(0));
            instructions.add("JZ " + labelEnd);

            if (ctx.stat(0).getChild(0).getText().equals("{")) {
                for (var inner : ctx.stat(0).stat()) {
                    visit(inner);
                }
            } else {
                visit(ctx.stat(0));
            }

            instructions.add("JMP " + labelStart);
            instructions.add("LABEL " + labelEnd);
            return null;
        }


        if (ctx.getChild(0).getText().equals("for")) {
            String labelStart = newLabel("for_start");
            String labelEnd = newLabel("for_end");

            if (ctx.expr(0) != null) visit(ctx.expr(0));

            instructions.add("LABEL " + labelStart);

            if (ctx.expr(1) != null) {
                Object cond = visit(ctx.expr(1));
                instructions.add("JZ " + labelEnd);
            }

            visit(ctx.stat(0));

            if (ctx.expr(2) != null) visit(ctx.expr(2));

            instructions.add("JMP " + labelStart);
            instructions.add("LABEL " + labelEnd);
            return null;
        }

//        if (!ctx.expr().isEmpty() && ctx.expr().size() == 1) {
//            var expr = ctx.expr(0);
//            Object result = visit(expr);
//
//            if (expr instanceof KSU_langParser.AssignContext assignCtx) {
//                String varName = assignCtx.ID().getText();
//                generateLoadInstruction(varName);
//            }
//
//            String typeCode = getInstructionType(result);
//            generatePrintInstruction(typeCode);
//            return null;
//        }

        if (ctx.expr().size() == 1) {
            Object result = visit(ctx.expr(0));

            if (!(ctx.expr(0) instanceof KSU_langParser.AssignContext) &&
                    !(ctx.expr(0) instanceof KSU_langParser.RelationalContext)) {

                String typeCode = getInstructionType(result);
                generatePrintInstruction(typeCode);
            }

            return null;
        }

        return super.visitStat(ctx);
    }

    @Override
    public Object visitRelational(KSU_langParser.RelationalContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        String op = ctx.op.getText();

        switch (op) {
            case "==":
                instructions.add("CMPEQ");
                break;
            case "!=":
                instructions.add("CMPNE");
                break;
            case "<":
                instructions.add("CMPLT");
                break;
            case ">":
                instructions.add("CMPGT");
                break;
            default:
                throw new RuntimeException("Unsupported relational operator: " + op);
        }

        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;
            return switch (op) {
                case "<" -> l < r;
                case ">" -> l > r;
                case "==" -> l == r;
                case "!=" -> l != r;
                default -> throw new RuntimeException("Invalid relational operator: " + op);
            };
        }

        if (left instanceof Float || right instanceof Float) {
            float l = ((Number) left).floatValue();
            float r = ((Number) right).floatValue();
            return switch (op) {
                case "<" -> l < r;
                case ">" -> l > r;
                case "==" -> l == r;
                case "!=" -> l != r;
                default -> throw new RuntimeException("Invalid relational operator: " + op);
            };
        }

        throw new RuntimeException("Invalid operands for relational operator: " + op);
    }
}
