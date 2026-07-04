package com.ridebooking.model;

/**
 * RideOption — matches UML class diagram.
 * Attributes: optionType, price, estimatedTime.
 * Association: FareEstimator → RideOption (1 to many).
 * Transient DTO returned by FareEstimator.sortRideOptions().
 */
public class RideOption {

    private String optionType;   // ECONOMY, PREMIUM, SHARED, METRO
    private double price;
    private int estimatedTime;   // minutes

    public RideOption() {}

    public RideOption(String optionType, double price, int estimatedTime) {
        this.optionType = optionType;
        this.price = price;
        this.estimatedTime = estimatedTime;
    }

    // Getters and Setters
    public String getOptionType() { return optionType; }
    public void setOptionType(String optionType) { this.optionType = optionType; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }
}
