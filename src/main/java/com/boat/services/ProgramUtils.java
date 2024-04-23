package com.boat.services;

import org.jline.reader.*;
import org.jline.reader.impl.*;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.*;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;



import java.util.Scanner;

public class ProgramUtils {
    // implement Scanner instance
    private Scanner scanner ;
    private LineReader reader;
    private Terminal terminal;
    // implement getInput method

    private static ProgramUtils instance = null;

    private ProgramUtils() {

        this.scanner = new Scanner(System.in);
        try {
            terminal = TerminalBuilder.terminal();
            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new StringsCompleter("1", "2", "3", "4", "5", "6", "9"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ProgramUtils getInstance() {
        if (instance == null) {
            instance = new ProgramUtils();
        }
        return instance;
    }
    public String getInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
    public String readKey(String prompt) {
        if (reader != null) {
            System.out.print(prompt + " ");
            return reader.readLine();
        } else {
            return getInput(prompt); // Fallback to standard input if reader is null
        }
    }
    public void closeScanner() {
        scanner.close();
    }
    public void printMessage(String message) {
        System.out.println(message);
    }

}
