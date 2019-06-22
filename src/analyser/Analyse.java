package analyser;

import java.io.IOException;
import java.util.List;

import models.*;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SimpleLexer;
import parser.SimpleParser;
import util.Node;

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

			//build custom visitor
			SimpleVisitorImpl visitor = new SimpleVisitorImpl();
			
			//visit the root, this will recursively visit the whole tree
			SimpleStmtBlock mainBlock = (SimpleStmtBlock) visitor.visitBlock(parser.block());
			
			//check semantics
			//give a fresh environment, no need to make it persist
			//this is just semantic checking
			List<SemanticError> errors = mainBlock.checkSemantics(new EnvironmentVariables(), new EnvironmentFunctions());


			//this means the semantic checker found some errors
			if(errors.size() > 0){
				System.out.println("Check semantics FAILED");			
				for(SemanticError err: errors)
					System.out.println(err);
			}else{
				System.out.println("Check semantics succeded");
				List<Node> codeGeneration = mainBlock.codeGeneration(new EnvironmentVariables(), new EnvironmentFunctions());
			}


		/*}catch(RecognitionException e){
			System.out.println("Some errors where found in the parsing process");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
