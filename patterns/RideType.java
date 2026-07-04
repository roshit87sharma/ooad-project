// Abstract Ride Type for Factory Pattern

public abstract class RideType {
    protected String rideId;
    protected Customer customer;
    protected String pickupLocation;
    protected String dropLocation;
    protected double baseFare;
    protected int capacity;

    public RideType(String rideId, Customer customer, String pickup, String drop) {
        this.rideId = rideId;
        this.customer = customer;
        this.pickupLocation = pickup;
        this.dropLocation = drop;
    }

    public abstract void displayDetails();
    public abstract double calculateFare(double distance);

    public String getRideId() { return rideId; }
    public int getCapacity() { return capacity; }
    public double getBaseFare() { return baseFare; }
}
