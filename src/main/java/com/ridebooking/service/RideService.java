package com.ridebooking.service;

import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.RideStatus;
import java.util.List;

/**
 * RideService — DIP: Controllers depend on this interface.
 * Implements core use cases: acceptRide(), startRide(), completeRide(),
 * cancelRide(), trackRide().
 * Uses State Pattern internally for ride transitions.
 */
public interface RideService {
    Ride acceptRide(Long rideId, Long driverId);
    Ride startRide(Long rideId);
    Ride completeRide(Long rideId);
    Ride cancelRide(Long rideId);
    Ride trackRide(Long rideId);
    RideStatus getRideStatus(Long rideId);
    List<Ride> getRidesByDriver(Long driverId);
}
