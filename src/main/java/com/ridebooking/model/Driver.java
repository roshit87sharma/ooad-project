package com.ridebooking.model;

import com.ridebooking.model.enums.DriverStatus;
import jakarta.persistence.*;

/**
 * Driver — matches UML class diagram.
 * Extends User (inheritance relationship).
 * Attributes: driverRating, availabilityStatus.
 * Methods: acceptRide(), startRide(), endRide().
 * Association: Driver → Vehicle (1 to 1, "owns").
 */
@Entity
@Table(name = "drivers")
public class Driver extends User {

    private float driverRating = 5.0f;

    @Enumerated(EnumType.STRING)
    private DriverStatus availabilityStatus = DriverStatus.OFFLINE;

    // UML: Driver → Vehicle (1 to 1)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicleId")
    private Vehicle vehicle;

    public Driver() {}

    public Driver(String name, String email, String phone) {
        super(name, email, phone);
    }

    // UML diagram methods
    public void acceptRide() {
        this.availabilityStatus = DriverStatus.BUSY;
    }

    public void startRide() {
        this.availabilityStatus = DriverStatus.ON_TRIP;
    }

    public void endRide() {
        this.availabilityStatus = DriverStatus.AVAILABLE;
    }

    @Override
    public void login() {
        this.availabilityStatus = DriverStatus.AVAILABLE;
    }

    @Override
    public void logout() {
        this.availabilityStatus = DriverStatus.OFFLINE;
    }

    public boolean isAvailable() {
        return availabilityStatus == DriverStatus.AVAILABLE;
    }

    // Getters and Setters
    public float getDriverRating() { return driverRating; }
    public void setDriverRating(float driverRating) { this.driverRating = driverRating; }

    public DriverStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(DriverStatus status) { this.availabilityStatus = status; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
