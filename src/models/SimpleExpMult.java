package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpMult extends SimpleExp {
	
	private SimpleExp leftSide, rightSide;


	SimpleExpMult(SimpleExp leftSide, SimpleExp rightSide) {
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}

	/**
	 * Simply multiply values of both expressions
	 * 
	 */
	@Override
	public String getType(EnvironmentVariables e) {
		if(leftSide.getType(e).equals("int") && rightSide.getType(e).equals("int")){
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
		List<Node> timeCode = new ArrayList<>();

		List<Node> leftNodes;
		List<Node> rightNodes;

		leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
		timeCode.addAll(leftNodes);

		timeCode.addAll(oCgen.push("a"));

		rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
		timeCode.addAll(rightNodes);

		timeCode.add(oCgen.top("t"));
		timeCode.add(oCgen.time("a", "a", "t"));
		timeCode.add(oCgen.pop());

		return timeCode;
	}

}
