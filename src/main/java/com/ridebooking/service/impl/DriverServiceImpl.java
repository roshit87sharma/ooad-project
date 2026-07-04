package com.ridebooking.service.impl;

import com.ridebooking.exception.DriverNotAvailableException;
import com.ridebooking.model.Driver;
import com.ridebooking.model.enums.DriverStatus;
import com.ridebooking.repository.DriverRepository;
import com.ridebooking.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * DriverServiceImpl — SRP: Only handles driver-related business logic.
 * DIP: Implements DriverService interface.
 */
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver registerDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailabilityStatus(DriverStatus.AVAILABLE);
    }

    @Override
    public void updateDriverStatus(Long driverId, DriverStatus status) {
        Driver driver = getDriverById(driverId);
        driver.setAvailabilityStatus(status);
        driverRepository.save(driver);
    }

    @Override
    public Driver findNearestAvailableDriver() {
        List<Driver> available = getAvailableDrivers();
        if (available.isEmpty()) {
            throw new DriverNotAvailableException("No drivers available at this time");
        }
        // Simplified: returns first available driver
        // In production, would calculate proximity using driver location
        return available.get(0);
    }
}
