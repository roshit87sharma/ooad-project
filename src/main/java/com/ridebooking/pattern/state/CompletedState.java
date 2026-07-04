package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;

/** State: COMPLETED — terminal state, no further transitions allowed */
public class CompletedState implements RideState {

    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride is already completed");
    }

    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Ride is already completed");
    }

    @Override
    public void complete(Ride ride) {
        throw new IllegalStateException("Ride is already completed");
    }

    @Override
    public void cancel(Ride ride) {
        throw new IllegalStateException("Cannot cancel a completed ride");
    }

    @Override
    public String getStateName() { return "COMPLETED"; }
}
