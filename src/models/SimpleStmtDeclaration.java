package models;

import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleStmtDeclaration extends SimpleStmt {

    private String id;
    private String type;
    private SimpleExp exp;

    public SimpleStmtDeclaration(String id, String type, SimpleExp exp){
        this.id = id;
        this.exp = exp;
        this.type = type;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        if(ev.containsVariable(id)){
            semanticErrors.add(new SemanticError(Strings.VariablesAlreadyDeclared + id));
        } else {

            exp.checkSemantics(ev,ef);

            String expType = exp.getType(ev);

            if(type.equals(expType)){
                ev.addVariable(id,type);
            } else {
                semanticErrors.add(new SemanticError(Strings.TypeMismatch));
            }

        }

        return semanticErrors;
    }
}
