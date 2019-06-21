package models;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpAnd extends SimpleExp {

    SimpleExp leftSide, rightSide;

    public SimpleExpAnd(SimpleExp leftSide, SimpleExp rightSide){
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
    public String getType(EnvironmentVariables e) {

        if(leftSide.getType(e).equals("bool") && rightSide.getType(e).equals("bool") ){
            return "bool";
        } else {
            return "err";
        }
    }
}
