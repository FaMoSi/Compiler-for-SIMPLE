package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private String filename;

    public Writer(String filename){
        try {
            this.filename = filename;
            new FileWriter(filename, false);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void write(String txt){
        try {
            FileWriter outputFile = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(outputFile);
            out.write(txt + "\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }
}
