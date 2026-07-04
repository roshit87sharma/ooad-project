package com.ridebooking.pattern.strategy;

import org.springframework.stereotype.Component;

/**
 * Shared ride fare strategy: cheapest option, shared with other riders.
 * Base fare Rs.30 + Rs.8/km + Rs.1/min.
 */
@Component("sharedFareStrategy")
public class SharedFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 30.0;
    private static final double COST_PER_KM = 8.0;
    private static final double COST_PER_MINUTE = 1.0;

    @Override
    public double calculateFare(double distance, double duration) {
        return BASE_FARE + (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
    }

    @Override
    public String getStrategyName() { return "SHARED"; }
}
