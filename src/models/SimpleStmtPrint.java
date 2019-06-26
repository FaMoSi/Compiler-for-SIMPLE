package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.List;

public class SimpleStmtPrint extends SimpleStmt {

	private SimpleExp exp;

	/**
	 * Creates a print statement
	 * @param exp the expression of which we want to print its value
	 */
	SimpleStmtPrint(SimpleExp exp) {
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

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> printCode = new ArrayList<>();

		List<Node> expCode = exp.codeGeneration(ev, ef, oCgen);

		printCode.addAll(expCode);
		printCode.add(oCgen.print("a"));

		return printCode;
	}

}
