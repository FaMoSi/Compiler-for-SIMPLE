package models;
import util.Node;
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

        ev.openScope();

        for (SimpleParameter parameter: parameters) {
            if(ev.containsVariable(parameter.getID()) == false){
                ev.addVariable(parameter.getID(), parameter.getType());
            } else {
                semanticErrors.add(new SemanticError(Strings.VariablesAlreadyDeclared + parameter.getID()));
            }
        }

        semanticErrors.addAll(block.checkSemanticsFunction(ev, ef));


        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariables ev, EnvironmentFunctions ef) {
        return null;
    }
}
