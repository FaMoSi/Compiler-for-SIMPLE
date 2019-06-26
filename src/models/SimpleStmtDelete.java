package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

public class SimpleStmtDelete extends SimpleStmt {

	private String id;
	private Integer line;
	private Integer column;


	/**
	 * Creates a delete statement
	 * @param id the variable we want to delete
	 */
	SimpleStmtDelete(String id, Integer line, Integer column) {
		this.id = id;
		this.line = line;
		this.column = column;
	}

	/*
	 * Checks if the variable in use exists. if it doesn't then add an error, 
	 * if it does then remove it from the current scope
	 * @see models.SimpleElementBase#CheckSemantics(models.EnvironmentVariables)
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		List<SemanticError> result = new LinkedList<>();

		//check for the variable
		if(!e.containsVariable(id)) {
            result.add(new SemanticError(Strings.lineAndColunmn(line, column) + Strings.ErrorVariableDoesntExist + id));
        } else {
            e.deleteVariable(id);
        }
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> deleteCode = new ArrayList<>();
			ev.deleteVariable(id);
		return deleteCode;
	}

}
