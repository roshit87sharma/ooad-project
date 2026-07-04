// Premium Ride Type

public class PremiumRide extends RideType {
    public PremiumRide(String rideId, Customer customer, String pickup, String drop) {
        super(rideId, customer, pickup, drop);
        this.baseFare = 100.0;
        this.capacity = 4;
    }

    @Override
    public void displayDetails() {
        System.out.println("=== Premium Ride ===");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Type: Premium");
        System.out.println("Capacity: " + capacity + " passengers");
        System.out.println("Base Fare: Rs." + baseFare);
    }

    @Override
    public double calculateFare(double distance) {
        return baseFare + (distance * 20.0);
    }
}
