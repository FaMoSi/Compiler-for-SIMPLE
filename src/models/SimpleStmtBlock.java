package models;

import parser.SimpleParser;
import util.Node;
import util.OperationCodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleStmtBlock extends SimpleStmt {

	List<SimpleStmt> children;

	/**
	 * Creates a new block
	 * @param children: the list of direct children elements of the block
	 */
	public SimpleStmtBlock(List<SimpleStmt> children, SimpleParser.BlockContext block) {
		this.children = children;
	}

	public SimpleStmtBlock() {

	}


	public List<SemanticError> checkSemantics(EnvironmentVariables e, EnvironmentFunctions f) {
		//create scope for inner elements
		e.openScope();
		f.openScope();

		//initialize result variable
		LinkedList<SemanticError> result = new LinkedList<SemanticError>();

		//check children semantics
		if(children!=null)
			for(SimpleStmt el:children)
				result.addAll(el.checkSemantics(e, f));

		//close scope for this block

		f.closeScope();
		e.closeScope();

		return result;
	}


	public List<SemanticError> checkSemanticsIfThenElse(EnvironmentVariables e, EnvironmentFunctions f) {
		//create scope for inner elements
		e.openScope();
		f.openScope();

		//initialize result variable
		LinkedList<SemanticError> result = new LinkedList<SemanticError>();

		//check children semantics
		if(children!=null)
			for(SimpleStmt el:children)
				result.addAll(el.checkSemantics(e, f));

		//close scope for this block

		return result;
	}

	public List<SemanticError> checkSemanticsFunction(EnvironmentVariables e, EnvironmentFunctions f) {

		//initialize result variable
		LinkedList<SemanticError> result = new LinkedList<SemanticError>();

		//create scope for inner elements
		//e.openScope();

		f.openScope();

		//check children semantics
		if(children!=null)
			for(SimpleStmt el:children)
				result.addAll(el.checkSemantics(e, f));

		//close scope for this block

		f.closeScope();
		e.closeScope();

		return result;
	}

	@Override
	public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
		return null;
	}


}
