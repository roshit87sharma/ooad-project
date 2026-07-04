package com.ridebooking.config;

import com.ridebooking.model.*;
import com.ridebooking.model.enums.*;
import com.ridebooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Loads sample data into H2 on startup for demo/viva purposes.
 * Creates 2 riders and 2 drivers with vehicles.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public void run(String... args) {
        // Sample Riders
        Rider rider1 = new Rider("Ravi Kumar", "ravi@email.com", "9876543210");
        Rider rider2 = new Rider("Priya Singh", "priya@email.com", "9876543211");
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        // Sample Driver 1 with Vehicle (1-to-1)
        Driver driver1 = new Driver("Arjun Patel", "arjun@email.com", "9987654321");
        Vehicle vehicle1 = new Vehicle("Sedan", "KA01AB1234");
        vehicle1.setModel("Swift Dzire");
        vehicle1.setColor("White");
        driver1.setVehicle(vehicle1);
        driver1.setAvailabilityStatus(DriverStatus.AVAILABLE);

        // Sample Driver 2 with Vehicle (1-to-1)
        Driver driver2 = new Driver("Vikram Singh", "vikram@email.com", "9987654322");
        Vehicle vehicle2 = new Vehicle("SUV", "KA01CD5678");
        vehicle2.setModel("Toyota Innova");
        vehicle2.setColor("Black");
        driver2.setVehicle(vehicle2);
        driver2.setAvailabilityStatus(DriverStatus.AVAILABLE);

        driverRepository.save(driver1);
        driverRepository.save(driver2);

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║       ONLINE RIDE BOOKING SYSTEM — STARTED           ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║  Riders loaded  : " + riderRepository.count() + "                                ║");
        System.out.println("║  Drivers loaded : " + driverRepository.count() + "                                ║");
        System.out.println("║                                                       ║");
        System.out.println("║  API Base URL   : http://localhost:8080/api            ║");
        System.out.println("║  H2 Console     : http://localhost:8080/h2-console     ║");
        System.out.println("║  DB URL         : jdbc:h2:mem:ridebookingdb            ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
