package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SimpleExpVal extends SimpleExp {

	private String type;

	public SimpleExpVal(String type) {
		this.type = type;
	}

	/*
	 * No semantic errors here
	 * Checks if the variable in use exists. if it doesn't then add an error.
	 * @see models.SimpleElementBase#CheckSemantics(models.EnvironmentVariables)
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		List<SemanticError> result = new LinkedList<SemanticError>();
				
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		return null;
	}

	@Override
	public String getType(EnvironmentVariables e) {
		return type;
	}

}
