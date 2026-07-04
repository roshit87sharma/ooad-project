package com.ridebooking.pattern.state;

import com.ridebooking.model.Ride;

/**
 * ═══════════════════════════════════════════════════════════
 *  BEHAVIORAL DESIGN PATTERN: STATE (Advanced Feature)
 * ═══════════════════════════════════════════════════════════
 * Defines the interface for ride state transitions.
 * Each concrete state knows which transitions are valid from it.
 *
 * State machine:
 *   REQUESTED → DRIVER_ASSIGNED → IN_PROGRESS → COMPLETED
 *                     ↓                ↓
 *                 CANCELLED        CANCELLED
 */
public interface RideState {

    /** Driver accepts the ride request */
    void accept(Ride ride);

    /** Driver starts the ride */
    void start(Ride ride);

    /** Driver completes the ride */
    void complete(Ride ride);

    /** Rider or driver cancels the ride */
    void cancel(Ride ride);

    /** Returns the state name for logging */
    String getStateName();
}
