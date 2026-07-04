package com.ridebooking.model;

import com.ridebooking.model.enums.RideStatus;
import com.ridebooking.model.enums.RideType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Ride — matches UML class diagram.
 * Attributes: rideId, fare, rideStatus.
 * Methods: calculateFare() — delegates to FareStrategy.
 * Associations:
 *   - Ride → Location (1 pickup, 1 drop) via @Embedded
 *   - Ride → Driver (many-to-one)
 *   - Ride → Payment (1 to 1, owned by Payment side)
 * Inheritance: Ride → SharedRide (SINGLE_TABLE strategy).
 * LSP: SharedRide can substitute Ride anywhere.
 */
@Entity
@Table(name = "rides")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ride_category", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("STANDARD")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;

    private double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus = RideStatus.REQUESTED;

    @Enumerated(EnumType.STRING)
    private RideType rideType = RideType.ECONOMY;

    // UML: Ride → Location (pickup, 1-to-1 embedded)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "pickup_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude")),
        @AttributeOverride(name = "address",   column = @Column(name = "pickup_address"))
    })
    private Location pickupLocation;

    // UML: Ride → Location (drop, 1-to-1 embedded)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "drop_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "drop_longitude")),
        @AttributeOverride(name = "address",   column = @Column(name = "drop_address"))
    })
    private Location dropLocation;

    // UML: Ride → Driver (many-to-one — a driver can have many rides over time)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private double distance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Ride() {}

    public Ride(Location pickupLocation, Location dropLocation, RideType rideType) {
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
        this.rideStatus = RideStatus.REQUESTED;
        this.startTime = LocalDateTime.now();
        if (pickupLocation != null && dropLocation != null) {
            this.distance = pickupLocation.distanceTo(dropLocation);
        }
    }

    // UML diagram method — fare is set by Strategy Pattern externally
    public double calculateFare() {
        return this.fare;
    }

    // Getters and Setters
    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public RideStatus getRideStatus() { return rideStatus; }
    public void setRideStatus(RideStatus rideStatus) { this.rideStatus = rideStatus; }

    public RideType getRideType() { return rideType; }
    public void setRideType(RideType rideType) { this.rideType = rideType; }

    public Location getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(Location pickup) { this.pickupLocation = pickup; }

    public Location getDropLocation() { return dropLocation; }
    public void setDropLocation(Location drop) { this.dropLocation = drop; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
