package models;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentVariablesWithOffset extends EnvironmentVariables {
    private int offset = 1;

    private List<HashMap> identifierAndOffset = new LinkedList<>();

    public EnvironmentVariablesWithOffset(){ }

    public EnvironmentVariablesWithOffset(EnvironmentVariablesWithOffset copy){
        for (HashMap hashmap: copy.identifierAndOffset) {
            this.identifierAndOffset.add(new HashMap(hashmap));
        }
    }

    @Override
    public void openScope(){
        scopes.add(new HashMap());
        identifierAndOffset.add(new HashMap());
        scopesAndAddress.add(new HashMap());
        offset = 1;
    }

    void varDeclaration(String id){
        int nestingLevel = identifierAndOffset.size()-1;
        identifierAndOffset.get(nestingLevel).put(id, offset);
        offset++;
    }

    Pair getOffsetAndNestingLevel(String identifier){
        int nestingLevel = identifierAndOffset.size();
        for(int i = identifierAndOffset.size()-1; i >= 0; i--){
            if(identifierAndOffset.get(i).get(identifier) != null){
                int offset = (int) identifierAndOffset.get(i).get(identifier);
                return new Pair(offset, nestingLevel);

            }
            nestingLevel--;
        }
        return null;
    }

    @Override
    public void closeScope(){
        //remove the last block from the list of hashtable
        identifierAndOffset.remove(identifierAndOffset.size()-1);
        scopesAndAddress.remove(scopesAndAddress.size()-1);
        scopes.remove(scopes.size()-1);
    }
}
