package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.List;

public class SimpleStmtAssignment extends SimpleStmt{

	SimpleExp exp;
	String id;
	
	/**
	 * @param exp
	 * @param id
	 */
	public SimpleStmtAssignment(SimpleExp exp, String id) {
		this.exp = exp;
		this.id = id;
	}

	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		
		List<SemanticError> res = exp.checkSemantics(e, f);
		
		if(!e.getVariableType(id).equals(exp.getType(e))){
			res.add(new SemanticError(Strings.TypeMismatch));
		}
		
		return res;
		
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		return null;
	}


}
