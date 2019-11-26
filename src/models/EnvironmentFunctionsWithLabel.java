package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentFunctionsWithLabel {
    private int labelCounter = 0;

    private LinkedList<HashMap> identifierAndLabel = new LinkedList<>();

    private LinkedList<HashMap> identifierAndVariablesDeclared = new LinkedList<>();

    public EnvironmentFunctionsWithLabel() {}

    public EnvironmentFunctionsWithLabel(EnvironmentFunctionsWithLabel copy){
        for (HashMap hashmap: copy.identifierAndLabel) {
            this.identifierAndLabel.add(new HashMap(hashmap));
        }

        for (HashMap hashmap: copy.identifierAndVariablesDeclared) {
            this.identifierAndVariablesDeclared.add(new HashMap(hashmap));
        }
    }


    void openScope() {
        identifierAndLabel.push(new HashMap());
        identifierAndVariablesDeclared.push(new HashMap());
    }

    String newFunctionDeclaration(String identifier, List<String> variablesDeclared, SimpleStmtBlock body){
        String freshLabel = getFreshLabel();
        identifierAndVariablesDeclared.peek().put(identifier, variablesDeclared);
        identifierAndLabel.peek().put(identifier, freshLabel);

        return freshLabel;
    }

    String getFunctionLabel(String identifier){
        for(int i = 0 ; i < identifierAndLabel.size(); i++){
            if(identifierAndLabel.get(i).get(identifier) != null){
                return (String) identifierAndLabel.get(i).get(identifier);
            }
        }
        return null;
    }

    List<String> getVariablesDeclared(String identifier){
        for (HashMap hashMap: identifierAndVariablesDeclared){
            if(hashMap.get(identifier) != null){
                return (List<String>) hashMap.get(identifier);
            }
        }
        return null;
    }

    int getNestingLevel(String identifier){
        int j = 1;
        for(int i = identifierAndLabel.size()-1; i >= 0 ; i--){
            if(identifierAndLabel.get(i).get(identifier) != null){
                return j;
            }
            j++;
        }
        return -1;
    }

    private String getFreshLabel(){
        return "flabel" + labelCounter++;
    }

    void closeScope() {
        identifierAndLabel.pop();
        identifierAndVariablesDeclared.pop();
    }
}
