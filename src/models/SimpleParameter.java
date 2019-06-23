package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class SimpleParameter extends SimpleElementBase {

    Boolean var;
    String id;
    String type;

    public SimpleParameter(String id, String type, Boolean var){
        this.var = var;
        this.id = id;
        this.type = type;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        return null;
    }

    public String getType(){ return type;}

    public String getID(){ return id; }

}
