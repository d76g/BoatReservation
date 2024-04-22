package com.boat.controller;

import com.boat.model.Boat;
import com.boat.services.ProgramUtils;
import com.boat.view.ConsoleView;

import java.io.*;
import java.util.ArrayList;

public class BoatController {
    private ArrayList<Boat> boats;
    private ProgramUtils utils;
    BoatController(ProgramUtils utils){
        // TODO Implement the BoatController class
        this.boats = new ArrayList<>();
        this.utils = utils;
        try {
            readBoats("boats.csv");
        } catch (IOException e) {
            utils.printMessage("Error reading data from file: " + e.getMessage());
        }
    }

    public void listBoats() {
        for (Boat boat : boats) {
            utils.printMessage("Boat ID: " + boat.getId() + ", Name: " + boat.getName() + ", Type: " + boat.getType() + ", Capacity: " + boat.getCapacity());
        }
    }
    public void writeBoat(String filePath, Boat boat) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(String.format("%s,%s,%s,%d",
                    boat.getId(), boat.getName(), boat.getType(), boat.getCapacity()));
        }
    }

    public void readBoats(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Boat boat = new Boat(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    boats.add(boat);
                }
            }
        }
    }

    // Method to handle new boat creation
    public void addBoat() {
        String id = utils.getInput("Enter boat ID:");
        String name = utils.getInput("Enter boat name:");
        String type = utils.getInput("Enter boat type (e.g., rowing, electric):");
        int capacity = Integer.parseInt(utils.getInput("Enter boat capacity:"));
        Boat boat = new Boat(id, name, type, capacity);
        try {
            writeBoat("boats.csv", boat);
            boats.add(boat);  // Add to in-memory list after writing to file
            utils.printMessage("Boat added successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing boat to file: " + e.getMessage());
        }
    }
}
