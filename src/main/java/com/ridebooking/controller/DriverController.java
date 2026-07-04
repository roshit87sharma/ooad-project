package com.ridebooking.controller;

import com.ridebooking.model.Driver;
import com.ridebooking.model.enums.DriverStatus;
import com.ridebooking.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * DriverController — REST endpoints for driver operations.
 */
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.registerDriver(driver));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam DriverStatus status) {
        driverService.updateDriverStatus(id, status);
        return ResponseEntity.ok("Driver status updated to " + status);
    }

    @PutMapping("/{id}/login")
    public ResponseEntity<String> loginDriver(@PathVariable Long id) {
        driverService.updateDriverStatus(id, DriverStatus.AVAILABLE);
        return ResponseEntity.ok("Driver logged in");
    }

    @PutMapping("/{id}/logout")
    public ResponseEntity<String> logoutDriver(@PathVariable Long id) {
        driverService.updateDriverStatus(id, DriverStatus.OFFLINE);
        return ResponseEntity.ok("Driver logged out");
    }
}
