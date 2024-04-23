package com.boat.view;

import com.boat.controller.UserController;
import com.boat.services.ProgramUtils;

public class UserScreen {
    // TODO Implement the UserScreen class
    private UserController controller;
    ProgramUtils utils = ProgramUtils.getInstance();

    public UserScreen(UserController controller) {
        this.controller = controller;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1 - Create a new user");
            System.out.println("2 - List all users");
            System.out.println("3 - Edit a user");
            System.out.println("4 - Delete a user");
            System.out.println("5 - Search for a user");
            System.out.println("6 - Delete all users");
            System.out.println("9 - Go back to the main menu");

            try {
                int choice = Integer.parseInt(utils.getInput("Enter your choice:"));
                switch (choice) {
                    case 1:
                        controller.createUser();
                        break;
                    case 2:
                        controller.listUsers();
                        break;
                    case 3:
                        controller.editUser();
                        break;
                    case 4:
                        controller.deleteUser();
                        break;
                    case 5:
                        displaySearchMenu();
                        break;
                    case 6:
//                        controller.deleteAllUsers();
                        break;
                    case 9:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displaySearchMenu() {
        System.out.println("\nSearch by:");
        System.out.println("1 - Username");
        System.out.println("2 - First name");
        System.out.println("3 - Last name");
        System.out.println("4 - Email");
        System.out.println("5 - Role");
        System.out.println("6 - Go back to the previous menu");

        try {
            int choice = Integer.parseInt(utils.getInput("Enter your choice:"));
            switch (choice) {
                case 1:
//                    controller.searchByUsername();
                    break;
                case 2:
//                    controller.searchByFirstName();
                    break;
                case 3:
//                    controller.searchByLastName();
                    break;
                case 4:
//                    controller.searchByEmail();
                    break;
                case 5:
//                    controller.searchByRole();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
