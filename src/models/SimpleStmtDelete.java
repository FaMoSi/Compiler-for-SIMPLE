package models;

import java.util.LinkedList;
import java.util.List;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

public class SimpleStmtDelete extends SimpleStmt {

	private String id;

	/**
	 * Creates a delete statement
	 * @param id the variable we want to delete
	 */
	public SimpleStmtDelete(String id) {
		this.id = id;
	}

	/*
	 * Checks if the variable in use exists. if it doesn't then add an error, 
	 * if it does then remove it from the current scope
	 * @see models.SimpleElementBase#CheckSemantics(models.EnvironmentVariables)
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		List<SemanticError> result = new LinkedList<SemanticError>();
		
		//check for the variable
		if(!e.containsVariable(id))
			result.add(new SemanticError(Strings.ErrorVariableDoesntExist + id));
		
		//if the variable does exist then delete it from the environment
		else
			e.deleteVariable(id);
		
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		return null;
	}

}
