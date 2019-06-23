package interpreter;

import models.*;
import util.Node;
import util.OperationCodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpROPInterp extends SimpleExp {
    SimpleExp leftSide, rightSide;
    String operation;

    public SimpleExpROPInterp(SimpleExp leftSide, SimpleExp rightSide, String operation){
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operation = operation;
    }


    @Override
    public List<SemanticError> checkSemantics(EnvironmentVariables ev, EnvironmentFunctions ef) {
        return null;
    }

    @Override
    public List<Node> codeGeneration(EnvironmentVariablesWithOffset ev, EnvironmentFunctionsWithLabel ef, OperationCodeGeneration oCgen) {
        List<Node> ropCode = new LinkedList<>();

        List<Node> leftNodes;
        List<Node> rightNodes;

        leftNodes = leftSide.codeGeneration(ev, ef, oCgen);
        ropCode.addAll(leftNodes);

        ropCode.addAll(oCgen.push("a"));

        rightNodes = rightSide.codeGeneration(ev, ef, oCgen);
        ropCode.addAll(rightNodes);

        ropCode.add(oCgen.top("t"));

        if (operation.equals("==")){
            ropCode.add(new Node("eq", "a", 0, "a", "t"));
        } else if (operation.equals("!=")){
            ropCode.add(new Node("noteq", "a", 0, "a", "t"));
        } else if (operation.equals("<")){
            ropCode.add(new Node("smaller", "a", 0, "a", "t"));
        } else if (operation.equals(">")){
            ropCode.add(new Node("greater", "a", 0, "a", "t"));
        } else if (operation.equals(">=")){
            ropCode.add(new Node("smalleq", "a", 0, "a", "t"));
        } else if (operation.equals("<=")){
            ropCode.add(new Node("greateq", "a", 0, "a", "t"));
        }

        ropCode.add(oCgen.pop());

        return ropCode;
    }

    @Override
    public String getType(EnvironmentVariables e) {
        return null;
    }
}

