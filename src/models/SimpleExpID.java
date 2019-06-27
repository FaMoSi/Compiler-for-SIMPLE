package models;

import javafx.util.Pair;
import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpID extends SimpleExp {

    private String id;
    private Integer line;
    private Integer column;

    public SimpleExpID(String id){
        this.id = id;
    }

    public SimpleExpID(String id, Integer line, Integer column){
        this.id = id;
        this.line = line;
        this.column = column;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {
        List<SemanticError> semanticErrors = new LinkedList<>();

        if(!ev.containsVariable(id)){
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.ErrorVariableDoesntExist+id));
        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> idCode = new ArrayList<>();
        Pair<Integer, Integer> offsetAndNestingLevel =  ev.getOffsetAndNestingLevel(id);

        idCode.add(oCgen.move("al", "fp"));

        int offset = offsetAndNestingLevel.getKey();

        for(int i = 0; i < oCgen.getNestingLevel() - offsetAndNestingLevel.getValue(); i++){
            idCode.add(oCgen.lw("al", 0, "al"));
        }

        idCode.add(oCgen.lw("a", offset, "al"));

        return idCode;
    }

    public String getType(EnvironmentVariables e){
        return e.getVariableType(id);
    }

    public String getID() { return id; }


}
