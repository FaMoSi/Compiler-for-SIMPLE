package models;

import parser.SimpleBaseVisitor;
import parser.SimpleParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class SimpleVisitorImpl extends SimpleBaseVisitor<SimpleElementBase> {

	private HashMap<String, SimpleStmtBlock> functionAndBody = new HashMap<>();

	@Override
	public SimpleElementBase visitStatement(SimpleParser.StatementContext ctx) {
		//visit the first child, this works for every case
		return visit(ctx.getChild(0));
	}

	@Override
	public SimpleElementBase visitAssignment(SimpleParser.AssignmentContext ctx) {
		//get expression
		SimpleExp exp = (SimpleExp) visit(ctx.exp());

		//get id of variable
		String id = ctx.ID().getText();

		Integer line = ctx.start.getLine();
		Integer column = ctx.start.getCharPositionInLine();

		//construct assignment expression
		return new SimpleStmtAssignment(exp, id, line, column);
	}

	@Override
	public SimpleElementBase visitFunctioncall(SimpleParser.FunctioncallContext ctx){

		String id = ctx.ID().getText();

		List<SimpleExp> simpleExps = new LinkedList<>();

		for (SimpleParser.ExpContext exp: ctx.exp()) {
			simpleExps.add((SimpleExp) visit(exp));
		}

		SimpleStmtBlock body = functionAndBody.get(id);

		Integer line = ctx.start.getLine();
		Integer column = ctx.start.getCharPositionInLine();

		return new SimpleStmtFunctionCall(id, simpleExps, body, line, column);
	}

	@Override
	public SimpleElementBase visitDeclaration(SimpleParser.DeclarationContext ctx){

		if(ctx.type() == null){ // function

			String id = ctx.ID().getText();

			List<SimpleParser.ParameterContext> paramList = ctx.parameter();

			List<SimpleParameter> parameters = new LinkedList<>();

			for (SimpleParser.ParameterContext param: paramList) {
				parameters.add((SimpleParameter) visit(param));
			}

			SimpleStmtBlock block = (SimpleStmtBlock) visit(ctx.block());

			functionAndBody.put(id, block);

			Integer line = ctx.start.getLine();
			Integer column = ctx.start.getCharPositionInLine();

			return new SimpleStmtFunctionDeclaration(id, parameters, block, line, column);

		} else { //var

			//get expression
			SimpleExp exp = (SimpleExp) visit(ctx.exp());

			//get id of variable
			String id = ctx.ID().getText();

			String type = ctx.type().getText();

			Integer line = ctx.start.getLine();
			Integer column = ctx.start.getCharPositionInLine();

			return new SimpleStmtDeclaration(id, type, exp, line, column);
		}
	}

	@Override
	public SimpleElementBase visitParameter(SimpleParser.ParameterContext ctx) {

		String id = ctx.ID().getText();

		String type = ctx.type().getText();

		Boolean var = ctx.getText().startsWith("var");

		return new SimpleParameter(id, type, var);
	}

	@Override
	public SimpleElementBase visitExp(SimpleParser.ExpContext ctx) {

		//Case of a negated expression
		if(ctx.getText().startsWith("-")){
			SimpleExp expNeg = (SimpleExp) visit(ctx.left);
			return new SimpleExpNeg(expNeg);
		}

		if(ctx.op == null){
			return visit(ctx.left);
		}

		SimpleExp left = (SimpleExp) visit(ctx.left);
		SimpleExp right = (SimpleExp) visit(ctx.right);
		switch (ctx.op.getText()){
			case "+":
				return new SimpleExpSum(left, right);
			case "-":
				return new SimpleExpDiff(left, right);
			default:
				System.out.println("Error visitExp();");
				return null;
		}
	}

	@Override
	public SimpleElementBase visitTerm(SimpleParser.TermContext ctx) {

		if(ctx.op == null){
			return visit(ctx.left);
		}

		String operation = ctx.op.getText();

		SimpleExp left = (SimpleExp) visit(ctx.left);
		SimpleExp right = (SimpleExp) visit(ctx.right);

		switch (operation){
			case "*":
				return new SimpleExpMult(left, right);
			case "/":
				return new SimpleExpDiv(left, right);
			default:
				System.out.println("Error visitTerm();");
				return null;
		}
	}

	@Override
	public SimpleElementBase visitBlock(SimpleParser.BlockContext ctx) {

		//list for saving children statements
		List<SimpleStmt> children = new LinkedList<>();

		//visit each children
		for(SimpleParser.StatementContext stmtCtx : ctx.statement())
			children.add((SimpleStmt) visitStatement(stmtCtx));

		//construct block statement expression
		return new SimpleStmtBlock(children);
	}

	@Override
	public SimpleElementBase visitDeletion(SimpleParser.DeletionContext ctx) {

		Integer line = ctx.start.getLine();
		Integer column = ctx.start.getCharPositionInLine();

		//construct delete expression with variable id
		return new SimpleStmtDelete(ctx.ID().getText(), line, column);
	}

	@Override
	public SimpleElementBase visitIfthenelse(SimpleParser.IfthenelseContext ctx) {

		Integer line = ctx.start.getLine();
		Integer column = ctx.start.getCharPositionInLine();

		SimpleExp guard = (SimpleExp) visit(ctx.exp());
		SimpleStmtBlock thenBlock = (SimpleStmtBlock) visit(ctx.block(0));
		SimpleStmtBlock elseBlock = (SimpleStmtBlock) visit(ctx.block(1));

		return new SimpleStmtIfThenElse(guard, thenBlock, elseBlock, line, column);
	}

	@Override
	public SimpleElementBase visitPrint(SimpleParser.PrintContext ctx) {

		//get expression
		SimpleExp exp = (SimpleExp) visit(ctx.exp());

		//construct print exp
		return new SimpleStmtPrint(exp);
	}
}
