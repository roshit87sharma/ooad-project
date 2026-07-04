package com.ridebooking.pattern.adapter;

import com.ridebooking.pattern.facade.MetroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ADAPTER: Adapts MetroService's API to a standard fare format.
 * Used by FareEstimator and MultiModalTripFacade.
 *
 * Structure:
 *   Standard Fare API (Target)  ←  MetroServiceAdapter  →  MetroService (Adaptee)
 */
@Component
public class MetroServiceAdapter {

    private final MetroService metroService;

    @Autowired
    public MetroServiceAdapter(MetroService metroService) {
        this.metroService = metroService;
    }

    /** Adapts metro fare to standard double format */
    public double getMetroFare(String fromStation, String toStation) {
        return metroService.calculateMetroFare(fromStation, toStation);
    }

    /** Adapts metro time estimate to standard int format */
    public int getMetroEstimatedTime(String fromStation, String toStation) {
        return metroService.getEstimatedTime(fromStation, toStation);
    }

    /** Check route availability */
    public boolean isRouteAvailable(String fromStation, String toStation) {
        return metroService.getStationList().contains(fromStation)
            && metroService.getStationList().contains(toStation);
    }
}
