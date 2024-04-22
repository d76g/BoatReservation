package com.boat.controller;
import java.io.*;

import com.boat.model.User;
import com.boat.model.Users;

import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Authentication {
    private Users users;
    public Authentication() {
        this.users = new Users();
        try {
            readUsers("users.csv");
        } catch (IOException e) {
            System.out.println("Error reading data from file: " + e.getMessage());
        }

    }
    // hash password


    public static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * hashedBytes.length);
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();  // Return hexadecimal string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    // Method to authenticate user
    private byte[] decodeBase64(String data) {
        return Base64.getDecoder().decode(data);
    }
    public User authenticateUser(String email, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[2].equals(email)) {
                    String[] passwordParts = parts[3].split(":");
                    if (passwordParts.length == 2) {
                        String salt = passwordParts[0];
                        String storedHash = passwordParts[1];
                        String computedHash = hashPassword(password, decodeBase64(salt));
                        if (computedHash.equals(storedHash)) {
                            return new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        }
                    }
                }
            }
        }
        return null;
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
}