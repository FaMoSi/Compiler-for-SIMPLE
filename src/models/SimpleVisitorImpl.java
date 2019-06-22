package models;

import parser.SimpleBaseVisitor;
import parser.SimpleParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class SimpleVisitorImpl extends SimpleBaseVisitor<SimpleElementBase> {

	public HashMap<String, SimpleStmtBlock>functionAndBody = new HashMap<>();

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
		
		//construct assignment expression
		SimpleStmtAssignment assign = new SimpleStmtAssignment(exp, id);
		return assign;
	}

	@Override
	public SimpleElementBase visitFunctioncall(SimpleParser.FunctioncallContext ctx){

		String id = ctx.ID().getText();

		List<SimpleExp> simpleExps = new LinkedList<>();

		for (SimpleParser.ExpContext exp: ctx.exp()) {
			simpleExps.add((SimpleExp) visit(exp));
		}

		SimpleStmtBlock body = functionAndBody.get(id);


		return new SimpleStmtFunctionCall(id, simpleExps, body);
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

			return new SimpleStmtFunctionDeclaration(id,parameters, block);

		} else { //var

			//get expression
			SimpleExp exp = (SimpleExp) visit(ctx.exp());

			//get id of variable
			String id = ctx.ID().getText();

			String type = ctx.type().getText();

			SimpleStmtDeclaration simpleStmtDeclaration = new SimpleStmtDeclaration(id, type, exp);

			return simpleStmtDeclaration;
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
		if(ctx.op == null){
			return visit(ctx.left);
		} else {
			SimpleExp left = (SimpleExp) visit(ctx.left);
			SimpleExp right = (SimpleExp) visit(ctx.right);
			switch (ctx.op.getText()){
				case "+":
					return new SimpleExpSum(left, right);
				case "-":
					return new SimpleExpDiff(left, right);
				default:
					System.out.println("Error visitExp();");
			}
		}
		return null;
	}

	@Override
	public SimpleElementBase visitTerm(SimpleParser.TermContext ctx) {

		if(ctx.op == null){
			return visit(ctx.left);
		} else {

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
			}
		}
		return null;
	}

	@Override
	public SimpleElementBase visitFactor(SimpleParser.FactorContext ctx) {

		if(ctx.op == null){
			return visit(ctx.left);
		} else if (ctx.op.getText() != null){
			String operation = ctx.op.getText();

			SimpleExp leftSide = (SimpleExp) visit(ctx.left);
			SimpleExp rightSide = (SimpleExp) visit(ctx.right);
			switch (operation){
				case "&&":
					return  new SimpleExpAnd(leftSide,rightSide);
				case "||":
					return  new SimpleExpOr(leftSide,rightSide);
				case "==":
					return new SimpleExpROP(leftSide, rightSide);
				case ">=":
					return new SimpleExpROP(leftSide, rightSide);
				case "<=":
					return new SimpleExpROP(leftSide, rightSide);
				case "!=":
					return new SimpleExpROP(leftSide, rightSide);
				case ">":
					return new SimpleExpROP(leftSide, rightSide);
				case "<":
					return new SimpleExpROP(leftSide, rightSide);
					default:
						System.out.println("Error visitFactor();");
			}
		}

		return null;
	}

	@Override
	public SimpleElementBase visitValue(SimpleParser.ValueContext ctx) {

		if (ctx.INTEGER() != null) {
			return new SimpleExpVal("int");
		} else if (ctx.getText().equals("true") || ctx.getText().equals("false")) {
			return new SimpleExpVal("bool");
		} else if (ctx.exp() != null) {
			return visit(ctx.exp());
		} else if (ctx.ID() != null) {
			return new SimpleExpID(ctx.ID().getText());
		} else {
				System.out.println("Error visitValue();");
		}
		return null;
	}

	@Override
	public SimpleElementBase visitBlock(SimpleParser.BlockContext ctx) {
		
		//list for saving children statements
		List<SimpleStmt> children = new LinkedList<SimpleStmt>();
		
		//visit each children
		for(SimpleParser.StatementContext stmtCtx : ctx.statement())
			children.add((SimpleStmt) visitStatement(stmtCtx));
		
		//construct block statement expression
		SimpleStmtBlock block = new SimpleStmtBlock(children );
		
		return block;
	}
	
	@Override
	public SimpleElementBase visitDeletion(SimpleParser.DeletionContext ctx) {
		
		//construct delete expression with variable id
		SimpleStmtDelete delete = new SimpleStmtDelete(ctx.ID().getText());
		
		return delete;		
	}

	@Override
	public SimpleElementBase visitIfthenelse(SimpleParser.IfthenelseContext ctx) {

		SimpleExp guard = (SimpleExp) visit(ctx.exp());
		SimpleStmtBlock thenBlock = (SimpleStmtBlock) visit(ctx.block(0));
		SimpleStmtBlock elseBlock = (SimpleStmtBlock) visit(ctx.block(1));

		return new SimpleStmtIfThenElse(guard, thenBlock, elseBlock);
	}

	@Override
	public SimpleElementBase visitPrint(SimpleParser.PrintContext ctx) {
		
		//get expression
		SimpleExp exp = (SimpleExp) visit(ctx.exp());
		
		//construct print exp
		SimpleStmtPrint print = new SimpleStmtPrint(exp);
		
		return print;
	}
}
