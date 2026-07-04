package com.ridebooking.pattern.facade;

import com.ridebooking.model.RideOption;
import com.ridebooking.pattern.adapter.MetroServiceAdapter;
import com.ridebooking.pattern.strategy.FareStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FareEstimator — matches UML class diagram.
 * Methods: calculateCabFare(), calculateSharedFare(),
 *          calculateMetroFare(), sortRideOptions().
 * Association: FareEstimator → RideOption (1 to many).
 *
 * Integrates Strategy Pattern (fare strategies) and Adapter Pattern (metro).
 */
@Component
public class FareEstimator {

    private final Map<String, FareStrategy> fareStrategies;
    private final MetroServiceAdapter metroAdapter;

    @Autowired
    public FareEstimator(Map<String, FareStrategy> fareStrategies,
                         MetroServiceAdapter metroAdapter) {
        this.fareStrategies = fareStrategies;
        this.metroAdapter = metroAdapter;
    }

    /** UML diagram method */
    public double calculateCabFare(double distance, String type) {
        String key = type.toLowerCase() + "FareStrategy";
        FareStrategy strategy = fareStrategies.getOrDefault(key,
                fareStrategies.get("economyFareStrategy"));
        return strategy.calculateFare(distance, 0);
    }

    /** UML diagram method */
    public double calculateSharedFare(double distance) {
        FareStrategy strategy = fareStrategies.get("sharedFareStrategy");
        return strategy.calculateFare(distance, 0);
    }

    /** UML diagram method */
    public double calculateMetroFare(String fromStation, String toStation) {
        return metroAdapter.getMetroFare(fromStation, toStation);
    }

    /**
     * UML diagram method — returns sorted RideOptions.
     * Association: FareEstimator → RideOption (1 to many).
     */
    public List<RideOption> sortRideOptions(double distance) {
        List<RideOption> options = new ArrayList<>();

        options.add(new RideOption("ECONOMY",
                calculateCabFare(distance, "economy"),
                (int) (distance * 3)));

        options.add(new RideOption("PREMIUM",
                calculateCabFare(distance, "premium"),
                (int) (distance * 2.5)));

        options.add(new RideOption("SHARED",
                calculateSharedFare(distance),
                (int) (distance * 4)));

        // Sort by price ascending
        return options.stream()
                .sorted(Comparator.comparingDouble(RideOption::getPrice))
                .collect(Collectors.toList());
    }
}
