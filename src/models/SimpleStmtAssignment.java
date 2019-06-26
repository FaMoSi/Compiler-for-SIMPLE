package models;

import javafx.util.Pair;
import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
import java.util.List;

public class SimpleStmtAssignment extends SimpleStmt{

	private SimpleExp exp;
	private String id;
	private Integer line;
	private Integer column;

	SimpleStmtAssignment(SimpleExp exp, String id, Integer line, Integer column) {
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
				res.add(new SemanticError(Strings.lineAndColunmn(line,column) + Strings.TypeMismatch));
			}
		}

		return res;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		List<Node> assignmentCode = new ArrayList<>();

		assignmentCode.addAll(exp.codeGeneration(ev, ef, oCgen));

		Pair<Integer, Integer> offsetAndNestingLevel =  ev.getOffsetAndNestingLevel(id);

		assignmentCode.add(oCgen.move("al", "fp"));

		for(int i = 0; i < oCgen.getNestingLevel() - offsetAndNestingLevel.getValue(); i++){
			assignmentCode.add(oCgen.lw("al", 0, "al"));
		}

		int offset = offsetAndNestingLevel.getKey();
		assignmentCode.add(oCgen.sw("a", offset, "al"));

		return assignmentCode;
	}
}
