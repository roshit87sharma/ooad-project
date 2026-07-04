package com.ridebooking.service.impl;

import com.ridebooking.exception.RideNotFoundException;
import com.ridebooking.model.Driver;
import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.DriverStatus;
import com.ridebooking.model.enums.RideStatus;
import com.ridebooking.pattern.state.RideState;
import com.ridebooking.pattern.state.RideStateFactory;
import com.ridebooking.repository.RideRepository;
import com.ridebooking.service.DriverService;
import com.ridebooking.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RideServiceImpl — SRP: Handles ride lifecycle transitions.
 * Integrates STATE PATTERN for ride status machine.
 * Implements use cases: acceptRide(), startRide(), completeRide(),
 * cancelRide(), trackRide().
 */
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverService driverService;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, DriverService driverService) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
    }

    /**
     * USE CASE: acceptRide()
     * STATE PATTERN: REQUESTED → DRIVER_ASSIGNED
     */
    @Override
    public Ride acceptRide(Long rideId, Long driverId) {
        Ride ride = getRideById(rideId);
        Driver driver = driverService.getDriverById(driverId);

        // STATE PATTERN — validates transition
        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.accept(ride);

        ride.setDriver(driver);
        driver.acceptRide();
        driverService.updateDriverStatus(driverId, DriverStatus.BUSY);

        return rideRepository.save(ride);
    }

    /**
     * USE CASE: startRide()
     * STATE PATTERN: DRIVER_ASSIGNED → IN_PROGRESS
     */
    @Override
    public Ride startRide(Long rideId) {
        Ride ride = getRideById(rideId);

        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.start(ride);

        if (ride.getDriver() != null) {
            ride.getDriver().startRide();
        }
        return rideRepository.save(ride);
    }

    /**
     * USE CASE: completeRide()
     * STATE PATTERN: IN_PROGRESS → COMPLETED
     */
    @Override
    public Ride completeRide(Long rideId) {
        Ride ride = getRideById(rideId);

        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.complete(ride);

        ride.setEndTime(LocalDateTime.now());
        if (ride.getDriver() != null) {
            ride.getDriver().endRide();
            driverService.updateDriverStatus(ride.getDriver().getUserId(), DriverStatus.AVAILABLE);
        }

        return rideRepository.save(ride);
    }

    /**
     * USE CASE: cancelRide()
     * STATE PATTERN: any valid state → CANCELLED
     */
    @Override
    public Ride cancelRide(Long rideId) {
        Ride ride = getRideById(rideId);

        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.cancel(ride);

        if (ride.getDriver() != null) {
            ride.getDriver().endRide();
            driverService.updateDriverStatus(ride.getDriver().getUserId(), DriverStatus.AVAILABLE);
        }

        return rideRepository.save(ride);
    }

    /**
     * USE CASE: trackRide() — returns current ride state and details
     */
    @Override
    public Ride trackRide(Long rideId) {
        return getRideById(rideId);
    }

    @Override
    public RideStatus getRideStatus(Long rideId) {
        return getRideById(rideId).getRideStatus();
    }

    @Override
    public List<Ride> getRidesByDriver(Long driverId) {
        return rideRepository.findByDriverUserId(driverId);
    }

    private Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found: " + rideId));
    }
}
