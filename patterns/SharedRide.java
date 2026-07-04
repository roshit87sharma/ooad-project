// Shared Ride Type

public class SharedRide extends RideType {
    public SharedRide(String rideId, Customer customer, String pickup, String drop) {
        super(rideId, customer, pickup, drop);
        this.baseFare = 30.0;
        this.capacity = 6;
    }

    @Override
    public void displayDetails() {
        System.out.println("=== Shared Ride ===");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Type: Shared");
        System.out.println("Capacity: " + capacity + " passengers");
        System.out.println("Base Fare: Rs." + baseFare);
    }

    @Override
    public double calculateFare(double distance) {
        return baseFare + (distance * 8.0);
    }
}
