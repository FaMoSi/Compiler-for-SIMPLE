package models;

import util.Params;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentFunctions {

	//contains the stack of scopes. the last one is always the current active scope
	//this linked list is used as a stack with LIFO behavior
	public LinkedList<HashMap<String, Params>> scopes = new LinkedList<HashMap<String,Params>>();

	/**
	 * Adds variable with the given id to existence	
	 * @param id
	 */
	public void addFunction(String id, Params params) {
		// TODO Auto-generated method stub
		scopes.peek().put(id, params);
	}
	
	
	/** 
	 * Inserts a new scope into the environment.
	 * When a scope is inserted old scope is clone so previous defined
	 * variables still exist
	 */
	public void openScope(){
		scopes.push(new HashMap<String, Params>());
	}
	
	
	/**
	 * Drops the current scope and returns to the outer scope
	 * removing all changes and additions done within this scope 
	 */
	public void closeScope(){
		scopes.pop();
	}
	
	/**
	 * Given an id determines if the variable belongs to the environment
	 * this is to check the scopes from inner to outer looking for the variable
	 * @param id
	 */
	public boolean containsFunction(String id){
		
		for(HashMap<String, Params> scope:scopes){
			if(scope.containsKey(id))
				return true;
		}
		
		return false;
	}

	public List<SimpleParameter> getFunctionParameters(String id){

		for(HashMap<String, Params> scope:scopes){
			if(scope.containsKey(id))
				return scope.get(id).getSimpleParameters();
		}

		return null;
	}
}
