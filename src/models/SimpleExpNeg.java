package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleExpNeg extends SimpleExp {
	
	private SimpleExp exp;


	SimpleExpNeg(SimpleExp exp) {
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
		List<SemanticError> result = new LinkedList<>();
		
		result.addAll(exp.checkSemantics(e,f));
			
		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> negCode = new ArrayList<>();

		negCode.addAll(exp.codeGeneration(ev, ef, oCgen));
		negCode.add(oCgen.li("t", "-1"));
		negCode.add(oCgen.time("a", "a", "t"));

		return negCode;
	}

}
