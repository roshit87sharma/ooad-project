package com.ridebooking.pattern.strategy;

/**
 * ═══════════════════════════════════════════════════════════
 *  BEHAVIORAL DESIGN PATTERN: STRATEGY
 * ═══════════════════════════════════════════════════════════
 * Defines the interface for fare calculation algorithms.
 * Each ride type (Economy, Premium, Shared) has its own strategy.
 * OCP: New strategies can be added without modifying existing code.
 *
 * Spring auto-discovers all @Component implementations and injects
 * them as a Map<String, FareStrategy> keyed by bean name.
 */
public interface FareStrategy {

    /**
     * Calculate fare based on distance and duration.
     * @param distance  distance in kilometers
     * @param duration  duration in minutes
     * @return calculated fare amount
     */
    double calculateFare(double distance, double duration);

    /** Returns the strategy identifier */
    String getStrategyName();
}
