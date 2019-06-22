package models;

import util.Node;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpDiv extends SimpleExp {
	
	SimpleExp leftSide, rightSide;

	/**
	 * Represents a binary expression sum
	 * @param leftSide 
	 * @param rightSide
	 */
	public SimpleExpDiv(SimpleExp leftSide, SimpleExp rightSide) {
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
		List<SemanticError> result = new LinkedList<SemanticError>();
		
		result.addAll(leftSide.checkSemantics(e, f));
		result.addAll(rightSide.checkSemantics(e, f));
		
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariables ev, EnvironmentFunctions ef) {
		return null;
	}

}
