// CREATIONAL DESIGN PATTERN #1: SINGLETON PATTERN
// RideBookingSystem is a Singleton - only one instance exists

import java.util.*;

public class RideBookingSystem {
    private static RideBookingSystem instance;
    
    private Map<String, Driver> drivers;
    private Map<String, Customer> customers;
    private Map<String, Ride> rides;
    private Map<String, Payment> payments;
    private Map<String, Invoice> invoices;
    private int rideCounter;
    private int paymentCounter;
    private int invoiceCounter;

    // Private constructor prevents instantiation from outside
    private RideBookingSystem() {
        this.drivers = new HashMap<>();
        this.customers = new HashMap<>();
        this.rides = new HashMap<>();
        this.payments = new HashMap<>();
        this.invoices = new HashMap<>();
        this.rideCounter = 1000;
        this.paymentCounter = 1000;
        this.invoiceCounter = 1000;
        System.out.println("Ride Booking System Initialized (Singleton)");
    }

    // Public method to get the single instance
    public static synchronized RideBookingSystem getInstance() {
        if (instance == null) {
            instance = new RideBookingSystem();
        }
        return instance;
    }

    // Driver management
    public void registerDriver(Driver driver) {
        drivers.put(driver.getUserId(), driver);
        System.out.println("Driver registered: " + driver.getName());
    }

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        drivers.values().forEach(driver -> {
            if (driver.isAvailable()) {
                available.add(driver);
            }
        });
        return available;
    }

    // Customer management
    public void registerCustomer(Customer customer) {
        customers.put(customer.getUserId(), customer);
        System.out.println("Customer registered: " + customer.getName());
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    // Ride management
    public Ride createRide(Customer customer, String pickupLocation, String dropLocation) {
        String rideId = "RIDE_" + (++rideCounter);
        Ride ride = new Ride(rideId, customer, pickupLocation, dropLocation);
        rides.put(rideId, ride);
        System.out.println("Ride created: " + rideId);
        return ride;
    }

    public Ride getRide(String rideId) {
        return rides.get(rideId);
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides.values());
    }

    // Payment management
    public void registerPayment(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }

    public Payment getPayment(String paymentId) {
        return payments.get(paymentId);
    }

    // Invoice management
    public Invoice generateInvoice(Ride ride, Payment payment, Fare fare) {
        String invoiceId = "INV_" + (++invoiceCounter);
        Invoice invoice = new Invoice(invoiceId, ride.getRideId(), 
                                     ride.getCustomer().getUserId(), 
                                     ride.getDriver().getUserId(), 
                                     fare, payment);
        invoices.put(invoiceId, invoice);
        System.out.println("Invoice generated: " + invoiceId);
        return invoice;
    }

    public Invoice getInvoice(String invoiceId) {
        return invoices.get(invoiceId);
    }

    // System status
    public void displaySystemStatus() {
        System.out.println("\n========== SYSTEM STATUS ==========");
        System.out.println("Total Drivers: " + drivers.size());
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Total Rides: " + rides.size());
        System.out.println("Available Drivers: " + getAvailableDrivers().size());
        System.out.println("===================================\n");
    }
}
