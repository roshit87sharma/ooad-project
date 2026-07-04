package com.ridebooking.model;

import com.ridebooking.model.enums.BookingStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Booking — matches UML class diagram.
 * Attributes: bookingId, bookingStatus.
 * Methods: confirmBooking(), cancelBooking().
 * Associations:
 *   - Rider → Booking (many-to-one, with FK in bookings table)
 *   - Booking → Ride (1 to 1)
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;

    // UML: Rider → Booking (many-to-one)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    // UML: Booking → Ride (1 to 1)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    private LocalDateTime bookingTime;
    private LocalDateTime cancellationTime;

    public Booking() {}

    public Booking(Rider rider, Ride ride) {
        this.rider = rider;
        this.ride = ride;
        this.bookingStatus = BookingStatus.CONFIRMED;
        this.bookingTime = LocalDateTime.now();
    }

    // UML diagram methods
    public void confirmBooking() {
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public void cancelBooking() {
        this.bookingStatus = BookingStatus.CANCELLED;
        this.cancellationTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus status) { this.bookingStatus = status; }

    public Rider getRider() { return rider; }
    public void setRider(Rider rider) { this.rider = rider; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public LocalDateTime getCancellationTime() { return cancellationTime; }
    public void setCancellationTime(LocalDateTime time) { this.cancellationTime = time; }
}
