package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpROP extends SimpleExp {
    SimpleExp leftSide, rightSide;

    public SimpleExpROP(SimpleExp leftSide, SimpleExp rightSide){
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
        return null;
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

