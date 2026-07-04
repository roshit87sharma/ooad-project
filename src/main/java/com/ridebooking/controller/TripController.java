package com.ridebooking.controller;

import com.ridebooking.model.Location;
import com.ridebooking.pattern.facade.MultiModalTripFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TripController — REST endpoint for multi-modal trip planning.
 * Delegates to MultiModalTripFacade (FACADE PATTERN).
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final MultiModalTripFacade tripFacade;

    @Autowired
    public TripController(MultiModalTripFacade tripFacade) {
        this.tripFacade = tripFacade;
    }

    /** GET /api/trips/multimodal — FACADE PATTERN demonstration */
    @GetMapping("/multimodal")
    public ResponseEntity<MultiModalTripFacade.MultiModalTripPlan> planMultiModalTrip(
            @RequestParam String originAddress,
            @RequestParam String fromStation,
            @RequestParam String toStation,
            @RequestParam String destinationAddress) {

        Location origin = new Location(12.9716, 77.5946, originAddress);
        Location destination = new Location(12.9352, 77.6245, destinationAddress);

        MultiModalTripFacade.MultiModalTripPlan plan =
                tripFacade.planTrip(origin, fromStation, toStation, destination);

        return ResponseEntity.ok(plan);
    }
}
