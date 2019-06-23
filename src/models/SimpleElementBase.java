package models;

import util.Node;
import util.OperationCodeGeneration;

import java.util.List;

public abstract class SimpleElementBase {

	public abstract List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef);

	public abstract List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen);
}
