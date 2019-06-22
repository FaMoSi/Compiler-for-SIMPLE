package models;

import util.Node;
import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpID extends SimpleExp {

    private String id;

    public SimpleExpID(String id){
        this.id = id;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {
        List<SemanticError> semanticErrors = new LinkedList<>();

        if(ev.containsVariable(id) == false){
            semanticErrors.add(new SemanticError(Strings.ErrorVariableDoesntExist+id));
        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariables ev, EnvironmentFunctions ef) {
        return null;
    }

    public String getType(EnvironmentVariables e){
        return e.getVariableType(id);
    }

    public String getId() { return id; }
}
