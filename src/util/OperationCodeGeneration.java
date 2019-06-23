package util;

import java.util.ArrayList;
import java.util.List;

public class OperationCodeGeneration {

    private int labelCounter;
    private int nestingLevel;

    public OperationCodeGeneration(int labelCounter, int nestingLevel){
        this.labelCounter = labelCounter;
        this.nestingLevel = nestingLevel;
    }

    public Node top(String register){
        return lw(register, 0, "sp");
    }

    public Node pop(){
        return addi("sp", "sp", "1");
    }

    public List<Node> push(String register){
        List<Node> pushCode = new ArrayList<>();
        pushCode.add(addi("sp", "sp", "-1"));
        pushCode.add(sw(register, 0, "sp"));
        return pushCode;
    }

    public Node lw(String arg1, Integer offset, String arg2){
        return new Node ("lw", arg1, offset, arg2, null);
    }

    public Node li(String arg1, String arg2){
        return new Node ("li", arg1, null, arg2, null);
    }

    public Node sw(String arg1, Integer offset, String arg2){
        return new Node ("sw", arg1, offset, arg2, null);
    }

    public Node addi(String arg1, String arg2, String arg3){
        return new Node ("addi", arg1, null, arg2, arg3);
    }

    public Node add(String arg1, String arg2, String arg3){
        return new Node ("add", arg1, null, arg2, arg3);
    }

    public Node sub(String arg1, String arg2, String arg3) {
        return new Node("sub", arg1, null, arg2, arg3);
    }

    public Node move(String arg1, String arg2) {
        return new Node("move", arg1, null, arg2, null);
    }

    public Node print(String arg1) {
        return new Node("print", arg1, null, null, null);
    }

    public Node time(String arg1, String arg2, String arg3) {
        return new Node("time", arg1, null, arg2, arg3);
    }

    public Node divide(String arg1, String arg2, String arg3) {
        return new Node("divide", arg1, null, arg2, arg3);
    }

    public Node beq(String arg1, String arg2, String arg3) {
        return new Node("beq", arg1, null, arg2, arg3);
    }

    public Node label(String label) {
        return new Node(label, null, null, null, null);
    }

    public Node b(String label) {
        return new Node("b", label, null, null, null);
    }

    public Node jr(String register) {
        return new Node("jr", register, null, null, null);
    }

    public Node jal(String label) {
        return new Node("jal", label, null, null, null);
    }

    public String getFreshLabel(){
        return "label" + labelCounter++;
    }

    public int getNestingLevel(){return nestingLevel;}

    public int increaseNestingLevel(){
        return nestingLevel++;
    }

    public int decreaseNestingLevel(){
        return nestingLevel--;
    }
}
