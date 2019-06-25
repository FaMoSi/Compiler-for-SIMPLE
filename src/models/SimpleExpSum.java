package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpSum extends SimpleExp {
	
	SimpleExp leftSide, rightSide;

	/**
	 * Represents a binary expression sum
	 * @param leftSide 
	 * @param rightSide
	 */
	public SimpleExpSum(SimpleExp leftSide, SimpleExp rightSide) {
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
		List<SemanticError> result = new LinkedList<SemanticError>();
		result.addAll(leftSide.checkSemantics(e, f));
		result.addAll(rightSide.checkSemantics(e, f));
		
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> sumCode = new LinkedList<>();

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
