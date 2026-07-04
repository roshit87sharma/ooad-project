package com.ridebooking.service;

import com.ridebooking.dto.BookRideRequest;
import com.ridebooking.model.Booking;
import java.util.List;

/**
 * BookingService — DIP: Controllers depend on this interface.
 * Implements core use cases: bookRide(), cancelBooking().
 */
public interface BookingService {
    Booking bookRide(BookRideRequest request);
    Booking cancelBooking(Long bookingId);
    Booking getBookingById(Long bookingId);
    List<Booking> getBookingsByRider(Long riderId);
}
