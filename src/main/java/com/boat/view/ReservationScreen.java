package com.boat.view;

import com.boat.controller.Authentication;
import com.boat.controller.ReservationController;
import com.boat.model.User;
import com.boat.services.ProgramUtils;

import java.io.IOException;
import java.util.Scanner;

public class ReservationScreen {
    // TODO Implement the ReservationScreen class
    private ReservationController controller;

    private ProgramUtils utils;
    User user = null;
    public ReservationScreen(ReservationController controller, User user, ProgramUtils utils) {
        this.utils = utils;
        this.controller = controller;
        this.user = user;
    }
    public void displayMenu() {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1 - Create a new reservation");
            System.out.println("2 - List all reservations");
            System.out.println("3 - Edit a reservation");
            System.out.println("4 - Delete a reservation");
            System.out.println("5 - Search for a reservation");
            if (user.getRole().equals("admin")) {
                System.out.println("6 - Delete all reservations");
            }
            System.out.println("9 - Go back to the main menu");

            try {
                int choice = Integer.parseInt(utils.getInput("Enter your choice:"));
                switch (choice) {
                    case 1:
                        controller.createReservation();
                        break;
                    case 2:
                        controller.listReservations();
                        break;
                    case 3:
                        controller.editReservation();  // Assuming implementation
                        break;
                    case 4:
                        controller.deleteReservation();  // Assuming implementation
                        break;
                    case 5:
                        displaySearchMenu();
                        break;
                    case 6:
                        if (user.getRole().equals("admin")) {
                            controller.deleteAllReservations();
                        } else {
                            System.out.println("Unauthorized access.");
                        }
                        break;
                    case 9:
                        System.out.println("Returning to the main menu...");
                        return; // Exiting this menu
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
    }

    // Placeholder methods for CRUD operations
    private void editReservation() {
        System.out.println("Editing a reservation...");
        // Further implementation needed
    }

    private void deleteReservation() {
        System.out.println("Deleting a reservation...");
        // Further implementation needed
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void displayReservation(String reservation) {
        System.out.println(reservation);
    }

    public void displaySearchMenu() {
        boolean running = true;
        do {
            System.out.println("Choose an option:");
            System.out.println("1 - Search by reservation ID");
            System.out.println("2 - Search by customer ID");
            System.out.println("3 - Search by boat ID");
            System.out.println("4 - Search by date");
            System.out.println("9 - Go back to the main menu");

            // get user input
            try {
                int choice = Integer.parseInt(utils.getInput("Enter your choice:"));
                // stay in the search menu until user chooses to go back

                switch (choice) {
                    case 1:
                        if(!controller.listReservationByID()) {
                            continue;
                        }
                        break;
                    case 2:

                        controller.searchReservationByCustomerId();
                        break;
                    case 3:
                        if (!controller.searchReservationByBoatId()) {
                            continue;
                        }
                        break;
                    case 4:
                        controller.listReservationByDate();
                        break;
                    case 9:
                        System.out.println("Returning to the main menu...");
                        running = false;
                        return; // Exiting this menu
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        } while (running);


    }

}
