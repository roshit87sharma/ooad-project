package com.ridebooking.service;

import com.ridebooking.model.Driver;
import com.ridebooking.model.enums.DriverStatus;
import java.util.List;

/**
 * DriverService — DIP: Controllers depend on this interface, not the implementation.
 */
public interface DriverService {
    Driver registerDriver(Driver driver);
    Driver getDriverById(Long driverId);
    List<Driver> getAvailableDrivers();
    void updateDriverStatus(Long driverId, DriverStatus status);
    Driver findNearestAvailableDriver();
}
