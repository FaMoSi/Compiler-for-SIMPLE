package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpOr extends SimpleExp {
    private SimpleExp leftSide, rightSide;


    public SimpleExpOr(SimpleExp leftSide, SimpleExp rightSide){
        this.leftSide = leftSide;
        this.rightSide = rightSide;
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
        List<Node> andCode = new ArrayList<>();

        List<Node> leftNodes;
        List<Node> rightNodes;

        leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
        andCode.addAll(leftNodes);

        andCode.addAll(oCgen.push("a"));

        rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
        andCode.addAll(rightNodes);

        andCode.add(oCgen.top("t"));
        andCode.add(new Node("or", "a", 0, "a", "t"));
        andCode.add(oCgen.pop());

        return andCode;    }
    @Override
    public String getType(EnvironmentVariables e) {

        if(leftSide.getType(e).equals("bool") && rightSide.getType(e).equals("bool") ){
            return "bool";
        } else {
            return "err";
        }
    }

}

