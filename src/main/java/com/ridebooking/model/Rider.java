package com.ridebooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rider — matches UML class diagram.
 * Extends User (inheritance relationship).
 * Attributes: riderRating.
 * Methods: requestRide(), chooseRideOption().
 * Association: Rider → Booking (1 to many).
 */
@Entity
@Table(name = "riders")
public class Rider extends User {

    private float riderRating = 5.0f;

    // UML: Rider → Booking (1 to many)
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Prevents circular serialization
    private List<Booking> bookings = new ArrayList<>();

    public Rider() {}

    public Rider(String name, String email, String phone) {
        super(name, email, phone);
    }

    // UML diagram methods
    public void requestRide() {
        // Delegates to BookingService at the service layer
    }

    public void chooseRideOption() {
        // Delegates to FareEstimator at the service layer
    }

    @Override
    public void login() { /* Handled by authentication service */ }

    @Override
    public void logout() { /* Handled by authentication service */ }

    // Getters and Setters
    public float getRiderRating() { return riderRating; }
    public void setRiderRating(float riderRating) { this.riderRating = riderRating; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
