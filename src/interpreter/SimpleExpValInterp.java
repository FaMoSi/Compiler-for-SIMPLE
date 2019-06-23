package interpreter;

import models.*;
import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SimpleExpValInterp extends SimpleExp {

	private String type;
	private String value;


	public SimpleExpValInterp(String type, String value) {
		this.type = type;
		this.value = value;
	}

	/*
	 * No semantic errors here
	 * Checks if the variable in use exists. if it doesn't then add an error.
	 * @see models.SimpleElementBase#CheckSemantics(models.EnvironmentVariables)
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		return null;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> valueCode = new ArrayList<>();

		if (value.equals("true")){
			valueCode.add(oCgen.li("a", "1"));
			return valueCode;
		} else if(value.equals("false")){
			valueCode.add(oCgen.li("a", "0"));
			return valueCode;
		} else {
			valueCode.add(oCgen.li("a", value));
			return valueCode;
		}
	}

	@Override
	public String getType(EnvironmentVariables e) {
		return type;
	}

}
