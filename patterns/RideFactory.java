// CREATIONAL DESIGN PATTERN #2: FACTORY PATTERN
// RideFactory creates different types of rides

public class RideFactory {
    public enum RideTypeEnum {
        ECONOMY, PREMIUM, SHARED
    }

    // Factory method to create rides
    public static RideType createRide(RideTypeEnum type, String rideId, 
                                      Customer customer, String pickup, String drop) {
        switch (type) {
            case ECONOMY:
                return new EconomyRide(rideId, customer, pickup, drop);
            case PREMIUM:
                return new PremiumRide(rideId, customer, pickup, drop);
            case SHARED:
                return new SharedRide(rideId, customer, pickup, drop);
            default:
                throw new IllegalArgumentException("Unknown ride type: " + type);
        }
    }
}
