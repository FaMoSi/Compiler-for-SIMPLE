package semantics;

import models.*;
import parser.SimpleParser;

public class SimpleVisitorSemantic extends SimpleVisitorImpl {
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
        Integer line = ctx.getStart().getLine();
        Integer column = ctx.getStart().getCharPositionInLine();

        if (ctx.INTEGER() != null) {
            return new SimpleExpVal("int");
        } else if (ctx.getText().equals("true") || ctx.getText().equals("false")) {
            return new SimpleExpVal("bool");
        } else if (ctx.exp() != null) {
            return visit(ctx.exp());
        } else if (ctx.ID() != null) {
            return new SimpleExpID(ctx.ID().getText(), line, column);
        } else {
            System.out.println("Error visitValue();");
        }
        return null;
    }
}
