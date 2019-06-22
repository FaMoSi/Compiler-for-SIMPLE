package models;

import parser.SimpleParser;
import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleStmtFunctionCall extends SimpleStmt {

    private List<SimpleExp> actualParams;

    private String id;

    private SimpleStmtBlock body;

    SimpleStmtFunctionCall(String id, List<SimpleExp> actualParams, SimpleStmtBlock body){
        this.id = id;
        this.actualParams = actualParams;
        this.body = body;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        for (SimpleExp exp: actualParams) {
            semanticErrors.addAll(exp.checkSemantics(e, f));
        }

        if(f.containsFunction(id) == false){
            semanticErrors.add(new SemanticError(Strings.FunctionNotDeclared + id));
        } else {
            List<SimpleParameter> parameters = f.getFunctionParameters(id);

            if(parameters.size() != actualParams.size()){
                semanticErrors.add(new SemanticError(Strings.MysmatchFormalActual));
            } else {
                for(int i = 0; i < parameters.size(); i++){

                        try{
                            if(parameters.get(i).var) {
                                SimpleExpID test = (SimpleExpID) actualParams.get(i);
                                e.createAssociationBetweenIdentifiers(parameters.get(i).id, test.getId());
                            }
                        }catch (ClassCastException err){
                            semanticErrors.add(new SemanticError(Strings.FunctionCallVarType));
                        }

                        if(!parameters.get(i).getType().equals(actualParams.get(i).getType(e))){
                            semanticErrors.add(new SemanticError(Strings.MysmatchTypeFunctionCall));
                        }
                }
            }
            semanticErrors.addAll(body.checkSemantics(e, f, parameters));
        }

        return semanticErrors;
    }
}
