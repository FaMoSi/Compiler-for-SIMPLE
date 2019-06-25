package models;

import javafx.util.Pair;
import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleStmtDeclaration extends SimpleStmt {

    private String id;
    private String type;
    private SimpleExp exp;
    private Integer line;
    private Integer column;

    public SimpleStmtDeclaration(String id, String type, SimpleExp exp){
        this.id = id;
        this.exp = exp;
        this.type = type;
    }

    public SimpleStmtDeclaration(String id, String type, SimpleExp exp, Integer line, Integer column){
        this.id = id;
        this.exp = exp;
        this.type = type;
        this.line = line;
        this.column = column;
    }


    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        if(ev.containsVariableLastBlock(id)){
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.VariablesAlreadyDeclared + id));
        } else {
            exp.checkSemantics(ev,ef);

            String expType = exp.getType(ev);

            if(type.equals(expType)){
                ev.addVariable(id,type);
            } else {
                semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.TypeMismatch));
            }

        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> codeDeclaration = new LinkedList<>();

        codeDeclaration.addAll(exp.codeGeneration(ev, ef, oCgen));

        Pair<Integer, Integer> offsetAndNestingLevel =  ev.getOffsetAndNestingLevel(id);

        codeDeclaration.add(oCgen.move("al", "fp"));

        for(int i = 0; i < oCgen.getNestingLevel() - offsetAndNestingLevel.getValue(); i++){
            codeDeclaration.add(oCgen.lw("al", 0, "al"));
        }

        int offset = offsetAndNestingLevel.getKey();
        codeDeclaration.add(oCgen.sw("a", offset, "al"));

        return codeDeclaration;
    }

    public String getID(){
        return id;
    }

    public String getType(){
        return type;
    }

    public SimpleExp getExp(){
        return exp;
    }
}
