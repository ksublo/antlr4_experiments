// Generated from KSU_lang.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KSU_langParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KSU_langVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(KSU_langParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(KSU_langParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(KSU_langParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(KSU_langParser.IdListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(KSU_langParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(KSU_langParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMod(KSU_langParser.ModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(KSU_langParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(KSU_langParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Uminus}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUminus(KSU_langParser.UminusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(KSU_langParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(KSU_langParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StrConcat}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrConcat(KSU_langParser.StrConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational(KSU_langParser.RelationalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(KSU_langParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(KSU_langParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(KSU_langParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link KSU_langParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(KSU_langParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KSU_langParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(KSU_langParser.LiteralContext ctx);
}