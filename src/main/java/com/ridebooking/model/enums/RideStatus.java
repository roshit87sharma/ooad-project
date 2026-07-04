package com.ridebooking.model.enums;

/**
 * Ride lifecycle states — used by the State Pattern.
 * Valid transitions enforced by RideState implementations.
 */
public enum RideStatus {
    REQUESTED,
    DRIVER_ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
