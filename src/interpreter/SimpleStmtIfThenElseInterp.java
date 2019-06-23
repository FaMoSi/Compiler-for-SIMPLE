package interpreter;

import models.*;
import util.Node;
import util.OperationCodeGeneration;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleStmtIfThenElseInterp extends SimpleStmt {

    private SimpleExp guard;
    private SimpleStmtBlockInterp thenBlock;
    private SimpleStmtBlockInterp elseBlock;

    public SimpleStmtIfThenElseInterp(SimpleExp guard, SimpleStmtBlockInterp thenBlock, SimpleStmtBlockInterp elseBlock){
        this.guard = guard;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {
        return null;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> ifThenElseCode = new ArrayList();

        String b_true = oCgen.getFreshLabel();
        String end_if = oCgen.getFreshLabel();

        List<Node> codeExp = guard.codeGeneration(ev, ef, oCgen);
        ifThenElseCode.addAll(codeExp);
        ifThenElseCode.add(oCgen.li("t", "1"));
        ifThenElseCode.add(oCgen.beq("a", "t", b_true));
        List<Node> elseCode = elseBlock.codeGeneration(ev, ef, oCgen);
        ifThenElseCode.addAll(elseCode);
        ifThenElseCode.add(oCgen.b(end_if));
        ifThenElseCode.add(oCgen.label(b_true));
        List<Node> thenCode = thenBlock.codeGeneration(ev, ef, oCgen);
        ifThenElseCode.addAll(thenCode);
        ifThenElseCode.add(oCgen.label(end_if));

        return ifThenElseCode;
    }
}
