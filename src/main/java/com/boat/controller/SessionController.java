package com.boat.controller;
import java.io.*;
import java.nio.file.*;
import com.boat.model.User;
public class SessionController {
    private static final String SESSION_FILE = "database/session.txt"; // Use a binary file for security

        public static void login(User user) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
                out.writeObject(user);
            } catch (IOException e) {
                System.err.println("Failed to write session file: " + e.getMessage());
            }
        }

        public static User loadSession() {
            if (!new File(SESSION_FILE).exists()) return null;

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SESSION_FILE))) {
                return (User) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to read session file: " + e.getMessage());
                return null;
            }
        }

        public static void logout() {
            try {
                Files.deleteIfExists(Paths.get(SESSION_FILE));
            } catch (IOException e) {
                System.err.println("Error deleting session data from file: " + e.getMessage());
            }
        }
}
