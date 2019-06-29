package models;

import util.Params;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnvironmentFunctions {

	//contains the stack of scopes. the last one is always the current active scope
	//this linked list is used as a stack with LIFO behavior
	private LinkedList<HashMap<String, Params>> scopes = new LinkedList<>();
	private LinkedList<HashMap<String, SimpleStmtBlock>> scopesAndBody = new LinkedList<>();
	private LinkedList<String> callStack = new LinkedList<>();


	public EnvironmentFunctions() { }

	public EnvironmentFunctions(EnvironmentFunctions ef){
		for (HashMap hashmap: ef.scopes) {
			this.scopes.push(new HashMap(hashmap));
		}
		for (HashMap hashmap: ef.scopesAndBody) {
			this.scopesAndBody.push(new HashMap(hashmap));
		}
		for (String id: ef.callStack) {
			this.callStack.push(id);
		}
	}


	void addFunction(String id, Params params, SimpleStmtBlock block) {
		// TODO Auto-generated method stub
		scopes.peek().put(id, params);
		scopesAndBody.peek().put(id, block);
	}

	SimpleStmtBlock getBody(String id){
		for (int i = scopesAndBody.size()-1; i >= 0; i--){
			if(scopesAndBody.get(i).get(id) != null){
				return scopesAndBody.get(i).get(id);
			}
		}
		return null;
	}

	/** 
	 * Inserts a new scope into the environment.
	 * When a scope is inserted old scope is clone so previous defined
	 * variables still exist
	 */
	void openScope(){
		scopes.push(new HashMap<>());
		scopesAndBody.push(new HashMap<>());
	}
	
	
	/**
	 * Drops the current scope and returns to the outer scope
	 * removing all changes and additions done within this scope 
	 */
	void closeScope(){
		scopes.pop();
		scopesAndBody.pop();
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

	void pushCallStack(String id){
		callStack.push(id);
	}

	void popCallStack(){
		callStack.pop();
	}

	boolean isFunctionRecursive(String id){
		return callStack.contains(id);
	}
}
