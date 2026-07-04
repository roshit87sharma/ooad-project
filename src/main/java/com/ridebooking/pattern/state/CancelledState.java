package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;

/** State: CANCELLED — terminal state, no further transitions allowed */
public class CancelledState implements RideState {

    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride is cancelled — cannot accept");
    }

    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Ride is cancelled — cannot start");
    }

    @Override
    public void complete(Ride ride) {
        throw new IllegalStateException("Ride is cancelled — cannot complete");
    }

    @Override
    public void cancel(Ride ride) {
        throw new IllegalStateException("Ride is already cancelled");
    }

    @Override
    public String getStateName() { return "CANCELLED"; }
}
