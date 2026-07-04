package com.ridebooking.controller;

import com.ridebooking.model.Rider;
import com.ridebooking.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * RiderController — REST endpoints for rider operations.
 * DIP: Depends on RiderService interface, not implementation.
 */
@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderService riderService;

    @Autowired
    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping
    public ResponseEntity<Rider> registerRider(@RequestBody Rider rider) {
        return ResponseEntity.ok(riderService.registerRider(rider));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rider> getRider(@PathVariable Long id) {
        return ResponseEntity.ok(riderService.getRiderById(id));
    }

    @GetMapping
    public ResponseEntity<List<Rider>> getAllRiders() {
        return ResponseEntity.ok(riderService.getAllRiders());
    }

    @PutMapping("/{id}/rate")
    public ResponseEntity<String> rateRider(@PathVariable Long id,
                                            @RequestParam float rating) {
        riderService.rateRider(id, rating);
        return ResponseEntity.ok("Rider rated successfully");
    }
}
