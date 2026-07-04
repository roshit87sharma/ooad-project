package com.ridebooking.pattern.state;

import com.ridebooking.model.enums.RideStatus;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps RideStatus enum (JPA-persistent) to RideState objects (behavioral).
 * Bridges persistence layer with the State Pattern.
 */
public class RideStateFactory {

    private static final Map<RideStatus, RideState> STATE_MAP = new HashMap<>();

    static {
        STATE_MAP.put(RideStatus.REQUESTED,        new RequestedState());
        STATE_MAP.put(RideStatus.DRIVER_ASSIGNED,   new DriverAssignedState());
        STATE_MAP.put(RideStatus.IN_PROGRESS,       new InProgressState());
        STATE_MAP.put(RideStatus.COMPLETED,         new CompletedState());
        STATE_MAP.put(RideStatus.CANCELLED,         new CancelledState());
    }

    public static RideState getState(RideStatus status) {
        RideState state = STATE_MAP.get(status);
        if (state == null) {
            throw new IllegalArgumentException("Unknown ride status: " + status);
        }
        return state;
    }
}
