package com.ridebooking.pattern.strategy;

import org.springframework.stereotype.Component;

/**
 * Premium fare strategy: highest comfort, highest cost.
 * Base fare Rs.100 + Rs.20/km + Rs.3/min.
 */
@Component("premiumFareStrategy")
public class PremiumFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 100.0;
    private static final double COST_PER_KM = 20.0;
    private static final double COST_PER_MINUTE = 3.0;

    @Override
    public double calculateFare(double distance, double duration) {
        return BASE_FARE + (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
    }

    @Override
    public String getStrategyName() { return "PREMIUM"; }
}
