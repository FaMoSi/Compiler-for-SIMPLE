package models;

import util.Params;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentFunctions {

	//contains the stack of scopes. the last one is always the current active scope
	//this linked list is used as a stack with LIFO behavior
	private LinkedList<HashMap<String, Params>> scopes;

	public EnvironmentFunctions() {
		scopes = new LinkedList<HashMap<String,Params>>();
	}


	void addFunction(String id, Params params) {
		// TODO Auto-generated method stub
		scopes.peek().put(id, params);
	}
	
	
	/** 
	 * Inserts a new scope into the environment.
	 * When a scope is inserted old scope is clone so previous defined
	 * variables still exist
	 */
	void openScope(){
		scopes.push(new HashMap<String, Params>());
	}
	
	
	/**
	 * Drops the current scope and returns to the outer scope
	 * removing all changes and additions done within this scope 
	 */
	void closeScope(){
		scopes.pop();
	}

	 boolean containsFunction(String id){
		
		for(HashMap<String, Params> scope:scopes){
			if(scope.containsKey(id))
				return true;
		}
		
		return false;
	}

	boolean containsFunctionLastBlock(String id){
		return scopes.peek().get(id) != null;
	}

	 List<SimpleParameter> getFunctionParameters(String id){

		for(HashMap<String, Params> scope:scopes){
			if(scope.containsKey(id))
				return scope.get(id).getSimpleParameters();
		}

		return new LinkedList<>();
	}
}
