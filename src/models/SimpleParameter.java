package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class SimpleParameter extends SimpleElementBase {

    private Boolean var;
    private String id;
    private String type;

    SimpleParameter(String id, String type, Boolean var){
        this.var = var;
        this.id = id;
        this.type = type;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        return new LinkedList<>();
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        return null;
    }

    public Boolean getVar(){ return var; }

    public String getType(){ return type;}

    public String getID(){ return id; }

}
