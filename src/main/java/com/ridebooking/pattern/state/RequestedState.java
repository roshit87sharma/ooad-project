package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.RideStatus;

/** State: REQUESTED — ride is waiting for a driver to accept */
public class RequestedState implements RideState {

    @Override
    public void accept(Ride ride) {
        ride.setRideStatus(RideStatus.DRIVER_ASSIGNED);
        System.out.println("[State] Ride " + ride.getRideId() + ": REQUESTED → DRIVER_ASSIGNED");
    }

    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Cannot start ride — no driver assigned yet");
    }

    @Override
    public void complete(Ride ride) {
        throw new IllegalStateException("Cannot complete ride — ride has not started");
    }

    @Override
    public void cancel(Ride ride) {
        ride.setRideStatus(RideStatus.CANCELLED);
        System.out.println("[State] Ride " + ride.getRideId() + ": REQUESTED → CANCELLED");
    }

    @Override
    public String getStateName() { return "REQUESTED"; }
}
