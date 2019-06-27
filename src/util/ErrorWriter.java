package util;

import org.antlr.v4.codegen.model.OutputFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorWriter {
    private String filename;

    public ErrorWriter(String filename){
        try {
            this.filename = filename;
            new FileWriter(filename, false);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void write(String error){
        try {
            FileWriter outputFile = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(outputFile);
            out.write(error + "\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }
}
