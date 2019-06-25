package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.List;

public class SimpleStmtAssignment extends SimpleStmt{

	SimpleExp exp;
	String id;
	Integer line;
	Integer column;
	
	/**
	 * @param exp
	 * @param id
	 */
	public SimpleStmtAssignment(SimpleExp exp, String id) {
		this.exp = exp;
		this.id = id;
	}

	public SimpleStmtAssignment(SimpleExp exp, String id, Integer line, Integer column) {
		this.exp = exp;
		this.id = id;
		this.line = line;
		this.column = column;
	}

	@Override
	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		
		List<SemanticError> res = exp.checkSemantics(e, f);

		if(!e.containsVariable(id)){
			res.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.ErrorVariableDoesntExist + id));
		} else {
			if(!e.getVariableType(id).equals(exp.getType(e))){
				res.add(new SemanticError(Strings.TypeMismatch));
			}
		}


		
		return res;
		
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		return null;
	}


}
