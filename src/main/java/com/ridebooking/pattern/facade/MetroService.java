package com.ridebooking.pattern.facade;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * MetroService — matches UML class diagram.
 * Attributes: stationList (List).
 * Methods: calculateMetroFare().
 * Acts as an ADAPTEE in the Adapter Pattern.
 */
@Component
public class MetroService {

    private final List<String> stationList;
    private final Map<String, Map<String, Double>> fareMap;

    public MetroService() {
        this.stationList = Arrays.asList(
            "Central", "MG Road", "Indiranagar", "Whitefield",
            "Majestic", "Yeshwanthpur", "Peenya", "Nagasandra"
        );
        this.fareMap = new HashMap<>();
        initializeFares();
    }

    private void initializeFares() {
        for (int i = 0; i < stationList.size(); i++) {
            fareMap.put(stationList.get(i), new HashMap<>());
            for (int j = 0; j < stationList.size(); j++) {
                double fare = Math.abs(i - j) * 10.0 + 10.0;
                fareMap.get(stationList.get(i)).put(stationList.get(j), fare);
            }
        }
    }

    /** UML diagram method */
    public double calculateMetroFare(String fromStation, String toStation) {
        if (fareMap.containsKey(fromStation)
                && fareMap.get(fromStation).containsKey(toStation)) {
            return fareMap.get(fromStation).get(toStation);
        }
        return -1;
    }

    public int getEstimatedTime(String fromStation, String toStation) {
        int fromIdx = stationList.indexOf(fromStation);
        int toIdx = stationList.indexOf(toStation);
        if (fromIdx < 0 || toIdx < 0) return -1;
        return Math.abs(fromIdx - toIdx) * 3; // 3 min per station
    }

    /** UML diagram attribute */
    public List<String> getStationList() {
        return Collections.unmodifiableList(stationList);
    }
}
