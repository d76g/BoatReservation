package com.boat.model;

import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private String reservationId;
    private String boatId;
    private String customerId;
    private String date;

    public Reservation(String reservationId, String boatId, String customerId, String date) {
        this.reservationId = reservationId;
        this.boatId = boatId;
        this.customerId = customerId;
        this.date = date;
    }

    // Getters and Setters
    public String getReservationId() {
        return reservationId;
    }

    public String getBoatId() {
        return boatId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public void setBoatId(String boatId) {
        this.boatId = boatId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public void setDate(String date) {
        this.date = date;
    }
}