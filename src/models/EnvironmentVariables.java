package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EnvironmentVariables {

	//contains the stack of scopes. the last one is always the current active scope
	//this linked list is used as a stack with LIFO behavior
	public LinkedList<HashMap<String, String>> scopes = new LinkedList<HashMap<String,String>>();

	public LinkedList<HashMap<String, Integer>> scopesAndAddress = new LinkedList<>();

	private int uniqueIdentifier = 0;

	public EnvironmentVariables() {}

	public EnvironmentVariables(EnvironmentVariables ev){
		for (HashMap hashmap: ev.scopes) {
			this.scopes.add(new HashMap(hashmap));
		}
		for (HashMap hashmap: ev.scopesAndAddress) {
			this.scopesAndAddress.add(new HashMap(hashmap));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		EnvironmentVariables ev = (EnvironmentVariables) o;

		for (Map<String, String> map : scopes) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String id = entry.getKey();
				String type = entry.getValue();
				if (ev.getVariableType(id) == null || !ev.getVariableType(id).equals(type)) {
					return false;
				}
			}
		}
		return true;
	}

	private int getUniqueIdentifier() { return uniqueIdentifier++; }

	/**
	 * Adds variable with the given id to existence	
	 * @param id
	 */
	public void addVariable(String id, String type) {
		// TODO Auto-generated method stub
		scopes.peek().put(id, type);
		scopesAndAddress.peek().put(id, getUniqueIdentifier());
	}
	
	
	/** 
	 * Inserts a new scope into the environment.
	 * When a scope is inserted old scope is clone so previous defined
	 * variables still exist
	 */
	public void openScope(){

		scopes.push(new HashMap<String, String>());
		scopesAndAddress.push(new HashMap<String, Integer>());
	}
	
	
	/**
	 * Drops the current scope and returns to the outer scope
	 * removing all changes and additions done within this scope 
	 */
	public void closeScope(){

		scopes.pop();
		scopesAndAddress.pop();
	}

	public boolean containsVariableLastBlock(String id){
		return scopes.peek().containsKey(id);
	}


	/**
	 * Given an id determines if the variable belongs to the environment
	 * this is to check the scopes from inner to outer looking for the variable
	 * @param id
	 */
	public boolean containsVariable(String id){
		
		for(HashMap<String, String> scope:scopes){
			if(scope.containsKey(id))
				return true;
		}
		
		return false;
	}

	public void createAssociationBetweenIdentifiers(String formal, String actual){

		for (HashMap scopeFormal: scopesAndAddress){
			if(scopeFormal.get(formal) != null){
				for (HashMap scopeActual: scopesAndAddress){
					if(scopeActual.get(actual) != null){
						scopeFormal.put(formal, scopeActual.get(actual));
						break;
					}
				}
			}
		}
	}


	/**
	 * Remove the variable with the given id from the first scope that contains it
	 * notice that if the variable exists in an outer scope it will have
	 * that value
	 * @param id
	 */
	public void deleteVariable(String id){

		int addressToDelete = -1;

		for(HashMap<String, Integer> scope:scopesAndAddress){
			if(scope.containsKey(id)){
				addressToDelete = scope.get(id);
				break;
			}
		}

		int index = 0;
		if(addressToDelete != -1){
			for (HashMap paramToDelete: scopesAndAddress) {
				Map<String, Integer> map = new HashMap<String, Integer>(paramToDelete);

				for (Map.Entry<String, Integer> entry : map.entrySet()) {
					int address = entry.getValue();
					String identiferToDelete = entry.getKey();
					if(address == addressToDelete){
						paramToDelete.remove(identiferToDelete);
						scopes.get(index).remove(identiferToDelete);
					}
				}
				index++;
			}
		}
	}
	
	/**
	 * Check for variable
	 * @param id of the variable
	 * @return variable value, null if the variable doesnt exist
	 */
	public String getVariableType(String id){
		for(HashMap<String, String> scope:scopes){
			if(scope.containsKey(id)){
				return scope.get(id);				
			}
		}
		
		return null;
	}

}
