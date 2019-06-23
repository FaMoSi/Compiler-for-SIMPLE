package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.LinkedList;
import java.util.List;

public class SimpleStmtIfThenElse extends SimpleStmt {

    SimpleExp guard;
    SimpleStmtBlock thenBlock;
    SimpleStmtBlock elseBlock;

    public SimpleStmtIfThenElse(SimpleExp guard, SimpleStmtBlock thenBlock, SimpleStmtBlock elseBlock){
        this.guard = guard;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;

    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        EnvironmentVariables thenEnvironment = new EnvironmentVariables(ev);
        EnvironmentVariables elseEnvironment = new EnvironmentVariables(ev);

        semanticErrors.addAll(thenBlock.checkSemanticsIfThenElse(thenEnvironment, ef));
        semanticErrors.addAll(elseBlock.checkSemanticsIfThenElse(elseEnvironment, ef));

        semanticErrors.addAll(guard.checkSemantics(ev, ef));

        if (guard.getType(ev) != "bool") {
            semanticErrors.add(new SemanticError(Strings.IfThenElseWrongGuard));
        }

        if (!thenEnvironment.equals(elseEnvironment) || !elseEnvironment.equals(thenEnvironment)) {
            semanticErrors.add(new SemanticError(Strings.IfThenElseNotBalanced));
        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        return null;
    }
}
