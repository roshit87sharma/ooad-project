// Ride Controller - Manages ride operations

public class RideController {
    private RideBookingSystem rideSystem;

    public RideController(RideBookingSystem rideSystem) {
        this.rideSystem = rideSystem;
    }

    public Ride requestRide(Customer customer, String pickupLocation, String dropLocation) {
        if (customer == null || pickupLocation == null || dropLocation == null) {
            throw new IllegalArgumentException("Invalid ride request parameters");
        }
        return rideSystem.createRide(customer, pickupLocation, dropLocation);
    }

    public boolean assignDriver(String rideId, Driver driver) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null) {
            return false;
        }
        ride.setDriver(driver);
        ride.setStatus(Ride.RideStatus.DRIVER_ASSIGNED);
        driver.setStatus(Driver.DriverStatus.BUSY);
        return true;
    }

    public boolean startRide(String rideId) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null || ride.getDriver() == null) {
            return false;
        }
        ride.setStatus(Ride.RideStatus.IN_PROGRESS);
        ride.getDriver().setStatus(Driver.DriverStatus.ON_TRIP);
        return true;
    }

    public boolean completeRide(String rideId, double distance) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null) {
            return false;
        }
        ride.setDistance(distance);
        ride.setStatus(Ride.RideStatus.COMPLETED);
        ride.getDriver().setStatus(Driver.DriverStatus.AVAILABLE);
        ride.getDriver().incrementCompletedRides();
        return true;
    }

    public Ride getRideDetails(String rideId) {
        return rideSystem.getRide(rideId);
    }
}
