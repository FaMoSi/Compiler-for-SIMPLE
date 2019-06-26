package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpROP extends SimpleExp {
    private SimpleExp leftSide, rightSide;
    private String operation;


    public SimpleExpROP(SimpleExp leftSide, SimpleExp rightSide){
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public SimpleExpROP(SimpleExp leftSide, SimpleExp rightSide, String operation){
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operation = operation;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {
        List<SemanticError> semanticErrors = new LinkedList<>();

        semanticErrors.addAll(leftSide.checkSemantics(ev, ef));
        semanticErrors.addAll(rightSide.checkSemantics(ev, ef));

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> ropCode = new ArrayList<>();

        List<Node> leftNodes;
        List<Node> rightNodes;

        leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
        ropCode.addAll(leftNodes);

        ropCode.addAll(oCgen.push("a"));

        rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
        ropCode.addAll(rightNodes);

        ropCode.add(oCgen.top("t"));

        switch (operation) {
            case "==":
                ropCode.add(new Node("eq", "a", 0, "a", "t"));
                break;
            case "!=":
                ropCode.add(new Node("noteq", "a", 0, "a", "t"));
                break;
            case "<":
                ropCode.add(new Node("smaller", "a", 0, "a", "t"));
                break;
            case ">":
                ropCode.add(new Node("greater", "a", 0, "a", "t"));
                break;
            case ">=":
                ropCode.add(new Node("smalleq", "a", 0, "a", "t"));
                break;
            case "<=":
                ropCode.add(new Node("greateq", "a", 0, "a", "t"));
                break;
        }

        ropCode.add(oCgen.pop());

        return ropCode;
    }

    @Override
    public String getType(EnvironmentVariables e) {

        if(leftSide.getType(e).equals(rightSide.getType(e)) && rightSide.getType(e).equals("int") ){
            return "bool";
        } else {
            return "err";
        }
    }
}

