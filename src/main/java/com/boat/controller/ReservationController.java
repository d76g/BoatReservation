// File: controller/ReservationController.java
package com.boat.controller;
import com.boat.model.Reservation;
import com.boat.model.Reservations;
import com.boat.model.User;
import com.boat.services.ProgramUtils;
import com.boat.view.ReservationScreen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class ReservationController {
    private Reservations reservations;
    private ProgramUtils utils;
    private Authentication auth;
    public ReservationController(User currentUser){
        this.reservations = new Reservations();
        this.utils = ProgramUtils.getInstance();
        try{
            readReservations("reservations.csv");
        } catch (IOException e) {
            utils.printMessage("Error reading data from file"+e.getMessage());
        }
    }
    // Existing methods updated with try-catch for IO operations
    public void createReservation() {
        utils.printMessage("Enter '9' to cancel reservation creation at any step or to exit.");
        String id = reservations.getReservationCount() + 1 + "";
        String boatId = utils.getInput("Enter boat ID:");
        if (boatId.equals("9")) {
            utils.printMessage("Reservation creation cancelled.");
            return;
        }
        String customerId = utils.getInput("Enter customer ID:");
        if (customerId.equals("9")) {
            utils.printMessage("Reservation creation cancelled.");
            return;
        }
        String date = utils.getInput("Enter date of reservation (YYYY-MM-DD): using 'today' for today's date");
        if (date.equals("9")) {
            utils.printMessage("Reservation creation cancelled.");
            return;
        } else if (date.equalsIgnoreCase("today")) {
            date = LocalDate.now().toString();
        }
        Reservation newReservation = new Reservation(id, boatId, customerId, date);

        try {
            writeReservation("reservations.csv", newReservation);
            reservations.addReservation(newReservation); // Add to in-memory list after writing to file
            utils.printMessage("Reservation created successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing reservation to file: " + e.getMessage());
        }
    }
    // edit reservation
    public void editReservation() {
        // display all reservations
        listReservations();
        utils.printMessage("Update reservation +-------------------------------------------+");
        utils.printMessage("Enter '9' to cancel reservation editing at any step or to exit.");
        utils.printMessage("+-------------------------------------------+");
        String reservationId = utils.getInput("Enter reservation ID:");
        if (reservationId.equals("9")) {
            utils.printMessage("Reservation editing cancelled.");
            return;
        }
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) {
            utils.printMessage("Reservation not found.");
            return;
        }
        String boatId = utils.getInput("Enter new boat ID:");
        if (boatId.equals("9")) {
            utils.printMessage("Reservation editing cancelled.");
            return;
        } else if(boatId.isEmpty()){
            boatId = reservation.getBoatId();
        }
        String customerId = utils.getInput("Enter new customer ID:");
        if (customerId.equals("9")) {
            utils.printMessage("Reservation editing cancelled.");
            return;
        } else if(customerId.isEmpty()){
            customerId = reservation.getCustomerId();
        }
        String date = utils.getInput("Enter new date of reservation (YYYY-MM-DD):");
        if (date.equals("9")) {
            utils.printMessage("Reservation editing cancelled.");
            return;
        } else if(date.isEmpty()){
            date = reservation.getDate();
        } else if (date.equalsIgnoreCase("today")) {
            date = LocalDate.now().toString();
        }
        reservation.setBoatId(boatId);
        reservation.setCustomerId(customerId);
        reservation.setDate(date);
        try {
            updateReservationFile("reservations.csv", reservations);
            utils.printMessage("Reservation edited successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing reservation to file: " + e.getMessage());
        }
    }
    // find reservation
    public Reservation findReservation(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId)) {
                return res;
            }
        }
        return null;
    }
    public boolean listReservationByID() {
        boolean keepSearching = true;
        while (keepSearching) {
            String reservationId = utils.getInput("Enter reservation ID (or type 'exit' to return):");

            // Allow the user to exit the search
            if ("exit".equalsIgnoreCase(reservationId)) {
                utils.printMessage("Exiting search...");
                return false;
            }

            try {
                if (reservationId == null || reservationId.trim().isEmpty()) {
                    utils.printMessage("Invalid reservation ID. Please try again.");
                    continue; // Skip the rest of the loop and ask for input again
                }

                Reservation reservation = findReservation(reservationId);
                if (reservation == null) {
                    utils.printMessage("Reservation not found. Please try again.");
                } else {
                    utils.printMessage("Reservation found with ID: " + reservationId);
                    utils.printMessage("+-------------------------------------------+");
                    utils.printMessage("Reservation ID: " + reservation.getReservationId() + ", Boat ID: " + reservation.getBoatId() + ", Customer ID: " + reservation.getCustomerId() + ", Date: " + reservation.getDate());
                    utils.printMessage("+-------------------------------------------+");
                    keepSearching = false; // Exit loop after successful search
                    return true;
                }
            } catch (Exception e) {
                utils.printMessage("Error searching for reservation: " + e.getMessage());
                return false;
            }
        }
        return false; // In case the loop exits some other way
    }

    public boolean searchReservationByBoatId() {
        boolean keepSearching = true;
        do {
            String boatId = utils.getInput("Enter boat ID (or type 'exit' to return):");
            if ("exit".equalsIgnoreCase(boatId)) {
                utils.printMessage("Exiting search...");
                return false;
            }
            if (boatId == null || boatId.trim().isEmpty()) {
                utils.printMessage("Invalid boat ID. Please try again.");
                continue;
            }

            List<Reservation> foundReservations = findReservationsByBoatId(boatId);
            if (foundReservations.isEmpty()) {
                utils.printMessage("No reservations found with Boat ID: " + boatId + ". Please try again.");
            } else {
                foundReservations.forEach(reservation ->
                        utils.printMessage("Reservation ID: " + reservation.getReservationId() + ", Boat ID: " + reservation.getBoatId() + ", Customer ID: " + reservation.getCustomerId() + ", Date: " + reservation.getDate())
                );
                keepSearching = false;
            }
        } while (keepSearching);
        return true;
    }

    public List<Reservation> findReservationsByBoatId(String boatId) {
        List<Reservation> foundReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getBoatId().equals(boatId)) {
                foundReservations.add(res);
            }
        }
        return foundReservations;
    }
    public Reservation findReservationByBoatId(String boatId) {
        for (Reservation res : reservations) {
            if (res.getBoatId().equals(boatId)) {
                return res;
            }
        }
        return null;
    }
    // find reservation by customer id
    public boolean searchReservationByCustomerId() {
        boolean keepSearching = true;
        do {
            String customerId = utils.getInput("Enter customer ID (or type 'exit' to return):");
            if ("exit".equalsIgnoreCase(customerId)) {
                utils.printMessage("Exiting search...");
                return false;
            }
            if (customerId == null || customerId.trim().isEmpty()) {
                utils.printMessage("Invalid customer ID. Please try again.");
                continue;
            }

            List<Reservation> foundReservations = findReservationsByCustomerId(customerId);
            if (foundReservations.isEmpty()) {
                utils.printMessage("No reservations found for Customer ID: " + customerId + ". Please try again.");
            } else {
                foundReservations.forEach(reservation ->
                        utils.printMessage("Reservation ID: " + reservation.getReservationId() + ", Boat ID: " + reservation.getBoatId() + ", Customer ID: " + reservation.getCustomerId() + ", Date: " + reservation.getDate())
                );
                keepSearching = false;
            }
        } while (keepSearching);
        return true;
    }
    public List<Reservation> findReservationsByCustomerId(String customerId) {
        List<Reservation> foundReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomerId().equals(customerId)) {
                foundReservations.add(res);
            }
        }
        return foundReservations;
    }
    // delete reservation
    public void deleteReservation() {
        listReservations();
        utils.printMessage("Enter '9' to cancel reservation deletion at any step or to exit.");
        String reservationId = utils.getInput("Enter reservation ID:");
        if (reservationId.equals("9")) {
            utils.printMessage("Reservation deletion cancelled.");
            return;
        }
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) {
            utils.printMessage("Reservation not found.");
            return;
        }
        reservations.removeReservation(reservation);
        try {
            writeReservations("reservations.csv");
            utils.printMessage("Reservation deleted successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing reservations to file: " + e.getMessage());
        }
    }
    // delete all reservations
    public void deleteAllReservations() {
        reservations.removeAllReservations();
        try {
            writeReservations("reservations.csv");
            utils.printMessage("All reservations deleted successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing reservations to file: " + e.getMessage());
        }
    }
    public boolean listReservationByDate() {
        boolean keepSearching = true;
        do {
            String date = utils.getInput("Enter date (YYYY-MM-DD) (or type 'exit' to return):");
            if ("exit".equalsIgnoreCase(date)) {
                utils.printMessage("Exiting search...");
                return false;
            }
            if (date == null || date.trim().isEmpty()) {
                utils.printMessage("Invalid date format. Please try again.");
                continue;
            }

            List<Reservation> foundReservations = findReservationsByDate(date);
            if (foundReservations.isEmpty()) {
                utils.printMessage("No reservations found for date: " + date + ". Please try again.");
            } else {
                foundReservations.forEach(reservation ->
                        utils.printMessage("Reservation ID: " + reservation.getReservationId() + ", Boat ID: " + reservation.getBoatId() + ", Customer ID: " + reservation.getCustomerId() + ", Date: " + reservation.getDate())
                );
                keepSearching = false;
            }
        } while (keepSearching);
        return true;
    }
    public List<Reservation> findReservationsByDate(String date) {
        List<Reservation> foundReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getDate().equals(date)) {
                foundReservations.add(res);
            }
        }
        return foundReservations;
    }
    public void listReservations() {
        if (reservations.isEmpty()) {
            utils.printMessage("No reservations found.");
            return;
        }
        for (Reservation res : reservations) {
            utils.printMessage("Reservation ID: " + res.getReservationId() + ", Boat ID: " + res.getBoatId() + ", Customer ID: " + res.getCustomerId() + ", Date: " + res.getDate());
        }
    }
    public void writeReservation(String filePath, Reservation reservation) throws IOException {
        try (FileWriter fileWriter = new FileWriter("database/"+filePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(String.format("%s,%s,%s,%s",
                    reservation.getReservationId(), reservation.getBoatId(),
                    reservation.getCustomerId(), reservation.getDate()));
        }
    }
    public void writeReservations(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter("database/"+filePath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            for (Reservation reservation : reservations) {
                printWriter.println(String.format("%s,%s,%s,%s",
                        reservation.getReservationId(), reservation.getBoatId(),
                        reservation.getCustomerId(), reservation.getDate()));
            }
        }
    }
    public void readReservations(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("database/"+filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Reservation reservation = new Reservation(parts[0], parts[1], parts[2], parts[3]);
                    reservations.addReservation(reservation);
                }
            }
        }
    }
    public void updateReservationFile(String filePath, Reservations reservations) throws IOException {
        try (FileWriter fileWriter = new FileWriter("database/"+filePath); // False to overwrite the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            for (Reservation reservation : reservations) {
                printWriter.println(String.format("%s,%s,%s,%s",
                        reservation.getReservationId(),
                        reservation.getBoatId(),
                        reservation.getCustomerId(),
                        reservation.getDate()));
            }
        }
    }

}
