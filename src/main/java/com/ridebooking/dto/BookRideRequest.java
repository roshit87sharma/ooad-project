package com.ridebooking.dto;

import com.ridebooking.model.enums.RideType;

/**
 * DTO for booking a ride — decouples REST request from domain model.
 */
public class BookRideRequest {

    private Long riderId;
    private double pickupLat;
    private double pickupLng;
    private String pickupAddress;
    private double dropLat;
    private double dropLng;
    private String dropAddress;
    private RideType rideType;

    public BookRideRequest() {}

    // Getters and Setters
    public Long getRiderId() { return riderId; }
    public void setRiderId(Long riderId) { this.riderId = riderId; }

    public double getPickupLat() { return pickupLat; }
    public void setPickupLat(double pickupLat) { this.pickupLat = pickupLat; }

    public double getPickupLng() { return pickupLng; }
    public void setPickupLng(double pickupLng) { this.pickupLng = pickupLng; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String address) { this.pickupAddress = address; }

    public double getDropLat() { return dropLat; }
    public void setDropLat(double dropLat) { this.dropLat = dropLat; }

    public double getDropLng() { return dropLng; }
    public void setDropLng(double dropLng) { this.dropLng = dropLng; }

    public String getDropAddress() { return dropAddress; }
    public void setDropAddress(String address) { this.dropAddress = address; }

    public RideType getRideType() { return rideType; }
    public void setRideType(RideType rideType) { this.rideType = rideType; }
}
