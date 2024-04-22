package com.boat;

import com.boat.controller.UserController;
import com.boat.services.AuthService;
import com.boat.controller.ReservationController;
import com.boat.controller.SessionController;
import com.boat.controller.Authentication;
import com.boat.services.ProgramUtils;
import com.boat.view.ConsoleView;
import com.boat.model.User;
import com.boat.view.ReservationScreen;
import com.boat.view.UserScreen;

import java.io.IOException;

// TIP To Run code, press Run or click the execute icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        ProgramUtils utils = ProgramUtils.getInstance();
        ConsoleView view = new ConsoleView();
        User user = SessionController.loadSession();
        Authentication auth = new Authentication();
        AuthService authService = new AuthService(auth, utils);
        if (user == null) {
            user = authService.authenticate();
        }
        ReservationController controller = new ReservationController(user);
        ReservationScreen screen = new ReservationScreen(controller, user, utils);
        UserController userController = new UserController(utils);
        UserScreen userScreen = new UserScreen(userController, utils);
        MENU_LOOP:
        do {
            view.printMessage("\nChoose an option:");
            view.printMessage("1 - Reservations");
            if (user.getRole().equals("admin")) {
                view.printMessage("2 - Boats");
                view.printMessage("3 - Users");
            }
            if (user.getRole().equals("user")){
                view.printMessage("2 - List all boats");
            }
            view.printMessage("9 - Exit the program");

            int choice = Integer.parseInt(utils.getInput("Enter your choice:"));

            switch (choice) {
                case 1: screen.displayMenu(); break;
                case 2:
                    if (user.getRole().equals("admin")) {
                        view.printMessage("Boats");
                    } else {
                        view.printMessage("List all boats.");
                    }
                    break;
                case 3:
                    if (user.getRole().equals("admin")) {
                        userScreen.displayMenu();
                    } else {
                        view.printMessage("Unauthorized access.");
                    }
                    break;
                case 9:
                    SessionController.logout();
                    break MENU_LOOP;
                default: view.printMessage("Invalid option. Please try again.");
            }
        } while (true);

        utils.closeScanner();
    }
}
/*
*
* case 3:
                    if (user.getRole().equals("admin")) {
                        controller.addBoat();
                    } else {
                        view.printMessage("Unauthorized access.");
                    }
                    break;
                case 4: controller.readBoats("boats.csv"); break;
* */