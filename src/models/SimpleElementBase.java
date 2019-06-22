package models;

import java.util.List;

public abstract class SimpleElementBase {

	public abstract List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef);

}
