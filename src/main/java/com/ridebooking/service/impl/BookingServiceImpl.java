package com.ridebooking.service.impl;

import com.ridebooking.dto.BookRideRequest;
import com.ridebooking.exception.RideNotFoundException;
import com.ridebooking.model.*;
import com.ridebooking.model.enums.*;
import com.ridebooking.pattern.factory.RideFactory;
import com.ridebooking.pattern.strategy.FareStrategy;
import com.ridebooking.repository.BookingRepository;
import com.ridebooking.service.BookingService;
import com.ridebooking.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * BookingServiceImpl — SRP: Handles booking lifecycle.
 * Integrates:
 *   - Factory Pattern (RideFactory) for ride creation
 *   - Strategy Pattern (FareStrategy) for fare calculation
 * This is the primary orchestration class for the bookRide() use case.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RiderService riderService;
    private final RideFactory rideFactory;                    // FACTORY PATTERN
    private final Map<String, FareStrategy> fareStrategies;   // STRATEGY PATTERN (injected by Spring)

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              RiderService riderService,
                              RideFactory rideFactory,
                              Map<String, FareStrategy> fareStrategies) {
        this.bookingRepository = bookingRepository;
        this.riderService = riderService;
        this.rideFactory = rideFactory;
        this.fareStrategies = fareStrategies;
    }

    /**
     * USE CASE: bookRide()
     * 1. Get rider from database
     * 2. Create Location objects for pickup and drop
     * 3. Factory Pattern — create Ride based on type (Economy/Premium/Shared)
     * 4. Strategy Pattern — calculate fare using appropriate strategy
     * 5. Create and persist Booking
     */
    @Override
    public Booking bookRide(BookRideRequest request) {
        // Step 1: Get rider
        Rider rider = riderService.getRiderById(request.getRiderId());

        // Step 2: Create locations
        Location pickup = new Location(
                request.getPickupLat(), request.getPickupLng(), request.getPickupAddress());
        Location drop = new Location(
                request.getDropLat(), request.getDropLng(), request.getDropAddress());

        // Step 3: FACTORY PATTERN — create ride based on type
        Ride ride = rideFactory.createRide(request.getRideType(), pickup, drop);

        // Step 4: STRATEGY PATTERN — calculate fare using type-specific strategy
        String strategyKey = request.getRideType().name().toLowerCase() + "FareStrategy";
        FareStrategy strategy = fareStrategies.getOrDefault(strategyKey,
                fareStrategies.get("economyFareStrategy"));
        double fare = strategy.calculateFare(ride.getDistance(), 0);
        ride.setFare(fare);

        // Step 5: Create and save booking
        Booking booking = new Booking(rider, ride);
        return bookingRepository.save(booking);
    }

    /**
     * USE CASE: cancelRide() — cancels a booking and its associated ride
     */
    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.cancelBooking();
        booking.getRide().setRideStatus(RideStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RideNotFoundException("Booking not found: " + bookingId));
    }

    @Override
    public List<Booking> getBookingsByRider(Long riderId) {
        return bookingRepository.findByRiderUserId(riderId);
    }
}
