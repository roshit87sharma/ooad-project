// Main Demonstration Class
// Shows all MVC components and design patterns in action

public class RideBookingSystemDemo {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          RIDE BOOKING SYSTEM - MVC ARCHITECTURE             ║");
        System.out.println("║        WITH CREATIONAL DESIGN PATTERNS DEMO                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // PATTERN 1: SINGLETON - Get system instance
        System.out.println("▶ PATTERN 1: SINGLETON PATTERN (RideBookingSystem)");
        System.out.println("-".repeat(60));
        RideBookingSystem system = RideBookingSystem.getInstance();
        RideBookingSystem system2 = RideBookingSystem.getInstance();
        System.out.println("Same instance? " + (system == system2) + "\n");

        // Initialize controllers
        RideController rideController = new RideController(system);
        DriverController driverController = new DriverController(system);
        CustomerController customerController = new CustomerController(system);
        FareController fareController = new FareController(system);
        PaymentController paymentController = new PaymentController(system, new ConcretePaymentGateway());

        // Create sample customers
        Customer customer1 = customerController.registerCustomer(
            "CUST_001", "Ravi Kumar", "ravi@email.com", "9876543210");
        Customer customer2 = customerController.registerCustomer(
            "CUST_002", "Priya Singh", "priya@email.com", "9876543211");
        customerController.addWallet("CUST_001", 5000);
        customerController.addWallet("CUST_002", 3000);

        // Create sample drivers
        Driver driver1 = driverController.registerDriver(
            "DRV_001", "Arjun Patel", "arjun@email.com", "9987654321", 
            "DL12345", "VH98765");
        Driver driver2 = driverController.registerDriver(
            "DRV_002", "Vikram Singh", "vikram@email.com", "9987654322", 
            "DL12346", "VH98766");

        driverController.logInDriver("DRV_001");
        driverController.logInDriver("DRV_002");

        system.displaySystemStatus();

        // PATTERN 2: FACTORY PATTERN - Create different types of rides
        System.out.println("▶ PATTERN 2: FACTORY PATTERN (RideFactory)");
        System.out.println("-".repeat(60));
        String rideId1 = "RIDE_1001";
        RideType economyRide = RideFactory.createRide(
            RideFactory.RideTypeEnum.ECONOMY, rideId1, customer1, 
            "Downtown", "Airport");
        economyRide.displayDetails();
        double economyFare = economyRide.calculateFare(25.5);
        System.out.println("Estimated Fare for 25.5 km: Rs." + economyFare + "\n");

        String rideId2 = "RIDE_1002";
        RideType premiumRide = RideFactory.createRide(
            RideFactory.RideTypeEnum.PREMIUM, rideId2, customer2, 
            "Mall Road", "Railway Station");
        premiumRide.displayDetails();
        double premiumFare = premiumRide.calculateFare(15.0);
        System.out.println("Estimated Fare for 15 km: Rs." + premiumFare + "\n");

        // PATTERN 3: BUILDER PATTERN - Construct complex Ride objects
        System.out.println("▶ PATTERN 3: BUILDER PATTERN (RideBuilder)");
        System.out.println("-".repeat(60));
        RideBuilder builder = new RideBuilder("RIDE_2001", customer1);
        builder.displayCurrentState();

        Ride complexRide = builder
            .setPickupLocation("Central Park")
            .setDropLocation("Tech Park")
            .setDriver(driver1)
            .setDistance(18.5)
            .setFare(450.0)
            .build();

        System.out.println("Complex Ride built successfully!");
        System.out.println("Ride ID: " + complexRide.getRideId());
        System.out.println("Driver: " + complexRide.getDriver().getName());
        System.out.println("Pickup: " + complexRide.getPickupLocation());
        System.out.println("Drop: " + complexRide.getDropLocation() + "\n");

        // PATTERN 4: PROTOTYPE PATTERN - Create rides by cloning prototypes
        System.out.println("▶ PATTERN 4: PROTOTYPE PATTERN (RidePrototype & Registry)");
        System.out.println("-".repeat(60));
        
        // Create prototype rides
        RidePrototype economyPrototype = new RidePrototype(
            "PROTO_ECONOMY", customer1, "Downtown Station", "Airport Terminal");
        RidePrototype premiumPrototype = new RidePrototype(
            "PROTO_PREMIUM", customer2, "Mall Road", "Bus Station");

        // Register prototypes
        RidePrototypeRegistry registry = RidePrototypeRegistry.getInstance();
        registry.registerPrototype("EconomyRide", economyPrototype);
        registry.registerPrototype("PremiumRide", premiumPrototype);
        registry.listPrototypes();

        // Clone prototypes to create new rides
        System.out.println("Creating new rides by cloning prototypes...\n");
        RidePrototype clonedRide1 = registry.getPrototypeWithNewId("EconomyRide", "RIDE_3001");
        clonedRide1.setDistance(18.5);
        clonedRide1.setFare(272.0);
        clonedRide1.displayDetails();

        RidePrototype clonedRide2 = registry.getPrototypeWithNewId("PremiumRide", "RIDE_3002");
        clonedRide2.setDistance(22.5);
        clonedRide2.setFare(550.0);
        clonedRide2.displayDetails();

        // Deep clone example
        System.out.println("Demonstrating prototype independence:");
        RidePrototype rideA = registry.getPrototypeWithNewId("EconomyRide", "RIDE_4001");
        RidePrototype rideB = rideA.cloneWithNewId("RIDE_4002");
        System.out.println("Ride A ID: " + rideA.getRideId());
        System.out.println("Ride B ID: " + rideB.getRideId());
        System.out.println("Both from same prototype but independent instances ✓\n");

        // Simulate a complete ride flow
        System.out.println("▶ SIMULATING COMPLETE RIDE FLOW");
        System.out.println("-".repeat(60));
        
        // Request ride
        Ride ride = rideController.requestRide(
            customer1, "City Center", "Beach Road");
        System.out.println("Ride Requested: " + ride.getRideId());
        System.out.println("Status: " + ride.getStatus());

        // Assign driver
        rideController.assignDriver(ride.getRideId(), driver1);
        System.out.println("Driver Assigned: " + driver1.getName());
        System.out.println("Status: " + ride.getStatus());

        // Start ride
        rideController.startRide(ride.getRideId());
        System.out.println("Ride Started");
        System.out.println("Status: " + ride.getStatus());

        // Complete ride
        rideController.completeRide(ride.getRideId(), 22.5);
        System.out.println("Ride Completed");
        System.out.println("Distance: " + ride.getDistance() + " km");
        System.out.println("Driver Completed Rides: " + driver1.getCompletedRides());

        // Calculate fare
        fareController.updateRideFare(ride.getRideId(), 22.5, 35.5);
        ride.setFare(450.0);
        System.out.println("Fare Calculated: Rs." + ride.getFare());

        // Process payment
        Payment payment = paymentController.processPayment(
            ride.getRideId(), 450.0, "CARD");
        System.out.println("Payment Status: " + payment.getStatus());

        // Generate invoice
        Fare fare = new Fare(22.5, 35.5);
        Invoice invoice = system.generateInvoice(ride, payment, fare);
        invoice.printInvoice();

        // Final system status
        system.displaySystemStatus();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║               DEMO COMPLETED SUCCESSFULLY                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
