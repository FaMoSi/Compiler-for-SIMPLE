package lexical;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import util.Writer;

import java.io.FileWriter;

public class ErrorListener extends BaseErrorListener {

    private static String fileName;
    private static FileWriter outputFile;
    private Integer error;
    private Writer writer;

    public ErrorListener(Writer writer)
    {
        this.error = 0;
        this.writer = writer;
    }

    public Integer error(){ return error; }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
    {
        error++;
        String errorMessage = "Error:(" + line + ", " + charPositionInLine + ") " + msg + "\n";
        writer.write(errorMessage);
        System.out.println(errorMessage);

    }
}
