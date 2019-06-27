package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleStmtFunctionCall extends SimpleStmt {

    private List<SimpleExp> actualParams;
    private String id;
    private Integer line;
    private Integer column;

    SimpleStmtFunctionCall(String id, List<SimpleExp> actualParams, Integer line, Integer column){
        this.id = id;
        this.actualParams = actualParams;
        this.line = line;
        this.column = column;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        for (SimpleExp exp: actualParams) {
            semanticErrors.addAll(exp.checkSemantics(e, f));
        }

        //checking if the function exist
        if(!f.containsFunction(id)){
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.FunctionNotDeclared + id));
            return semanticErrors;
        }

        List<SimpleParameter> parameters = f.getFunctionParameters(id);

        e.openScope();

        //checking if all parameters has been provided
        if(parameters.size() != actualParams.size()){
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.MysmatchFormalActual));
            return semanticErrors;
        }

        //checking if every parameter has the right type
        for (SimpleParameter parameter: parameters) {
            if(e.containsVariableLastBlock(parameter.getID())){
                semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.VariablesAlreadyDeclared + parameter.getID()));
                return semanticErrors;
            }

            e.addVariable(parameter.getID(), parameter.getType());
        }

        //checking if for every parameter passed by var has been provided an identifier
        for(int i = 0; i < parameters.size(); i++){
            try{
                if(parameters.get(i).getVar()) {
                    SimpleExpID test = (SimpleExpID) actualParams.get(i);
                    e.createAssociationBetweenIdentifiers(parameters.get(i).getID(), test.getID());
                }
            }catch (ClassCastException err){
                semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.FunctionCallVarType));
            }
            //if not add error
            if(!parameters.get(i).getType().equals(actualParams.get(i).getType(e))){
                semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.MysmatchTypeFunctionCall));
                return semanticErrors;
            }
        }

        SimpleStmtBlock body = f.getBody(id);

        //the body could be null if the function is calling itself
        if(body != null && !body.getFunctionID().equals(id)){
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
