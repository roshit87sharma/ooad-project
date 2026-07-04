package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.RideStatus;

/** State: IN_PROGRESS — ride is underway */
public class InProgressState implements RideState {

    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride is already in progress");
    }

    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Ride is already in progress");
    }

    @Override
    public void complete(Ride ride) {
        ride.setRideStatus(RideStatus.COMPLETED);
        System.out.println("[State] Ride " + ride.getRideId() + ": IN_PROGRESS → COMPLETED");
    }

    @Override
    public void cancel(Ride ride) {
        ride.setRideStatus(RideStatus.CANCELLED);
        System.out.println("[State] Ride " + ride.getRideId() + ": IN_PROGRESS → CANCELLED");
    }

    @Override
    public String getStateName() { return "IN_PROGRESS"; }
}
