package models;

import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleStmtIfThenElse extends SimpleStmt {

    private SimpleExp guard;
    private SimpleStmtBlock thenBlock;
    private SimpleStmtBlock elseBlock;
    private Integer line;
    private Integer column;


    SimpleStmtIfThenElse(SimpleExp guard, SimpleStmtBlock thenBlock, SimpleStmtBlock elseBlock, Integer line, Integer column){
        this.guard = guard;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
        this.line = line;
        this.column = column;

    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {

        List<SemanticError> semanticErrors = new LinkedList<>();

        EnvironmentVariables thenEnvironmentVariables = new EnvironmentVariables(ev);
        EnvironmentVariables elseEnvironmentVariables = new EnvironmentVariables(ev);

        EnvironmentFunctions thenEnvironmentFunctions = new EnvironmentFunctions(ef);
        EnvironmentFunctions elseEnvironmentFunctions = new EnvironmentFunctions(ef);

        semanticErrors.addAll(thenBlock.checkSemantics(thenEnvironmentVariables, thenEnvironmentFunctions));
        semanticErrors.addAll(elseBlock.checkSemantics(elseEnvironmentVariables, elseEnvironmentFunctions));

        semanticErrors.addAll(guard.checkSemantics(ev, ef));

        if (!guard.getType(ev).equals("bool")) {
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line, column) + Strings.IfThenElseWrongGuard));
        }

        if (!thenEnvironmentVariables.equals(elseEnvironmentVariables) || !elseEnvironmentVariables.equals(thenEnvironmentVariables)) {
            semanticErrors.add(new SemanticError(Strings.lineAndColunmn(line, column) + Strings.IfThenElseNotBalanced));
        }

        return semanticErrors;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> ifThenElseCode = new ArrayList();

        String b_true = oCgen.getFreshLabel();
        String end_if = oCgen.getFreshLabel();

        EnvironmentVariablesWithOffset thenEnvironmentVariables = new EnvironmentVariablesWithOffset(ev);
        EnvironmentVariablesWithOffset elseEnvironmentVariables = new EnvironmentVariablesWithOffset(ev);

        EnvironmentFunctionsWithLabel thenEnvironmentFunctions = new EnvironmentFunctionsWithLabel(ef);
        EnvironmentFunctionsWithLabel elseEnvironmentFunctions = new EnvironmentFunctionsWithLabel(ef);


        List<Node> codeExp = guard.codeGeneration(ev, ef, oCgen);
        ifThenElseCode.addAll(codeExp);
        ifThenElseCode.add(oCgen.li("t", "1"));
        ifThenElseCode.add(oCgen.beq("a", "t", b_true));
        List<Node> elseCode = elseBlock.codeGeneration(elseEnvironmentVariables, elseEnvironmentFunctions, oCgen);
        ifThenElseCode.addAll(elseCode);
        ifThenElseCode.add(oCgen.b(end_if));
        ifThenElseCode.add(oCgen.label(b_true));
        List<Node> thenCode = thenBlock.codeGeneration(thenEnvironmentVariables, thenEnvironmentFunctions, oCgen);
        ifThenElseCode.addAll(thenCode);
        ifThenElseCode.add(oCgen.label(end_if));

        return ifThenElseCode;
    }
}
