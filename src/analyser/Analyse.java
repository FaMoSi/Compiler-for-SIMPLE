package analyser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Semantics.SimpleVisitorSemantic;
import interpreter.ExecuteVM;
import interpreter.SimpleVisitorInterp;
import models.*;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SimpleLexer;
import parser.SimpleParser;
import util.Node;
import util.OperationCodeGeneration;

public class Analyse {

	public static void main(String[] args) {
		
		String fileName = "test.spl";
		
		try{
			CharStream input = CharStreams.fromFileName(fileName);

			//create lexer
			SimpleLexer lexer = new SimpleLexer(input);
			lexer.removeErrorListeners();
			lexer.addErrorListener(new ErrorListener("Errors.txt"));

			//create parser
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			SimpleParser parser = new SimpleParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(new ErrorListener("Errors.txt"));

			//tell the parser to build the AST
			parser.setBuildParseTree(true);

			ErrorListener errorListener  = (ErrorListener) parser.getErrorListeners().get(0);

			//build custom visitor
			SimpleVisitorImpl visitor = new SimpleVisitorSemantic();

			SimpleParser.BlockContext block = parser.block();

			if(!errorListener.error()){

				//visit the root, this will recursively visit the whole tree
			SimpleStmtBlock mainBlock = (SimpleStmtBlock) visitor.visitBlock(block);

			//check semantics
			//give a fresh environment, no need to make it persist
			//this is just semantic checking
			List<SemanticError> errors = new LinkedList<>();

			errors.addAll(mainBlock.checkSemantics(new EnvironmentVariables(), new EnvironmentFunctions()));

			List<Node> codeGeneration = new LinkedList<>();

			//this means the semantic checker found some errors
			if(errors.size() > 0){
				System.out.println("Check semantics FAILED");			
				for(SemanticError err: errors)
					System.out.println(err);
			} else {
				System.out.println("Check semantics succeded");
				SimpleVisitorInterp visitorInterp = new SimpleVisitorInterp();
				SimpleStmtBlock mainInterpBlock = (SimpleStmtBlock) visitorInterp.visitBlock(block);
				codeGeneration = mainInterpBlock.codeGeneration(new EnvironmentVariablesWithOffset(), new EnvironmentFunctionsWithLabel(), new OperationCodeGeneration(0, 0));
			}

			if (codeGeneration != null && !codeGeneration.isEmpty()){
				codeGeneration.add(new Node("halt", null, null, null, null));

				Node[] code = new Node[codeGeneration.size()];

				code = codeGeneration.toArray(code);

				code = generateCodeWithLabelReference(code, codeGeneration);

				ExecuteVM vm = new ExecuteVM(code);

				vm.run();
			} else {
				System.out.println("Code Generation vuota");
			}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Node[] generateCodeWithLabelReference(Node[] code, List<Node> codeList) {
		HashMap<String, Integer> labelAdd = new HashMap<>();
		HashMap<String, List<Integer>> labelRef = new HashMap<>();
		List<Integer> indexList = new LinkedList<>();

		int i = 0;
		for (Node node: codeList) {
			switch (node.getInstr()){
				case ("beq"):
					indexList = labelRef.get(node.getArg3());
					if (indexList != null){
						indexList.add(i);
					} else {
						indexList = new LinkedList<>();
						indexList.add(i);
					}
					labelRef.put(node.getArg3(), indexList);
					code[i] = node;
					i++;
					break;
				case ("b"):
					indexList = labelRef.get(node.getArg1());
					if (indexList != null){
						indexList.add(i);
					} else {
						indexList = new LinkedList<>();
						indexList.add(i);
					}
					labelRef.put(node.getArg1(), indexList);
					code[i] = node;
					i++;
					break;
				case ("jal"):
					indexList = labelRef.get(node.getArg1());
					if (indexList != null){
						indexList.add(i);
					} else {
						indexList = new LinkedList<>();
						indexList.add(i);
					}
					labelRef.put(node.getArg1(), indexList);
					code[i] = node;
					i++;
					break;
				default:
					if (node.getInstr().startsWith("label") || node.getInstr().startsWith("flabel")){
						labelAdd.put(node.getInstr(), i);
						break;
					} else {
						code[i] = node;
						i++;
						break;
					}
			}
		}

		Map<String, List<Integer>> mapRef = new HashMap<>(labelRef);

		for (Map.Entry<String, List<Integer>> entry: mapRef.entrySet()) {
			for (Integer index: entry.getValue()) {
				code[index].setOffset(labelAdd.get(entry.getKey()));
			}
		}

		return code;
	}

}
