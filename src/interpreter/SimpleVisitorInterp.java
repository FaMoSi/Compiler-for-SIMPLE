package interpreter;

import models.*;
import parser.SimpleBaseVisitor;
import parser.SimpleParser;
import util.Node;

import java.util.LinkedList;
import java.util.List;

public class SimpleVisitorInterp extends SimpleVisitorImpl {

    private int nestingLevel = 0;

    @Override
    public SimpleElementBase visitStatement(SimpleParser.StatementContext ctx) {
        //visit the first child, this works for every case
        return visit(ctx.getChild(0));
    }

    @Override
    public SimpleElementBase visitBlock(SimpleParser.BlockContext ctx) {

        //list for saving children statements
        List<SimpleStmt> children = new LinkedList<SimpleStmt>();

        //visit each children
        for(SimpleParser.StatementContext stmtCtx : ctx.statement())
            children.add((SimpleStmt) visitStatement(stmtCtx));

        //construct block statement expression
        SimpleStmtBlockInterp block = new SimpleStmtBlockInterp(children);

        return block;
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

            SimpleStmtBlockInterp block = (SimpleStmtBlockInterp) visit(ctx.block());

            return new SimpleStmtFunctionDeclarationInterp(id, parameters, block);

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
    public SimpleElementBase visitIfthenelse(SimpleParser.IfthenelseContext ctx) {

        SimpleExp guard = (SimpleExp) visit(ctx.exp());
        SimpleStmtBlockInterp thenBlock = (SimpleStmtBlockInterp) visit(ctx.block(0));
        SimpleStmtBlockInterp elseBlock = (SimpleStmtBlockInterp) visit(ctx.block(1));

        return new SimpleStmtIfThenElseInterp(guard, thenBlock, elseBlock);
    }

    @Override
    public SimpleElementBase visitFactor(SimpleParser.FactorContext ctx) {
        if(ctx.op == null){
            return (SimpleElementBase) visit(ctx.left);
        } else if (ctx.op.getText() != null){
            String operation = ctx.op.getText();

            SimpleExp leftSide = (SimpleExp) visit(ctx.left);
            SimpleExp rightSide = (SimpleExp) visit(ctx.right);
            switch (operation){
                case "&&":
                    return new SimpleExpAnd(leftSide,rightSide);
                case "||":
                    return new SimpleExpOr(leftSide,rightSide);
                case "==":
                    return new SimpleExpROPInterp(leftSide, rightSide, "==");
                case ">=":
                    return new SimpleExpROPInterp(leftSide, rightSide, ">=");
                case "<=":
                    return new SimpleExpROPInterp(leftSide, rightSide, "<=");
                case "!=":
                    return new SimpleExpROPInterp(leftSide, rightSide, "!=");
                case ">":
                    return new SimpleExpROPInterp(leftSide, rightSide, ">");
                case "<":
                    return new SimpleExpROPInterp(leftSide, rightSide, "<");
                default:
                    System.out.println("Error visitFactor();");
            }
        }

        return null;
    }

    @Override
    public SimpleElementBase visitValue(SimpleParser.ValueContext ctx) {

        if (ctx.INTEGER() != null) {
            return new SimpleExpValInterp("int", ctx.INTEGER().getText());
        } else if (ctx.getText().equals("true")) {
            return new SimpleExpValInterp("bool", "true");
        } else if (ctx.getText().equals("false")){
            return new SimpleExpValInterp("bool", "false");
        } else if (ctx.exp() != null) {
            return visit(ctx.exp());
        } else if (ctx.ID() != null) {
            return new SimpleExpID(ctx.ID().getText());
        } else {
            System.out.println("Error visitValue();");
        }
        return null;
    }
}
