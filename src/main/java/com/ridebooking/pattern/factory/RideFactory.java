package com.ridebooking.pattern.factory;

import com.ridebooking.model.Location;
import com.ridebooking.model.Ride;
import com.ridebooking.model.SharedRide;
import com.ridebooking.model.enums.RideType;
import org.springframework.stereotype.Component;

/**
 * ═══════════════════════════════════════════════════════════
 *  CREATIONAL DESIGN PATTERN: FACTORY
 * ═══════════════════════════════════════════════════════════
 * Creates Ride objects based on RideType.
 * Encapsulates object creation — clients don't know about SharedRide subclass.
 * LSP: All returned Ride objects can be used interchangeably.
 */
@Component
public class RideFactory {

    /**
     * Factory method — creates the appropriate Ride subclass.
     * @param type    type of ride (ECONOMY, PREMIUM, SHARED)
     * @param pickup  pickup location
     * @param drop    drop location
     * @return Ride instance (or SharedRide for SHARED type)
     */
    public Ride createRide(RideType type, Location pickup, Location drop) {
        switch (type) {
            case SHARED:
                SharedRide shared = new SharedRide(pickup, drop);
                shared.setMaxPassengers(4);
                System.out.println("[Factory] Created SharedRide");
                return shared;

            case PREMIUM:
                Ride premium = new Ride(pickup, drop, RideType.PREMIUM);
                System.out.println("[Factory] Created Premium Ride");
                return premium;

            case ECONOMY:
            default:
                Ride economy = new Ride(pickup, drop, RideType.ECONOMY);
                System.out.println("[Factory] Created Economy Ride");
                return economy;
        }
    }
}
