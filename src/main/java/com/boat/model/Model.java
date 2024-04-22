package com.boat.model;

public class Model {
    // create a singelton instance of the model
    private static Users users = new Users();
    private static Reservations reservations = new Reservations();
    private static Model instance = null;

    private Model() {
        // Exists only to defeat instantiation.
    }
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
}
