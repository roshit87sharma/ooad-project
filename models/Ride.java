// Ride Model

import java.time.LocalDateTime;

public class Ride {
    public enum RideStatus {
        REQUESTED, SEARCHING_DRIVER, DRIVER_ASSIGNED, DRIVER_EN_ROUTE, 
        IN_PROGRESS, COMPLETED, CANCELLED
    }

    private String rideId;
    private Customer customer;
    private Driver driver;
    private String pickupLocation;
    private String dropLocation;
    private RideStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;
    private double fare;

    public Ride(String rideId, Customer customer, String pickupLocation, String dropLocation) {
        this.rideId = rideId;
        this.customer = customer;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = RideStatus.REQUESTED;
        this.startTime = LocalDateTime.now();
    }

    public String getRideId() { return rideId; }
    public Customer getCustomer() { return customer; }
    public Driver getDriver() { return driver; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropLocation() { return dropLocation; }
    public RideStatus getStatus() { return status; }
    public double getDistance() { return distance; }
    public double getFare() { return fare; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public void setDriver(Driver driver) { this.driver = driver; }
    public void setStatus(RideStatus status) { this.status = status; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setFare(double fare) { this.fare = fare; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isCompleted() {
        return status == RideStatus.COMPLETED;
    }
}
