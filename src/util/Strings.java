package util;

public class Strings {

	public static final String lineAndColunmn(Integer line, Integer column){ return "Error:(" + line + ", " + column + ") "; }

	public static final String ErrorVariableDoesntExist = "Not variable found. Variable either doesnt exist or has been deleted. Variable name: ";

	public static final String VariablesAlreadyDeclared = "The variable has been already declared. Variables with ID: ";

	public static final String FunctionAlreadyDeclared = "The function has been already declared. Function with ID: ";

	public static final String FunctionNotDeclared = "The called function has not been declared. Function with ID: ";

	public static final String TypeMismatch = "The type of the expression and the type of the declared variables are not the same.";

	public static final String MysmatchFormalActual = "The number of formal parameter is different from the number of actual parameters.";

	public static final String MysmatchTypeFunctionCall = "The type of the formal parameter and the type of the actual parameters are different.";

	public static final String FunctionCallVarType = "The formal param is passed by var, then require a variable.";

	public static final String IfThenElseNotBalanced = "If then else not balanced.";

	public static final String IfThenElseWrongGuard = "The condition for if then else must be of type bool.";

}
