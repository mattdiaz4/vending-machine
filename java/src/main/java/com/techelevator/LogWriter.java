package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class LogWriter {

    private File file;
    public LogWriter(String nameOfFile) {
        this.file = new File(nameOfFile);
    }


    public void writeToFile(String lineOfText) {
        PrintWriter printWriter = null;
        if (this.file.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                printWriter = new PrintWriter(fileOutputStream);
                printWriter.println(lineOfText);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                printWriter = new PrintWriter(this.file);
                printWriter.println("first line.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        printWriter.flush();
        printWriter.close();
    }
}
