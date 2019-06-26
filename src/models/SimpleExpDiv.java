package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpDiv extends SimpleExp {
	
	private SimpleExp leftSide;
	private SimpleExp rightSide;

	SimpleExpDiv(SimpleExp leftSide, SimpleExp rightSide) {
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}

	/**
	 * Simply divide value of left expression by value of right expression
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
		List<Node> divCode = new ArrayList<>();

		List<Node> leftNodes;
		List<Node> rightNodes;

		leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
		divCode.addAll(leftNodes);

		divCode.addAll(oCgen.push("a"));

		rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
		divCode.addAll(rightNodes);

		divCode.add(oCgen.top("t"));
		divCode.add(oCgen.divide("a", "a", "t"));
		divCode.add(oCgen.pop());

		return divCode;
	}

}
