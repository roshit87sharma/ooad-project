package com.ridebooking.model.enums;

/**
 * Ride type — used by Factory Pattern to create appropriate Ride subclass
 * and by Strategy Pattern to select the fare calculation algorithm.
 */
public enum RideType {
    ECONOMY,
    PREMIUM,
    SHARED
}
