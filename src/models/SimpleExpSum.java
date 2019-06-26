package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpSum extends SimpleExp {
	
	private SimpleExp leftSide, rightSide;


	SimpleExpSum(SimpleExp leftSide, SimpleExp rightSide) {
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}


	@Override
	public String getType(EnvironmentVariables e) {

		if (leftSide.getType(e).equals("int") && rightSide.getType(e).equals("int")) {
			return "int";
		} else {
			return "err";
		}
	}

	/**
	 * Check semantics in both side expressions
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		List<SemanticError> result = new LinkedList<>();

		result.addAll(leftSide.checkSemantics(e, f));
		result.addAll(rightSide.checkSemantics(e, f));
		
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> sumCode = new ArrayList<>();

		List<Node> leftNodes;
		List<Node> rightNodes;

		leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
		sumCode.addAll(leftNodes);

		sumCode.addAll(oCgen.push("a"));

		rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
		sumCode.addAll(rightNodes);

		sumCode.add(oCgen.top("t"));
		sumCode.add(oCgen.add("a", "a", "t"));
		sumCode.add(oCgen.pop());

		return sumCode;
	}
}
