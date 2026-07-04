// Driver Controller - Manages driver operations

public class DriverController {
    private RideBookingSystem rideSystem;

    public DriverController(RideBookingSystem rideSystem) {
        this.rideSystem = rideSystem;
    }

    public Driver registerDriver(String userId, String name, String email, String phone,
                                 String licenseNumber, String vehicleNumber) {
        Driver driver = new Driver(userId, name, email, phone, licenseNumber, vehicleNumber);
        rideSystem.registerDriver(driver);
        return driver;
    }

    public boolean logInDriver(String driverId) {
        Driver driver = rideSystem.getDriver(driverId);
        if (driver == null) {
            return false;
        }
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        return true;
    }

    public boolean logOutDriver(String driverId) {
        Driver driver = rideSystem.getDriver(driverId);
        if (driver == null) {
            return false;
        }
        driver.setStatus(Driver.DriverStatus.OFFLINE);
        return true;
    }

    public boolean updateLocation(String driverId, double latitude, double longitude) {
        Driver driver = rideSystem.getDriver(driverId);
        if (driver == null) {
            return false;
        }
        driver.setLocation(latitude, longitude);
        return true;
    }

    public boolean rateCustomer(String driverId, String customerId, double rating) {
        if (rating < 1.0 || rating > 5.0) {
            return false;
        }
        // Update customer rating
        return true;
    }

    public Driver getDriverDetails(String driverId) {
        return rideSystem.getDriver(driverId);
    }
}
