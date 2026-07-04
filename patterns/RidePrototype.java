// CREATIONAL DESIGN PATTERN #4: PROTOTYPE PATTERN
// RidePrototype creates new ride instances by cloning existing ride prototypes

import java.time.LocalDateTime;

public class RidePrototype implements Cloneable {
    private String rideId;
    private Customer customer;
    private String pickupLocation;
    private String dropLocation;
    private Ride.RideStatus status;
    private double distance;
    private double fare;

    public RidePrototype(String rideId, Customer customer, String pickupLocation, String dropLocation) {
        this.rideId = rideId;
        this.customer = customer;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = Ride.RideStatus.REQUESTED;
        this.distance = 0;
        this.fare = 0;
    }

    // Getters
    public String getRideId() { return rideId; }
    public Customer getCustomer() { return customer; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropLocation() { return dropLocation; }
    public Ride.RideStatus getStatus() { return status; }
    public double getDistance() { return distance; }
    public double getFare() { return fare; }

    // Setters
    public void setRideId(String rideId) { this.rideId = rideId; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    public void setStatus(Ride.RideStatus status) { this.status = status; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setFare(double fare) { this.fare = fare; }

    // Clone method implements prototype pattern
    @Override
    public RidePrototype clone() {
        try {
            RidePrototype cloned = (RidePrototype) super.clone();
            cloned.rideId = this.rideId;
            cloned.customer = this.customer;
            cloned.pickupLocation = this.pickupLocation;
            cloned.dropLocation = this.dropLocation;
            cloned.status = this.status;
            cloned.distance = this.distance;
            cloned.fare = this.fare;
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create a new ride by cloning with updated ID
    public RidePrototype cloneWithNewId(String newRideId) {
        RidePrototype cloned = this.clone();
        cloned.rideId = newRideId;
        cloned.status = Ride.RideStatus.REQUESTED;
        return cloned;
    }

    // Display ride details
    public void displayDetails() {
        System.out.println("\n--- Ride Details ---");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Customer: " + (customer != null ? customer.getName() : "Not set"));
        System.out.println("Pickup: " + pickupLocation);
        System.out.println("Drop: " + dropLocation);
        System.out.println("Status: " + status);
        System.out.println("Distance: " + (distance > 0 ? distance + " km" : "Not set"));
        System.out.println("Fare: " + (fare > 0 ? "Rs." + fare : "Not calculated"));
        System.out.println("-------------------");
    }
}
