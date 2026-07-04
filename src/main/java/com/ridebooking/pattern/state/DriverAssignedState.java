package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.RideStatus;

/** State: DRIVER_ASSIGNED — driver accepted, waiting to start */
public class DriverAssignedState implements RideState {

    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride already has a driver assigned");
    }

    @Override
    public void start(Ride ride) {
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        System.out.println("[State] Ride " + ride.getRideId() + ": DRIVER_ASSIGNED → IN_PROGRESS");
    }

    @Override
    public void complete(Ride ride) {
        throw new IllegalStateException("Cannot complete ride — ride has not started yet");
    }

    @Override
    public void cancel(Ride ride) {
        ride.setRideStatus(RideStatus.CANCELLED);
        System.out.println("[State] Ride " + ride.getRideId() + ": DRIVER_ASSIGNED → CANCELLED");
    }

    @Override
    public String getStateName() { return "DRIVER_ASSIGNED"; }
}
