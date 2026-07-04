// Economy Ride Type

public class EconomyRide extends RideType {
    public EconomyRide(String rideId, Customer customer, String pickup, String drop) {
        super(rideId, customer, pickup, drop);
        this.baseFare = 50.0;
        this.capacity = 4;
    }

    @Override
    public void displayDetails() {
        System.out.println("=== Economy Ride ===");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Type: Economy");
        System.out.println("Capacity: " + capacity + " passengers");
        System.out.println("Base Fare: Rs." + baseFare);
    }

    @Override
    public double calculateFare(double distance) {
        return baseFare + (distance * 12.0);
    }
}
