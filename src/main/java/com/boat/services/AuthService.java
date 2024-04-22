package com.boat.services;

import com.boat.controller.Authentication;
import com.boat.controller.SessionController;
import com.boat.view.ConsoleView;
import com.boat.model.User;
import java.io.IOException;

public class AuthService {
    private Authentication auth;
    private ProgramUtils utils;

    public AuthService(Authentication auth, ProgramUtils utils) {
        this.auth = auth;
        this.utils = utils;
    }
    public User authenticate() {
        User user = null;
        while (user == null) {
            String email = utils.getInput("Enter your email:");
            String password = utils.getInput("Enter your password:");
            try {
                user = auth.authenticateUser(email, password);
                if (user == null) {
                    utils.printMessage("Invalid credentials, please try again.");
                } else {
                    SessionController.login(user);
                    utils.printMessage("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    utils.printMessage("Login successful. Welcome, " + user.getName().toUpperCase() + "!");
                    utils.printMessage("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            } catch ( IOException e) {

                utils.printMessage("Failed to access user data: " + e.getMessage());
            }
        }
        return user;
    }
}
