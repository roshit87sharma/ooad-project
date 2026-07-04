package com.ridebooking.pattern.facade;

import com.ridebooking.model.Location;
import com.ridebooking.pattern.adapter.MetroServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════
 *  STRUCTURAL DESIGN PATTERN: FACADE
 * ═══════════════════════════════════════════════════════════
 * MultiModalTrip Facade — matches UML class diagram (tripId, planTrip).
 * Simplifies complex trip planning across multiple transport modes.
 * Coordinates: FareEstimator + MetroService + Cab booking.
 *
 * Client calls ONE method (planTrip) instead of coordinating 3 services.
 */
@Component
public class MultiModalTripFacade {

    private final FareEstimator fareEstimator;
    private final MetroServiceAdapter metroAdapter;
    private Long tripId;

    @Autowired
    public MultiModalTripFacade(FareEstimator fareEstimator,
                                MetroServiceAdapter metroAdapter) {
        this.fareEstimator = fareEstimator;
        this.metroAdapter = metroAdapter;
    }

    /**
     * UML diagram method: planTrip()
     * FACADE: Client calls this ONE method.
     * Internally coordinates: cab fare + metro fare + cab fare.
     */
    public MultiModalTripPlan planTrip(Location origin, String nearestFromStation,
                                       String nearestToStation, Location destination) {
        this.tripId = System.currentTimeMillis();
        MultiModalTripPlan plan = new MultiModalTripPlan(tripId);

        // Leg 1: Cab from origin to metro station
        double distToStation = 3.0;
        double cabFare1 = fareEstimator.calculateCabFare(distToStation, "economy");
        plan.addLeg("CAB_TO_METRO", cabFare1, (int) (distToStation * 3),
                origin.getAddress() + " → " + nearestFromStation + " Station");

        // Leg 2: Metro ride
        double metroFare = fareEstimator.calculateMetroFare(nearestFromStation, nearestToStation);
        int metroTime = metroAdapter.getMetroEstimatedTime(nearestFromStation, nearestToStation);
        plan.addLeg("METRO", metroFare, metroTime,
                nearestFromStation + " → " + nearestToStation);

        // Leg 3: Cab from metro station to destination
        double distFromStation = 2.5;
        double cabFare2 = fareEstimator.calculateCabFare(distFromStation, "economy");
        plan.addLeg("CAB_FROM_METRO", cabFare2, (int) (distFromStation * 3),
                nearestToStation + " Station → " + destination.getAddress());

        plan.calculateTotals();
        return plan;
    }

    public Long getTripId() { return tripId; }

    // ─── Inner classes for trip plan result ───

    public static class MultiModalTripPlan {
        private Long tripId;
        private List<TripLeg> legs = new ArrayList<>();
        private double totalFare;
        private int totalTime;

        public MultiModalTripPlan(Long tripId) { this.tripId = tripId; }

        public void addLeg(String mode, double fare, int timeMinutes, String description) {
            legs.add(new TripLeg(mode, fare, timeMinutes, description));
        }

        public void calculateTotals() {
            totalFare = legs.stream().mapToDouble(TripLeg::getFare).sum();
            totalTime = legs.stream().mapToInt(TripLeg::getTimeMinutes).sum();
        }

        public Long getTripId() { return tripId; }
        public List<TripLeg> getLegs() { return legs; }
        public double getTotalFare() { return totalFare; }
        public int getTotalTime() { return totalTime; }
    }

    public static class TripLeg {
        private String mode;
        private double fare;
        private int timeMinutes;
        private String description;

        public TripLeg(String mode, double fare, int timeMinutes, String description) {
            this.mode = mode;
            this.fare = fare;
            this.timeMinutes = timeMinutes;
            this.description = description;
        }

        public String getMode() { return mode; }
        public double getFare() { return fare; }
        public int getTimeMinutes() { return timeMinutes; }
        public String getDescription() { return description; }
    }
}
