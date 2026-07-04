package com.ridebooking.controller;

import com.ridebooking.model.Ride;
import com.ridebooking.model.RideOption;
import com.ridebooking.model.enums.RideStatus;
import com.ridebooking.pattern.facade.FareEstimator;
import com.ridebooking.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * RideController — REST endpoints for ride lifecycle and fare estimation.
 * Delegates state transitions to RideService (which uses State Pattern).
 */
@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final FareEstimator fareEstimator;

    @Autowired
    public RideController(RideService rideService, FareEstimator fareEstimator) {
        this.rideService = rideService;
        this.fareEstimator = fareEstimator;
    }

    /** PUT /api/rides/{id}/accept — USE CASE: acceptRide() */
    @PutMapping("/{id}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable Long id,
                                           @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.acceptRide(id, driverId));
    }

    /** PUT /api/rides/{id}/start — USE CASE: startRide() */
    @PutMapping("/{id}/start")
    public ResponseEntity<Ride> startRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.startRide(id));
    }

    /** PUT /api/rides/{id}/complete — USE CASE: completeRide() */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }

    /** PUT /api/rides/{id}/cancel — USE CASE: cancelRide() */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.cancelRide(id));
    }

    /** GET /api/rides/{id}/track — USE CASE: trackRide() */
    @GetMapping("/{id}/track")
    public ResponseEntity<Ride> trackRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.trackRide(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<RideStatus> getRideStatus(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideStatus(id));
    }

    /** GET /api/rides/estimate — FareEstimator with sorted RideOptions */
    @GetMapping("/estimate")
    public ResponseEntity<List<RideOption>> estimateFare(@RequestParam double distance) {
        return ResponseEntity.ok(fareEstimator.sortRideOptions(distance));
    }
}
