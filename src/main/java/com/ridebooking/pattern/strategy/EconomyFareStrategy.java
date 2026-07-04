package com.ridebooking.pattern.strategy;

import org.springframework.stereotype.Component;

/**
 * Economy fare strategy: lowest cost.
 * Base fare Rs.50 + Rs.12/km + Rs.1.5/min.
 */
@Component("economyFareStrategy")
public class EconomyFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 50.0;
    private static final double COST_PER_KM = 12.0;
    private static final double COST_PER_MINUTE = 1.5;

    @Override
    public double calculateFare(double distance, double duration) {
        return BASE_FARE + (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
    }

    @Override
    public String getStrategyName() { return "ECONOMY"; }
}
