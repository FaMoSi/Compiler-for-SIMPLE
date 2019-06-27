package lexical;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import util.ErrorWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorListener extends BaseErrorListener {

    private static String fileName;
    private static FileWriter outputFile;
    private Integer error;
    private ErrorWriter errorWriter;

    public ErrorListener(ErrorWriter errorWriter)
    {
        this.error = 0;
        this.errorWriter = errorWriter;
    }

    public Integer error(){ return error; }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
    {
        error++;
        String errorMessage = "Error:(" + line + ", " + charPositionInLine + ") " + msg + "\n";
        errorWriter.write(errorMessage);
        System.out.println(errorMessage);

    }
}
