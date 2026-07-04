package com.ridebooking.model;

import jakarta.persistence.Embeddable;

/**
 * Location — matches UML class diagram.
 * Attributes: latitude, longitude, address.
 * Used as @Embeddable in Ride (pickup and drop locations).
 * Association: Ride → Location (1 pickup, 1 drop) via embedding.
 */
@Embeddable
public class Location {

    private double latitude;
    private double longitude;
    private String address;

    public Location() {}

    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    // Haversine distance calculation (km)
    public double distanceTo(Location other) {
        double earthRadius = 6371.0;
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(this.latitude))
                 * Math.cos(Math.toRadians(other.latitude))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    // Getters and Setters
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
