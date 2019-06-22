package models;

import util.Node;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpOr extends SimpleExp {
    SimpleExp leftSide, rightSide;


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
    public List<Node> codeGeneration(EnvironmentVariables ev, EnvironmentFunctions ef) {
        return null;
    }

    @Override
    public String getType(EnvironmentVariables e) {

        if(leftSide.getType(e).equals("bool") && rightSide.getType(e).equals("bool") ){
            return "bool";
        } else {
            return "err";
        }
    }

}

