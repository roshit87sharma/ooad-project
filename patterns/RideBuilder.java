// CREATIONAL DESIGN PATTERN #3: BUILDER PATTERN
// RideBuilder constructs complex Ride objects step by step

import java.time.LocalDateTime;

public class RideBuilder {
    private String rideId;
    private Customer customer;
    private Driver driver;
    private String pickupLocation;
    private String dropLocation;
    private Ride.RideStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;
    private double fare;

    // Constructor
    public RideBuilder(String rideId, Customer customer) {
        this.rideId = rideId;
        this.customer = customer;
        this.status = Ride.RideStatus.REQUESTED;
        this.startTime = LocalDateTime.now();
    }

    // Builder methods - fluent interface
    public RideBuilder setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
        return this;
    }

    public RideBuilder setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
        return this;
    }

    public RideBuilder setDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public RideBuilder setStatus(Ride.RideStatus status) {
        this.status = status;
        return this;
    }

    public RideBuilder setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public RideBuilder setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public RideBuilder setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public RideBuilder setFare(double fare) {
        this.fare = fare;
        return this;
    }

    // Validation method
    private Ride validateLocation(Ride ride) {
        if (ride.getPickupLocation() == null || ride.getDropLocation() == null) {
            throw new IllegalStateException("Pickup and drop locations are required");
        }
        System.out.println("✓ Location validation passed");
        return ride;
    }

    private Ride validateDriver(Ride ride) {
        if (ride.getDriver() == null) {
            System.out.println("⚠ Warning: No driver assigned yet");
        } else {
            System.out.println("✓ Driver validation passed");
        }
        return ride;
    }

    private Ride validateFare(Ride ride) {
        if (fare > 0) {
            System.out.println("✓ Fare validation passed");
        } else {
            System.out.println("⚠ Warning: Fare not set");
        }
        return ride;
    }

    // Build method - constructs and validates the Ride object
    public Ride build() {
        Ride ride = new Ride(rideId, customer, pickupLocation, dropLocation);
        
        // Apply all builder settings
        if (driver != null) {
            ride.setDriver(driver);
        }
        ride.setStatus(status);
        if (distance > 0) {
            ride.setDistance(distance);
        }
        if (fare > 0) {
            ride.setFare(fare);
        }
        if (endTime != null) {
            ride.setEndTime(endTime);
        }

        // Validate the ride
        System.out.println("\n--- Validating Ride ---");
        validateLocation(ride);
        validateDriver(ride);
        validateFare(ride);
        System.out.println("--- Ride Validation Complete ---\n");

        return ride;
    }

    // Reset builder for new ride
    public void reset() {
        this.rideId = null;
        this.customer = null;
        this.driver = null;
        this.pickupLocation = null;
        this.dropLocation = null;
        this.status = Ride.RideStatus.REQUESTED;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.distance = 0;
        this.fare = 0;
    }

    // Display current state
    public void displayCurrentState() {
        System.out.println("\n--- Current Ride State ---");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Customer: " + (customer != null ? customer.getName() : "Not set"));
        System.out.println("Driver: " + (driver != null ? driver.getName() : "Not assigned"));
        System.out.println("Pickup: " + (pickupLocation != null ? pickupLocation : "Not set"));
        System.out.println("Drop: " + (dropLocation != null ? dropLocation : "Not set"));
        System.out.println("Status: " + status);
        System.out.println("Distance: " + (distance > 0 ? distance + " km" : "Not set"));
        System.out.println("Fare: " + (fare > 0 ? "Rs." + fare : "Not calculated"));
        System.out.println("------------------------\n");
    }
}
