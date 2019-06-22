package models;
import util.Params;
import util.Strings;

import java.util.*;
import java.util.LinkedList;
import java.util.List;

public class SimpleStmtFunctionDeclaration extends SimpleStmt {

    List<SimpleParameter> parameters;
    String id;
    SimpleStmtBlock block;

    SimpleStmtFunctionDeclaration(String id, List<SimpleParameter> parameters, SimpleStmtBlock block){
        this.id = id;
        this.parameters = parameters;
        this.block = block;

    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        List<Params> params = new LinkedList<>();

        if(ef.containsFunction(id) == false){
            ef.addFunction(id, new Params(parameters));
        } else {
            semanticErrors.add(new SemanticError(Strings.FunctionAlreadyDeclared + id));
        }

        semanticErrors.addAll(block.checkSemantics(ev, ef, parameters));


        return semanticErrors;
    }
}
