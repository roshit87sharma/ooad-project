package com.ridebooking.controller;

import com.ridebooking.dto.BookRideRequest;
import com.ridebooking.model.Booking;
import com.ridebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * BookingController — REST endpoints for booking operations.
 * Delegates to BookingService which integrates Factory + Strategy patterns.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** POST /api/bookings — USE CASE: bookRide() */
    @PostMapping
    public ResponseEntity<Booking> bookRide(@RequestBody BookRideRequest request) {
        return ResponseEntity.ok(bookingService.bookRide(request));
    }

    /** PUT /api/bookings/{id}/cancel — USE CASE: cancelRide() */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<Booking>> getBookingsByRider(@PathVariable Long riderId) {
        return ResponseEntity.ok(bookingService.getBookingsByRider(riderId));
    }
}
