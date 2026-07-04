package com.ridebooking.model;

import com.ridebooking.model.enums.RideType;
import jakarta.persistence.*;

/**
 * SharedRide — matches UML class diagram.
 * Extends Ride (inheritance relationship — LSP compliant).
 * Attributes: maxPassengers, currentPassengers.
 * Methods: addPassenger(), splitFare().
 * Uses SINGLE_TABLE inheritance with discriminator value "SHARED".
 */
@Entity
@DiscriminatorValue("SHARED")
public class SharedRide extends Ride {

    private int maxPassengers = 4;
    private int currentPassengers = 1;

    public SharedRide() {}

    public SharedRide(Location pickupLocation, Location dropLocation) {
        super(pickupLocation, dropLocation, RideType.SHARED);
    }

    // UML diagram methods
    public boolean addPassenger() {
        if (currentPassengers < maxPassengers) {
            currentPassengers++;
            return true;
        }
        return false;
    }

    public double splitFare() {
        if (currentPassengers == 0) return getFare();
        return getFare() / currentPassengers;
    }

    // Getters and Setters
    public int getMaxPassengers() { return maxPassengers; }
    public void setMaxPassengers(int maxPassengers) { this.maxPassengers = maxPassengers; }

    public int getCurrentPassengers() { return currentPassengers; }
    public void setCurrentPassengers(int currentPassengers) { this.currentPassengers = currentPassengers; }
}
