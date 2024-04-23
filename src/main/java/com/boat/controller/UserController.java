package com.boat.controller;
import com.boat.model.Model;
import com.boat.model.User;
import com.boat.model.Users;
import com.boat.services.ProgramUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class UserController {
    // TODO Implement the UserController class
//    private Users users;
    Model model = Model.getInstance();

    private Users users = model.getUsers();
    ProgramUtils utils = ProgramUtils.getInstance();

    public UserController() {
        try {
            readUsers("users.csv");
        } catch (IOException e) {
            utils.printMessage("Error reading data from file: " + e.getMessage());
        }
        if (users.isEmpty()) {
            createDefaultUser();
        }
    }
    // create a default user
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decodeBase64(String data) {
        return Base64.getDecoder().decode(data);
    }
    public void createUser() {
        String name = utils.getInput("Enter user name:");
        String email = utils.getInput("Enter user email:");
        String password = utils.getInput("Enter user password:");
        String role = utils.getInput("Enter user role: (admin, user)");
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        String saltEncoded = encodeBase64(salt);
        String id = users.increaseUserId();
        User user = new User(id, name, email, saltEncoded + ":" + hashedPassword, role);
        users.addUser(user);
        try {
            writeUser("users.csv", user);
            utils.printMessage("User added successfully and saved to file.");
        } catch (IOException e) {
            utils.printMessage("Error writing user to file: " + e.getMessage());
        }
    }
    // delete a user
    public void deleteUser() {
        String id = utils.getInput("Enter user ID:");
        User user = users.getUserById(id);
        if (user != null) {
            users.removeUser(user);
            try {
                writeUsers("users.csv", users);
                utils.printMessage("User deleted successfully.");
            } catch (IOException e) {
                utils.printMessage("Error writing users to file: " + e.getMessage());
            }
        } else {
            utils.printMessage("User not found.");
        }
    }
    // edit a user
    public void editUser() {
        utils.printMessage("Editing a user...");
        utils.printMessage("Enter '9' to cancel.");
        String id = utils.getInput("Enter user ID:");
        User user = users.getUserById(id);
        if (user != null) {
            String name = utils.getInput("Enter user name:");
            if (name.equals("9")) {
                return;
            } else if (name.isEmpty()) {
                name = user.getName();
            }
            String email = utils.getInput("Enter user email:");
            if (email.equals("9")) {
                return;
            } else if (email.isEmpty()) {
                email = user.getEmail();
            }
            String password = utils.getInput("Enter user password:");
            if (password.equals("9")) {
                return;
            } else if (password.isEmpty()) {
                password = user.getPassword();
            }
            String role = utils.getInput("Enter user role: (admin, user)");
            if (role.equals("9")) {
                return;
            } else if (role.isEmpty()) {
                role = user.getRole();
            }
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);
            String saltEncoded = encodeBase64(salt);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(saltEncoded + ":" + hashedPassword);
            user.setRole(role);
            try {
                writeUsers("users.csv", users);
                utils.printMessage("User updated successfully.");
            } catch (IOException e) {
                utils.printMessage("Error writing users to file: " + e.getMessage());
            }
        } else {
            utils.printMessage("User not found.");
        }
    }
    public void createDefaultUser() {
        String defaultPassword = "password123";
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(defaultPassword, salt);
        String saltEncoded = encodeBase64(salt);
        User user = new User("1", "Bashar", "bashar@capgemini.com", saltEncoded + ":" + hashedPassword, "admin");
        users.addUser(user);
        try {
            writeUser("users.csv", user);
        } catch (IOException e) {
            utils.printMessage("Error writing user to file: " + e.getMessage());
        }
    }
    public void readUsers(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            // Assume data[3] is salt:hash
            User user = new User(data[0], data[1], data[2], data[3], data[4]);
            users.addUser(user);
        }
        reader.close();
    }

    public void writeUser(String filename, User user) throws IOException {
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.println(user.getId() + "," + user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getRole());
        writer.close();
    }
    // write users
    public void writeUsers(String filename, Users users) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter writer = new PrintWriter(fileWriter);
        for (User user : users) {
            writer.println(user.getId() + "," + user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getRole());
        }
        writer.close();
    }
    public void listUsers() {
        for (User user : users) {
            utils.printMessage("User ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail() + ", Role: " + user.getRole());
        }
    }

}
