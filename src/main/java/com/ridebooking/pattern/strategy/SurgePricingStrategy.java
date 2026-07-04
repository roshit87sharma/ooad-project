package com.ridebooking.pattern.strategy;

import org.springframework.stereotype.Component;

/**
 * ADVANCED FEATURE: Surge Pricing Strategy.
 * Decorates any base FareStrategy with a surge multiplier.
 * Demonstrates OCP: pricing can be extended without modifying base strategies.
 */
@Component("surgeFareStrategy")
public class SurgePricingStrategy implements FareStrategy {

    private final FareStrategy baseStrategy;
    private final double surgeMultiplier;

    /** Default constructor: 1.5x surge on economy */
    public SurgePricingStrategy() {
        this.baseStrategy = new EconomyFareStrategy();
        this.surgeMultiplier = 1.5;
    }

    /** Parameterized constructor for custom surge on any strategy */
    public SurgePricingStrategy(FareStrategy baseStrategy, double surgeMultiplier) {
        this.baseStrategy = baseStrategy;
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public double calculateFare(double distance, double duration) {
        double baseFare = baseStrategy.calculateFare(distance, duration);
        double surgedFare = baseFare * surgeMultiplier;
        System.out.printf("[Surge] Base: Rs.%.2f × %.1fx = Rs.%.2f%n",
                baseFare, surgeMultiplier, surgedFare);
        return surgedFare;
    }

    @Override
    public String getStrategyName() {
        return "SURGE_" + surgeMultiplier + "x_" + baseStrategy.getStrategyName();
    }

    public double getSurgeMultiplier() { return surgeMultiplier; }
}
