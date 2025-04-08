// Generated from KSU_lang.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KSU_langParser}.
 */
public interface KSU_langListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(KSU_langParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(KSU_langParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(KSU_langParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(KSU_langParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(KSU_langParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(KSU_langParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(KSU_langParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(KSU_langParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(KSU_langParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(KSU_langParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(KSU_langParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(KSU_langParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMod(KSU_langParser.ModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMod(KSU_langParser.ModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(KSU_langParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(KSU_langParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(KSU_langParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(KSU_langParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Uminus}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUminus(KSU_langParser.UminusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Uminus}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUminus(KSU_langParser.UminusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Var}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVar(KSU_langParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVar(KSU_langParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(KSU_langParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(KSU_langParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StrConcat}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrConcat(KSU_langParser.StrConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StrConcat}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrConcat(KSU_langParser.StrConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRelational(KSU_langParser.RelationalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRelational(KSU_langParser.RelationalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(KSU_langParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(KSU_langParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(KSU_langParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(KSU_langParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(KSU_langParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(KSU_langParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(KSU_langParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(KSU_langParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KSU_langParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(KSU_langParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KSU_langParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(KSU_langParser.LiteralContext ctx);
}