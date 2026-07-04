package com.ridebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Online Ride Booking System.
 * Spring Boot auto-configures all layers: Controller, Service, Repository, Model.
 */
@SpringBootApplication
public class RideBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideBookingApplication.class, args);
    }
}
