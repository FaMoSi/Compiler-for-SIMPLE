package models;

import parser.SimpleParser;
import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
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

                e.openScope();

                for (SimpleParameter parameter: parameters) {
                    if(e.containsVariable(parameter.getID()) == false){
                        e.addVariable(parameter.getID(), parameter.getType());
                    } else {
                        semanticErrors.add(new SemanticError(Strings.VariablesAlreadyDeclared + parameter.getID()));
                    }
                }

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


            semanticErrors.addAll(body.checkSemanticsFunction(e, f));
        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> functionCallCode = new ArrayList<>();

        List<String> variablesDeclared = ef.getVariablesDeclared(id);

        ev.openScope();

        if(variablesDeclared != null) {
            //loading local variables
            for (int i = variablesDeclared.size() - 1; i >= 0; i--) {
                functionCallCode.add(oCgen.addi("sp", "sp", "-1"));
            }
        }

        //loading parameters
        for(int i = actualParams.size()-1; i >= 0; i--){
            functionCallCode.addAll(actualParams.get(i).codeGeneration(ev, ef, oCgen));
            functionCallCode.addAll(oCgen.push("a"));
        }

        functionCallCode.add(oCgen.move("al","fp"));

        for(int i = 0; i < oCgen.getNestingLevel() - ef.getNestingLevel(id); i++){
            functionCallCode.add(oCgen.lw("al",0,"al"));
        }

        functionCallCode.addAll(oCgen.push("fp"));

        ev.closeScope();

        String functionLabel = ef.getFunctionLabel(id);
        functionCallCode.add(oCgen.jal(functionLabel));

        return functionCallCode;
    }

}
