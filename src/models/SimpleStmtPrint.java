package models;

import java.util.List;

public class SimpleStmtPrint extends SimpleStmt {

	private SimpleExp exp;

	/**
	 * Creates a print statement
	 * @param exp the expression of which we want to print its value
	 */
	public SimpleStmtPrint(SimpleExp exp) {
		this.exp = exp;
	}

	/*
	 * Simply check semantics in the expression
	 * @see models.SimpleElementBase#CheckSemantics(models.EnvironmentVariables)
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		return exp.checkSemantics(e, f);
	}

}
