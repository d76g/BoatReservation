package com.boat.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reservations implements Iterable<Reservation> {
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
    public void removeAllReservations() {
        reservations.clear();
    }
    @Override
    public Iterator<Reservation> iterator() {
        return reservations.iterator();
    }
    public int getReservationCount() {
        return reservations.size();
    }

    // is empty
    public boolean isEmpty() {
        return reservations.isEmpty();
    }
    public Reservation findReservationById(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }
}
