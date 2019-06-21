package models;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpNeg extends SimpleExp {
	
	SimpleExp exp;

	/**
	 * Represents a negated expression
	 * @param exp 
	 * @param rightSide
	 */
	public SimpleExpNeg(SimpleExp exp) {
		this.exp = exp;		
	}

	/**
	 * Simply rest right expression from left expression
	 * 
	 */
	@Override
	public String getType(EnvironmentVariables e) {
		if(exp.getType(e).equals("int")){
			return "int";
		} else {
			return "err";
		}
	}

	/**
	 * Check semantics in the expression
	 */
	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		List<SemanticError> result = new LinkedList<SemanticError>();
		
		result.addAll(exp.checkSemantics(e,f));
			
		return result;
	}

}
