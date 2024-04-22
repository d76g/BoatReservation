package com.boat.services;

import java.util.Scanner;

public class ProgramUtils {
    // implement Scanner instance
    Scanner scanner ;
    // implement getInput method

    private static ProgramUtils instance = null;

    private ProgramUtils() {
        this.scanner = new Scanner(System.in);
    }

    public static ProgramUtils getInstance() {
        if (instance == null) {
            instance = new ProgramUtils();
        }
        return instance;
    }
    public String getInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }
    public void closeScanner() {
        scanner.close();
    }
    public void printMessage(String message) {
        System.out.println(message);
    }

}
