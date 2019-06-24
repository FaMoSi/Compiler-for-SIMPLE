package interpreter;

import models.*;
import parser.SimpleParser;

public class SimpleVisitorInterp extends SimpleVisitorImpl {

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
                    return new SimpleExpROP(leftSide, rightSide, operation);
                case ">=":
                    return new SimpleExpROP(leftSide, rightSide, operation);
                case "<=":
                    return new SimpleExpROP(leftSide, rightSide, operation);
                case "!=":
                    return new SimpleExpROP(leftSide, rightSide, operation);
                case ">":
                    return new SimpleExpROP(leftSide, rightSide, operation);
                case "<":
                    return new SimpleExpROP(leftSide, rightSide, operation);
                default:
                    System.out.println("Error visitFactor();");
            }
        }

        return null;
    }

    @Override
    public SimpleElementBase visitValue(SimpleParser.ValueContext ctx) {

        if (ctx.INTEGER() != null) {
            return new SimpleExpVal("int", ctx.INTEGER().getText());
        } else if (ctx.getText().equals("true")) {
            return new SimpleExpVal("bool", "true");
        } else if (ctx.getText().equals("false")){
            return new SimpleExpVal("bool", "false");
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
