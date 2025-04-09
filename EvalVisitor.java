import org.antlr.v4.runtime.tree.*;

import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends KSU_langBaseVisitor<Object> {

    private final Map<String, Object> memory = new HashMap<>();
    private final Map<String, String> symbolTable = new HashMap<>();
    private void checkTypeConsistency(Object left, Object right, String op) {
        if (left instanceof Integer && right instanceof Integer) {
            return;
        }
        if (left instanceof Double && right instanceof Double) {
            return;
        }
        if (left instanceof String && right instanceof String && op.equals("+")) {
            return;
        }
        if (left instanceof Boolean && right instanceof Boolean) {
            if (op.equals("&&") || op.equals("||")) {
                return;
            }
        }

        throw new RuntimeException("Type mismatch: " + left.getClass().getName() + " and " + right.getClass().getName());
    }

    @Override
    public Object visitProg(KSU_langParser.ProgContext ctx) {
        for (KSU_langParser.StatContext stat : ctx.stat()) {
            visit(stat);
        }
        return null;
    }

    @Override
    public Object visitAssign(KSU_langParser.AssignContext ctx) {
        String varName = ctx.ID().getText();
        Object value = visit(ctx.expr());

        symbolTable.put(varName, value.getClass().getSimpleName());
        memory.put(varName, value);

        return value;
    }

    @Override
    public Object visitVar(KSU_langParser.VarContext ctx) {
        String name = ctx.ID().getText();
        if (!symbolTable.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' used before declaration.");
        }
        return memory.get(name);
    }

    @Override
    public Object visitLiteral(KSU_langParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            return Integer.decode(ctx.INT().getText());
        }
        if (ctx.FLOAT() != null) {
            return Double.parseDouble(ctx.FLOAT().getText());
        }
        if (ctx.BOOL() != null) {
            return Boolean.parseBoolean(ctx.BOOL().getText());
        }
        if (ctx.STRING() != null) {
            return ctx.STRING().getText().replaceAll("^\"|\"$", "");
        }
        return null;
    }

    @Override
    public Object visitParens(KSU_langParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitAddSub(KSU_langParser.AddSubContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (left instanceof Integer && right instanceof Double) {
            left = ((Integer) left).doubleValue();
        } else if (left instanceof Double && right instanceof Integer) {
            right = ((Integer) right).doubleValue();
        }

        checkTypeConsistency(left, right, op);

        if (left instanceof Integer && right instanceof Integer) {
            return op.equals("+") ? (Integer) left + (Integer) right : (Integer) left - (Integer) right;
        }

        if (left instanceof Double && right instanceof Double) {
            return op.equals("+") ? (Double) left + (Double) right : (Double) left - (Double) right;
        }

        return null;
    }

    @Override
    public Object visitMulDiv(KSU_langParser.MulDivContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (left instanceof Integer && right instanceof Double) {
            left = ((Integer) left).doubleValue();
        } else if (left instanceof Double && right instanceof Integer) {
            right = ((Integer) right).doubleValue();
        }

        checkTypeConsistency(left, right, op);

        if (left instanceof Integer && right instanceof Integer) {
            return op.equals("*") ? (Integer) left * (Integer) right : (Integer) left / (Integer) right;
        }

        if (left instanceof Double && right instanceof Double) {
            return op.equals("*") ? (Double) left * (Double) right : (Double) left / (Double) right;
        }

        return null;
    }

    @Override
    public Object visitMod(KSU_langParser.ModContext ctx) {
        Object left = visit(ctx.expr(0));
        Object right = visit(ctx.expr(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (Integer) left % (Integer) right;
        }

        throw new RuntimeException("Modulo operation can only be used with integers.");
    }

    @Override
    public Object visitStat(KSU_langParser.StatContext ctx) {
        if (ctx.expr() != null) {
            Object value = visit(ctx.expr());
            System.out.println(value);
            return value;
        }
        return null;
    }

    @Override
    public Object visitFor(KSU_langParser.ForContext ctx) {
        if (ctx.expr(0) != null) {
            visit(ctx.expr(0));
        }

        while ((Boolean) visit(ctx.expr(1))) {
            visit(ctx.stat());

            if (ctx.expr(2) != null) {
                visit(ctx.expr(2));
            }
        }

        return null;
    }

}
