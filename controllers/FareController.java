// Fare Controller - Manages fare calculations

public class FareController {
    private RideBookingSystem rideSystem;

    public FareController(RideBookingSystem rideSystem) {
        this.rideSystem = rideSystem;
    }

    public Fare calculateFare(double distance, double durationMinutes) {
        if (distance <= 0 || durationMinutes <= 0) {
            throw new IllegalArgumentException("Invalid distance or duration");
        }
        return new Fare(distance, durationMinutes);
    }

    public boolean updateRideFare(String rideId, double distance, double durationMinutes) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null) {
            return false;
        }
        
        Fare fare = calculateFare(distance, durationMinutes);
        ride.setFare(fare.getTotalFare());
        return true;
    }

    public double getFareEstimate(double distance) {
        // Rough estimate based on distance only
        return 50.0 + (distance * 15.0); // Base fare + distance cost
    }

    public Fare getRideFareDetails(String rideId) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null) {
            return null;
        }
        // Reconstruct fare from ride details
        double durationMinutes = 10; // Default, should be calculated from ride times
        return new Fare(ride.getDistance(), durationMinutes);
    }

    public void displayFareBreakdown(String rideId) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride != null) {
            double durationMinutes = 10;
            Fare fare = new Fare(ride.getDistance(), durationMinutes);
            System.out.println(fare.toString());
        }
    }
}
