# 🚗 Online Ride Booking System — Complete Implementation Guide

## OOAD Mini Project — Full Code Explanation

> **Tech Stack**: Spring Boot 3.4 · JPA/Hibernate · H2 Database · React · Tailwind CSS  
> **Design Patterns**: Strategy · Factory · Adapter · Facade · State  
> **SOLID Principles**: SRP · OCP · LSP · DIP  

---

## 📋 Table of Contents

1. [How to Run](#1-how-to-run)
2. [Project Architecture](#2-project-architecture)
3. [Full Code Walkthrough](#3-full-code-walkthrough)
   - 3.1 [Application Entry Point](#31-application-entry-point)
   - 3.2 [Configuration](#32-configuration)
   - 3.3 [Enums](#33-enums)
   - 3.4 [Model Layer — All Entity Classes](#34-model-layer--all-entity-classes)
   - 3.5 [Repository Layer](#35-repository-layer)
   - 3.6 [Service Layer — Interfaces](#36-service-layer--interfaces)
   - 3.7 [Service Layer — Implementations](#37-service-layer--implementations)
   - 3.8 [Controller Layer](#38-controller-layer)
   - 3.9 [DTO Layer](#39-dto-layer)
   - 3.10 [Exception Handling](#310-exception-handling)
   - 3.11 [Design Patterns — Full Code](#311-design-patterns--full-code)
4. [SOLID Principles Explained](#4-solid-principles-explained)
5. [Design Patterns Explained](#5-design-patterns-explained)
6. [UML Diagrams](#6-uml-diagrams)
7. [REST API Reference](#7-rest-api-reference)
8. [Demo Flow](#8-demo-flow)

---

## 1. How to Run

```bash
# Backend (Spring Boot on port 8080)
cd mini_project
mvn spring-boot:run

# Frontend (React on port 5173)
cd mini_project/frontend
npm install
npm run dev
```

Open http://localhost:5173 in browser.

---

## 2. Project Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                   FRONTEND (React + Vite)                    │
│       http://localhost:5173  →  Proxy /api to :8080          │
└────────────────────────┬────────────────────────────────────┘
                         │  HTTP REST (JSON)
┌────────────────────────▼────────────────────────────────────┐
│               CONTROLLER LAYER (REST APIs)                   │
│  RiderController, DriverController, BookingController,       │
│  RideController, PaymentController, TripController           │
├─────────────────────────────────────────────────────────────┤
│               SERVICE LAYER (Business Logic)                 │
│  RiderServiceImpl, DriverServiceImpl, BookingServiceImpl,    │
│  RideServiceImpl, PaymentServiceImpl                         │
├─────────────────────────────────────────────────────────────┤
│             DESIGN PATTERNS (Core OOAD Logic)                │
│  FareStrategy(Strategy), RideFactory(Factory),               │
│  RazorpayAdapter(Adapter), MultiModalTripFacade(Facade),     │
│  RideState(State)                                            │
├─────────────────────────────────────────────────────────────┤
│              REPOSITORY LAYER (Spring Data JPA)              │
│  RiderRepo, DriverRepo, BookingRepo, RideRepo,              │
│  VehicleRepo, PaymentRepo                                    │
├─────────────────────────────────────────────────────────────┤
│              DATABASE (H2 In-Memory)                         │
│  Tables: riders, drivers, vehicles, rides, bookings, payments│
└─────────────────────────────────────────────────────────────┘
```

**Why 4 Layers?**
- **Controller** → Receives HTTP requests, delegates to service, returns JSON responses. No business logic here.
- **Service** → Contains ALL business logic, validates data, orchestrates patterns.
- **Repository** → Spring Data JPA generates SQL queries automatically from method names.
- **Model** → Java classes mapped to database tables using JPA annotations.

---

## 3. Full Code Walkthrough

### 3.1 Application Entry Point

**File: `RideBookingApplication.java`**

```java
@SpringBootApplication
public class RideBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RideBookingApplication.class, args);
    }
}
```

**Explanation:**
- `@SpringBootApplication` is a shortcut for 3 annotations:
  - `@Configuration` — this class can define Spring beans
  - `@EnableAutoConfiguration` — Spring auto-configures JPA, H2, Tomcat, etc.
  - `@ComponentScan` — scans all packages under `com.ridebooking` to find `@Component`, `@Service`, `@Controller` classes
- `SpringApplication.run()` starts the embedded Tomcat server on port 8080, creates all beans, connects to H2 database, and creates tables.

---

### 3.2 Configuration

**File: `application.properties`**

```properties
spring.application.name=ride-booking-system
server.port=8080

# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:ridebookingdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Web Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Line-by-line explanation:**
- `jdbc:h2:mem:ridebookingdb` — Creates a database called "ridebookingdb" in RAM (not on disk). Fast but data is lost when app stops.
- `ddl-auto=create-drop` — Hibernate automatically CREATE all tables on startup from `@Entity` classes, and DROP them on shutdown.
- `show-sql=true` — Prints every SQL query to the console for debugging.
- `h2.console.enabled=true` — Enables a web-based SQL browser at `/h2-console` where you can inspect the database.

**File: `DataInitializer.java`**

```java
@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired private RiderRepository riderRepository;
    @Autowired private DriverRepository driverRepository;

    @Override
    public void run(String... args) {
        // Create 2 sample riders
        Rider rider1 = new Rider("Ravi Kumar", "ravi@email.com", "9876543210");
        riderRepository.save(rider1);

        // Create driver with vehicle (1-to-1 relationship)
        Driver driver1 = new Driver("Arjun Patel", "arjun@email.com", "9987654321");
        Vehicle vehicle1 = new Vehicle("Sedan", "KA01AB1234");
        driver1.setVehicle(vehicle1);              // Sets 1-to-1 association
        driver1.setAvailabilityStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver1);            // Cascades to save vehicle too
    }
}
```

**Explanation:**
- `CommandLineRunner` — Spring calls the `run()` method AFTER the application starts. We use it to insert sample data into the empty H2 database.
- `driver1.setVehicle(vehicle1)` — Because Driver has `@OneToOne(cascade = CascadeType.ALL)`, saving the Driver automatically saves the Vehicle too. This is the UML "Driver owns Vehicle" relationship.

---

### 3.3 Enums

**File: `RideStatus.java`** — Represents the ride lifecycle states
```java
public enum RideStatus {
    REQUESTED,         // Ride just created, waiting for driver
    DRIVER_ASSIGNED,   // Driver accepted the ride
    IN_PROGRESS,       // Driver started the trip
    COMPLETED,         // Trip finished
    CANCELLED          // Ride was cancelled
}
```
These enum values are stored as strings in the database (via `@Enumerated(EnumType.STRING)`). The **State Pattern** uses these values to determine which transitions are valid.

**File: `RideType.java`** — Connects Factory and Strategy patterns
```java
public enum RideType {
    ECONOMY,   // Cheapest, basic car
    PREMIUM,   // Luxury car, higher fare
    SHARED     // Share with others, cheapest
}
```
The **Factory Pattern** reads this to decide whether to create a `Ride` or `SharedRide`. The **Strategy Pattern** reads this to pick the right fare calculation algorithm.

**File: `BookingStatus.java`** — `CONFIRMED`, `COMPLETED`, `CANCELLED`

**File: `PaymentStatus.java`** — `PENDING`, `PROCESSING`, `SUCCESS`, `FAILED`, `REFUNDED`

**File: `PaymentMode.java`** — `CASH`, `CARD`, `UPI`, `WALLET`

**File: `DriverStatus.java`** — `AVAILABLE`, `BUSY`, `ON_TRIP`, `OFFLINE`

---

### 3.4 Model Layer — All Entity Classes

#### `User.java` — Abstract Base Class (UML: Inheritance Root)

```java
@MappedSuperclass                          // ← NOT an entity itself, but provides fields to children
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;                   // Auto-incremented primary key

    @Column(nullable = false)
    private String name;                   // Cannot be null in database

    @Column(nullable = false, unique = true)
    private String email;                  // Email must be unique across all riders/drivers

    @Column(nullable = false)
    private String phone;

    protected User() {}                    // JPA requires a no-arg constructor

    protected User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public abstract void login();          // Forced implementation in Rider and Driver
    public abstract void logout();         // Abstract methods → UML "abstract class" requirement
}
```

**Why `@MappedSuperclass` instead of `@Entity`?**
- `@MappedSuperclass` means User does NOT get its own database table. Instead, Rider and Driver each get their OWN tables with userId, name, email, phone columns COPIED into them.
- This matches the UML where User is an abstract class that Rider and Driver extend.

---

#### `Rider.java` — Extends User (UML: User → Rider)

```java
@Entity
@Table(name = "riders")                    // Creates "riders" table in database
public class Rider extends User {          // Inherits userId, name, email, phone from User

    private float riderRating = 5.0f;      // Default rating = 5 stars

    // UML: Rider → Booking (1 to many)
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore                            // Prevents infinite JSON loop (Rider→Bookings→Rider→...)
    private List<Booking> bookings = new ArrayList<>();

    public Rider() {}

    public Rider(String name, String email, String phone) {
        super(name, email, phone);         // Calls User constructor
    }

    // UML diagram methods (business logic is in service layer)
    public void requestRide() { }
    public void chooseRideOption() { }

    @Override
    public void login() { }               // Concrete implementation of abstract method
    @Override
    public void logout() { }
}
```

**Key annotations explained:**
- `@OneToMany(mappedBy = "rider")` — This is the inverse side of the Rider-Booking relationship. The `mappedBy = "rider"` tells JPA that the `rider` field in Booking class owns the foreign key.
- `cascade = CascadeType.ALL` — If you delete a Rider, all their Bookings are automatically deleted.
- `fetch = FetchType.LAZY` — Bookings are NOT loaded from database until you actually call `rider.getBookings()`. This improves performance.
- `@JsonIgnore` — Without this, Jackson would serialize Rider → Bookings → each Booking has a Rider → infinite loop. This breaks the loop.

---

#### `Driver.java` — Extends User (UML: User → Driver)

```java
@Entity
@Table(name = "drivers")
public class Driver extends User {

    private float driverRating = 5.0f;

    @Enumerated(EnumType.STRING)            // Store "AVAILABLE" as text, not number
    private DriverStatus availabilityStatus = DriverStatus.OFFLINE;

    // UML: Driver → Vehicle (1 to 1, "owns")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicleId")
    private Vehicle vehicle;

    // UML diagram methods — these directly modify the driver's status
    public void acceptRide() {
        this.availabilityStatus = DriverStatus.BUSY;       // Driver is now busy
    }
    public void startRide() {
        this.availabilityStatus = DriverStatus.ON_TRIP;    // Driver is driving
    }
    public void endRide() {
        this.availabilityStatus = DriverStatus.AVAILABLE;  // Driver is free again
    }

    @Override
    public void login() {
        this.availabilityStatus = DriverStatus.AVAILABLE;  // Going online
    }
    @Override
    public void logout() {
        this.availabilityStatus = DriverStatus.OFFLINE;    // Going offline
    }

    public boolean isAvailable() {
        return availabilityStatus == DriverStatus.AVAILABLE;
    }
}
```

**Key annotations explained:**
- `@OneToOne` — Each Driver has exactly ONE Vehicle and each Vehicle belongs to ONE Driver. This is the UML "owns" relationship.
- `@JoinColumn(name = "vehicle_id")` — Creates a `vehicle_id` foreign key column in the `drivers` table.
- `fetch = FetchType.EAGER` — Vehicle is ALWAYS loaded with the Driver (we always need vehicle info when showing driver details).

---

#### `Vehicle.java` — UML: Driver owns Vehicle (1:1)

```java
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(nullable = false)
    private String vehicleType;            // "Sedan", "SUV", "Hatchback"

    @Column(nullable = false, unique = true)
    private String vehicleNumber;          // "KA01AB1234" — unique plate number

    private String model;                  // "Swift Dzire", "Toyota Innova"
    private String color;                  // "White", "Black"

    public Vehicle() {}

    public Vehicle(String vehicleType, String vehicleNumber) {
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
    }
}
```

---

#### `Location.java` — UML: Ride has Location (Embedded)

```java
@Embeddable                                // NOT a separate table — embedded inside Ride table
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

    /**
     * Haversine formula — calculates distance between two GPS coordinates.
     * Returns distance in kilometers, accounting for Earth's curvature.
     */
    public double distanceTo(Location other) {
        double R = 6371;                   // Earth's radius in km
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
```

**Why `@Embeddable` instead of `@Entity`?**
- Location doesn't need its own table. It is embedded INSIDE the `rides` table as columns: `pickup_latitude`, `pickup_longitude`, `pickup_address`, `drop_latitude`, `drop_longitude`, `drop_address`.
- The `distanceTo()` method uses the real Haversine formula to calculate distance between GPS coordinates.

---

#### `Ride.java` — Core Entity (UML: Ride with Inheritance)

```java
@Entity
@Table(name = "rides")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // Ride + SharedRide share ONE table
@DiscriminatorColumn(name = "ride_category")             // Column that tells them apart
@DiscriminatorValue("STANDARD")                          // Ride rows have ride_category = "STANDARD"
public class Ride {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;

    private double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus = RideStatus.REQUESTED;  // New rides start as REQUESTED

    @Enumerated(EnumType.STRING)
    private RideType rideType = RideType.ECONOMY;

    // UML: Ride → Location (pickup, embedded — shares same table)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "pickup_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude")),
        @AttributeOverride(name = "address",   column = @Column(name = "pickup_address"))
    })
    private Location pickupLocation;

    // UML: Ride → Location (drop, embedded — renamed columns to avoid collision)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "drop_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "drop_longitude")),
        @AttributeOverride(name = "address",   column = @Column(name = "drop_address"))
    })
    private Location dropLocation;

    // UML: Ride → Driver (many-to-one)
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
        // Auto-calculate distance from GPS coordinates
        if (pickupLocation != null && dropLocation != null) {
            this.distance = pickupLocation.distanceTo(dropLocation);
        }
    }
}
```

**Why SINGLE_TABLE Inheritance?**
- Both `Ride` and `SharedRide` are stored in the SAME `rides` table. A column called `ride_category` distinguishes them ("STANDARD" vs "SHARED").
- A `SharedRide` row has extra columns: `max_passengers` and `current_passengers` (these are NULL for regular rides).
- This is more efficient than separate tables and matches the UML inheritance relationship.

**Why `@AttributeOverrides`?**
- Since we embed Location TWICE (pickup and drop), we need different column names. Without this, both would try to create `latitude`, `longitude`, `address` columns — causing a conflict.

---

#### `SharedRide.java` — Extends Ride (UML: Ride → SharedRide)

```java
@Entity
@DiscriminatorValue("SHARED")              // SharedRide rows have ride_category = "SHARED"
public class SharedRide extends Ride {     // Inherits ALL fields from Ride

    private int maxPassengers = 4;         // Maximum riders allowed
    private int currentPassengers = 1;     // Starts with 1 (the person who booked)

    public SharedRide() {}

    public SharedRide(Location pickup, Location drop) {
        super(pickup, drop, RideType.SHARED);  // Calls Ride constructor
    }

    /**
     * LSP: This extra method doesn't break Ride behavior.
     * SharedRide adds functionality without removing any.
     */
    public boolean addPassenger() {
        if (currentPassengers < maxPassengers) {
            currentPassengers++;
            return true;
        }
        return false;                      // Car is full
    }

    /** Split the fare equally among all passengers */
    public double splitFare() {
        return getFare() / currentPassengers;
    }
}
```

**LSP (Liskov Substitution Principle) here:** SharedRide can be used ANYWHERE a Ride is expected. It doesn't override any methods in a way that breaks parent behavior. The `addPassenger()` and `splitFare()` methods are ADDITIONS, not changes.

---

#### `Booking.java` — Links Rider to Ride

```java
@Entity
@Table(name = "bookings")
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;

    // UML: Rider → Booking (many-to-one: many bookings belong to one rider)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    // UML: Booking → Ride (1 to 1: each booking has exactly one ride)
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
        this.bookingTime = LocalDateTime.now();     // Record when booking was made
    }

    public void cancelBooking() {
        this.bookingStatus = BookingStatus.CANCELLED;
        this.cancellationTime = LocalDateTime.now(); // Record when cancelled
    }
}
```

**Why separate Booking and Ride?**
- A **Booking** is a business transaction (who booked, when, status).
- A **Ride** is a physical trip (pickup, drop, distance, driver, fare).
- Separating them follows SRP — each class has one responsibility.

---

#### `Payment.java` — 1-to-1 with Ride

```java
@Entity
@Table(name = "payments")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private double amount;                         // Fare amount in rupees

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;               // CARD, UPI, WALLET, CASH

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // UML: Ride → Payment (1 to 1)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id")
    private Ride ride;

    private LocalDateTime transactionTime;

    public Payment() {}

    public Payment(double amount, PaymentMode mode, Ride ride) {
        this.amount = amount;
        this.paymentMode = mode;
        this.ride = ride;
        this.paymentStatus = PaymentStatus.PROCESSING;
    }

    /** Called after successful payment gateway response */
    public void processPayment() {
        this.paymentStatus = PaymentStatus.SUCCESS;
        this.transactionTime = LocalDateTime.now();
    }
}
```

---

#### `RideOption.java` — DTO for Fare Estimation

```java
public class RideOption {
    private String optionType;       // "ECONOMY", "PREMIUM", "SHARED"
    private double price;            // Calculated fare
    private int estimatedTime;       // Estimated trip time in minutes

    public RideOption(String optionType, double price, int estimatedTime) {
        this.optionType = optionType;
        this.price = price;
        this.estimatedTime = estimatedTime;
    }
}
```

This is a DTO (Data Transfer Object) — NOT an entity. It's used by the FareEstimator to return fare comparison options to the frontend.

---

### 3.5 Repository Layer

Each repository extends `JpaRepository<Entity, IDType>`, which provides free CRUD methods: `save()`, `findById()`, `findAll()`, `deleteById()`, `count()`.

```java
// RiderRepository.java
@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByEmail(String email);      // Spring generates SQL: SELECT * FROM riders WHERE email = ?
}

// DriverRepository.java
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    // Spring generates: SELECT * FROM drivers WHERE availability_status = ?
    List<Driver> findByAvailabilityStatus(DriverStatus status);
}

// BookingRepository.java
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRiderUserId(Long riderId);  // Navigates Booking → Rider → userId
}

// RideRepository.java
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByRideStatus(RideStatus status);
    List<Ride> findByDriverUserId(Long driverId);
}

// VehicleRepository.java
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> { }

// PaymentRepository.java
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByRideRideId(Long rideId);          // Navigates Payment → Ride → rideId
}
```

**How does `findByAvailabilityStatus` work?** Spring Data JPA parses the method name:
- `findBy` = SELECT query
- `AvailabilityStatus` = WHERE availability_status = ?
- Spring automatically generates the SQL implementation at runtime. You write ZERO SQL.

---

### 3.6 Service Layer — Interfaces

```java
// RiderService.java — Defines what rider operations exist
public interface RiderService {
    Rider registerRider(Rider rider);
    Rider getRiderById(Long id);
    List<Rider> getAllRiders();
    Rider rateRider(Long riderId, float rating);
}

// DriverService.java
public interface DriverService {
    Driver registerDriver(Driver driver);
    Driver getDriverById(Long id);
    List<Driver> getAvailableDrivers();
    Driver loginDriver(Long id);
    Driver logoutDriver(Long id);
    void updateDriverStatus(Long id, DriverStatus status);
}

// BookingService.java
public interface BookingService {
    Booking bookRide(BookRideRequest request);      // USE CASE: bookRide (Factory + Strategy)
    Booking cancelBooking(Long bookingId);
    Booking getBookingById(Long bookingId);
    List<Booking> getBookingsByRider(Long riderId);
}

// RideService.java
public interface RideService {
    Ride acceptRide(Long rideId, Long driverId);   // USE CASE: acceptRide (State Pattern)
    Ride startRide(Long rideId);                    // USE CASE: startRide (State Pattern)
    Ride completeRide(Long rideId);                 // USE CASE: completeRide (State Pattern)
    Ride cancelRide(Long rideId);                   // USE CASE: cancelRide (State Pattern)
    Ride trackRide(Long rideId);
    RideStatus getRideStatus(Long rideId);
    List<Ride> getRidesByDriver(Long driverId);
}

// PaymentService.java
public interface PaymentService {
    Payment processPayment(Long rideId, PaymentMode mode);  // USE CASE (Adapter Pattern)
    Payment getPaymentByRide(Long rideId);
    Payment refundPayment(Long paymentId);
}
```

**DIP (Dependency Inversion):** Controllers depend on these INTERFACES, not on implementations. Spring automatically injects the correct implementation at runtime.

---

### 3.7 Service Layer — Implementations

#### `BookingServiceImpl.java` — Uses Factory + Strategy Patterns

```java
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RiderService riderService;
    private final RideFactory rideFactory;                   // FACTORY PATTERN
    private final Map<String, FareStrategy> fareStrategies;  // STRATEGY PATTERN

    // Spring injects all dependencies via constructor (DIP)
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              RiderService riderService,
                              RideFactory rideFactory,
                              Map<String, FareStrategy> fareStrategies) {
        this.bookingRepository = bookingRepository;
        this.riderService = riderService;
        this.rideFactory = rideFactory;
        this.fareStrategies = fareStrategies;   // Spring auto-collects ALL FareStrategy beans
    }

    @Override
    public Booking bookRide(BookRideRequest request) {
        // Step 1: Get rider from database
        Rider rider = riderService.getRiderById(request.getRiderId());

        // Step 2: Create pickup and drop Location objects
        Location pickup = new Location(
                request.getPickupLat(), request.getPickupLng(), request.getPickupAddress());
        Location drop = new Location(
                request.getDropLat(), request.getDropLng(), request.getDropAddress());

        // Step 3: FACTORY PATTERN — creates Ride or SharedRide based on type
        Ride ride = rideFactory.createRide(request.getRideType(), pickup, drop);

        // Step 4: STRATEGY PATTERN — picks the right fare algorithm
        String strategyKey = request.getRideType().name().toLowerCase() + "FareStrategy";
        // e.g., ECONOMY → "economyFareStrategy" (matches @Component bean name)
        FareStrategy strategy = fareStrategies.getOrDefault(strategyKey,
                fareStrategies.get("economyFareStrategy"));
        double fare = strategy.calculateFare(ride.getDistance(), 0);
        ride.setFare(fare);

        // Step 5: Create Booking and save to database
        Booking booking = new Booking(rider, ride);
        return bookingRepository.save(booking);   // JPA persists Booking + Ride (cascaded)
    }
}
```

**How does `Map<String, FareStrategy>` work?**
- When you have multiple `@Component` beans implementing the same interface, Spring can inject them as a `Map` where key = bean name, value = bean instance.
- So `fareStrategies` map contains: `{"economyFareStrategy" → EconomyFareStrategy, "premiumFareStrategy" → PremiumFareStrategy, "sharedFareStrategy" → SharedFareStrategy, "surgeFareStrategy" → SurgePricingStrategy}`.

---

#### `RideServiceImpl.java` — Uses State Pattern

```java
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverService driverService;

    // USE CASE: acceptRide()
    // STATE PATTERN: REQUESTED → DRIVER_ASSIGNED
    @Override
    public Ride acceptRide(Long rideId, Long driverId) {
        Ride ride = getRideById(rideId);
        Driver driver = driverService.getDriverById(driverId);

        // STATE PATTERN — get current state object, validate and apply transition
        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.accept(ride);  // If REQUESTED → sets DRIVER_ASSIGNED
                                    // If COMPLETED → throws IllegalStateException

        ride.setDriver(driver);     // Assign driver to ride
        driver.acceptRide();        // Change driver status to BUSY
        driverService.updateDriverStatus(driverId, DriverStatus.BUSY);

        return rideRepository.save(ride);
    }

    // USE CASE: startRide()
    // STATE PATTERN: DRIVER_ASSIGNED → IN_PROGRESS
    @Override
    public Ride startRide(Long rideId) {
        Ride ride = getRideById(rideId);
        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.start(ride);   // If DRIVER_ASSIGNED → sets IN_PROGRESS
                                    // If REQUESTED → throws "no driver assigned"
        if (ride.getDriver() != null) {
            ride.getDriver().startRide();  // Driver status → ON_TRIP
        }
        return rideRepository.save(ride);
    }

    // USE CASE: completeRide()
    // STATE PATTERN: IN_PROGRESS → COMPLETED
    @Override
    public Ride completeRide(Long rideId) {
        Ride ride = getRideById(rideId);
        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.complete(ride); // If IN_PROGRESS → sets COMPLETED

        ride.setEndTime(LocalDateTime.now());
        if (ride.getDriver() != null) {
            ride.getDriver().endRide();   // Driver status → AVAILABLE
            driverService.updateDriverStatus(ride.getDriver().getUserId(), DriverStatus.AVAILABLE);
        }
        return rideRepository.save(ride);
    }

    // USE CASE: cancelRide()
    @Override
    public Ride cancelRide(Long rideId) {
        Ride ride = getRideById(rideId);
        RideState currentState = RideStateFactory.getState(ride.getRideStatus());
        currentState.cancel(ride);  // Most states allow cancel, except COMPLETED/CANCELLED

        if (ride.getDriver() != null) {
            ride.getDriver().endRide();
            driverService.updateDriverStatus(ride.getDriver().getUserId(), DriverStatus.AVAILABLE);
        }
        return rideRepository.save(ride);
    }

    // USE CASE: trackRide()
    @Override
    public Ride trackRide(Long rideId) {
        return getRideById(rideId);    // Simply returns current ride state
    }
}
```

---

#### `PaymentServiceImpl.java` — Uses Adapter Pattern

```java
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RideRepository rideRepository;
    private final PaymentGateway paymentGateway;    // ADAPTER PATTERN — interface, not concrete

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RideRepository rideRepository,
                              @Qualifier("razorpayAdapter") PaymentGateway paymentGateway) {
        // @Qualifier tells Spring which PaymentGateway implementation to inject
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Payment processPayment(Long rideId, PaymentMode mode) {
        // Step 1: Get the ride to know the fare amount
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found: " + rideId));

        // Step 2: Create a Payment object
        Payment payment = new Payment(ride.getFare(), mode, ride);

        // Step 3: ADAPTER PATTERN — call the payment gateway
        // Our code sends rupees → Adapter converts to paise → calls Razorpay SDK
        boolean success = paymentGateway.processPayment(payment.getAmount(), mode.name());

        // Step 4: Update payment status based on gateway response
        if (success) {
            payment.processPayment();   // Sets status = SUCCESS + timestamp
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new PaymentFailedException("Payment failed for ride: " + rideId);
        }

        return paymentRepository.save(payment);
    }
}
```

---

### 3.8 Controller Layer

#### `BookingController.java`

```java
@RestController                            // Returns JSON (not HTML views)
@RequestMapping("/api/bookings")           // Base URL: /api/bookings
public class BookingController {

    private final BookingService bookingService; // DIP: depends on interface

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping                           // POST /api/bookings
    public ResponseEntity<Booking> bookRide(@RequestBody BookRideRequest request) {
        // @RequestBody: Spring deserializes JSON body → BookRideRequest object
        return ResponseEntity.ok(bookingService.bookRide(request));
        // ResponseEntity.ok() wraps response with HTTP 200 status
    }

    @PutMapping("/{id}/cancel")            // PUT /api/bookings/5/cancel
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        // @PathVariable: extracts "5" from the URL
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/{id}")                   // GET /api/bookings/5
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/rider/{riderId}")        // GET /api/bookings/rider/1
    public ResponseEntity<List<Booking>> getBookingsByRider(@PathVariable Long riderId) {
        return ResponseEntity.ok(bookingService.getBookingsByRider(riderId));
    }
}
```

#### `RideController.java`

```java
@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final FareEstimator fareEstimator;

    @PutMapping("/{id}/accept")            // PUT /api/rides/1/accept?driverId=1
    public ResponseEntity<Ride> acceptRide(@PathVariable Long id,
                                           @RequestParam Long driverId) {
        // @RequestParam: extracts "driverId=1" from URL query string
        return ResponseEntity.ok(rideService.acceptRide(id, driverId));
    }

    @PutMapping("/{id}/start")             // PUT /api/rides/1/start
    public ResponseEntity<Ride> startRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.startRide(id));
    }

    @PutMapping("/{id}/complete")          // PUT /api/rides/1/complete
    public ResponseEntity<Ride> completeRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }

    @GetMapping("/estimate")               // GET /api/rides/estimate?distance=15
    public ResponseEntity<List<RideOption>> estimateFare(@RequestParam double distance) {
        return ResponseEntity.ok(fareEstimator.sortRideOptions(distance));
    }
}
```

---

### 3.9 DTO Layer

**File: `BookRideRequest.java`** — Data Transfer Object for booking requests

```java
public class BookRideRequest {
    private Long riderId;                  // Which rider is booking
    private double pickupLat;              // Pickup GPS latitude
    private double pickupLng;              // Pickup GPS longitude
    private String pickupAddress;          // "MG Road, Bangalore"
    private double dropLat;
    private double dropLng;
    private String dropAddress;
    private RideType rideType;             // ECONOMY, PREMIUM, or SHARED
    // + getters and setters
}
```

**Why use a DTO instead of the entity directly?**
- The incoming JSON has different fields than the entity (e.g., flat lat/lng vs nested Location).
- DTOs protect the entity from external input — validation happens before creating entities.

---

### 3.10 Exception Handling

```java
// Custom exceptions
public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) { super(message); }
}

public class DriverNotAvailableException extends RuntimeException {
    public DriverNotAvailableException(String message) { super(message); }
}

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) { super(message); }
}

// Global handler — catches all exceptions and returns structured JSON
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RideNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidState(IllegalStateException ex) {
        // This catches State Pattern violations (e.g., starting a completed ride)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "INVALID_STATE", "message", ex.getMessage()));
    }
}
```

**How it works:** When `RideServiceImpl` throws an `IllegalStateException` (from State Pattern), `GlobalExceptionHandler` catches it and returns HTTP 409 Conflict with a JSON error body instead of a stack trace.

---

### 3.11 Design Patterns — Full Code

#### STRATEGY PATTERN — Fare Calculation

```java
// INTERFACE — defines the algorithm contract
public interface FareStrategy {
    double calculateFare(double distance, double duration);
    String getStrategyName();
}

// CONCRETE STRATEGY 1 — Economy: ₹50 base + ₹12/km
@Component("economyFareStrategy")
public class EconomyFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 50.0;
    private static final double COST_PER_KM = 12.0;
    private static final double COST_PER_MINUTE = 1.5;

    @Override
    public double calculateFare(double distance, double duration) {
        return BASE_FARE + (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
    }
}

// CONCRETE STRATEGY 2 — Premium: ₹100 base + ₹20/km
@Component("premiumFareStrategy")
public class PremiumFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 100.0;
    private static final double COST_PER_KM = 20.0;
    private static final double COST_PER_MINUTE = 3.0;

    @Override
    public double calculateFare(double distance, double duration) {
        return BASE_FARE + (distance * COST_PER_KM) + (duration * COST_PER_MINUTE);
    }
}

// CONCRETE STRATEGY 3 — Shared: ₹30 base + ₹8/km (cheapest)
@Component("sharedFareStrategy")
public class SharedFareStrategy implements FareStrategy { /* similar pattern */ }

// CONCRETE STRATEGY 4 — Surge: wraps any strategy with 1.5x multiplier
@Component("surgeFareStrategy")
public class SurgePricingStrategy implements FareStrategy {
    private final FareStrategy baseStrategy;       // Wraps another strategy
    private final double surgeMultiplier = 1.5;

    @Override
    public double calculateFare(double distance, double duration) {
        double baseFare = baseStrategy.calculateFare(distance, duration);
        return baseFare * surgeMultiplier;          // ₹100 base becomes ₹150
    }
}
```

---

#### FACTORY PATTERN — Ride Creation

```java
@Component
public class RideFactory {
    public Ride createRide(RideType type, Location pickup, Location drop) {
        switch (type) {
            case SHARED:
                SharedRide shared = new SharedRide(pickup, drop);
                shared.setMaxPassengers(4);
                return shared;              // Returns SharedRide AS Ride (LSP)

            case PREMIUM:
                return new Ride(pickup, drop, RideType.PREMIUM);

            case ECONOMY:
            default:
                return new Ride(pickup, drop, RideType.ECONOMY);
        }
    }
}
```

---

#### ADAPTER PATTERN — Payment Gateway

```java
// TARGET INTERFACE — what our system expects
public interface PaymentGateway {
    boolean processPayment(double amount, String method); // Takes rupees
    boolean refundPayment(double amount);
    String getGatewayName();
}

// ADAPTER — translates our interface to Razorpay's interface
@Component("razorpayAdapter")
public class RazorpayAdapter implements PaymentGateway {
    private final RazorpaySDK razorpaySDK = new RazorpaySDK();

    @Override
    public boolean processPayment(double amount, String method) {
        int paiseAmount = (int) (amount * 100);    // ADAPTATION: ₹112.22 → 11222 paise
        String orderId = razorpaySDK.createOrder(paiseAmount, "INR");
        return razorpaySDK.capturePayment(orderId);
    }

    // ADAPTEE — simulates external Razorpay SDK (incompatible interface)
    private static class RazorpaySDK {
        public String createOrder(int amountInPaise, String currency) {
            return "rzp_order_" + System.currentTimeMillis();
        }
        public boolean capturePayment(String orderId) {
            return true;
        }
    }
}
```

---

#### FACADE PATTERN — Multi-Modal Trip

```java
@Component
public class MultiModalTripFacade {
    private final FareEstimator fareEstimator;       // Sub-system 1
    private final MetroServiceAdapter metroAdapter;  // Sub-system 2

    // ONE method hides 3 complex sub-system calls
    public MultiModalTripPlan planTrip(Location origin, String fromStation,
                                       String toStation, Location destination) {
        MultiModalTripPlan plan = new MultiModalTripPlan(System.currentTimeMillis());

        // Leg 1: Cab from home → metro station (uses FareEstimator)
        double cabFare1 = fareEstimator.calculateCabFare(3.0, "economy");
        plan.addLeg("CAB_TO_METRO", cabFare1, 9, origin.getAddress() + " → " + fromStation);

        // Leg 2: Metro ride (uses MetroServiceAdapter)
        double metroFare = fareEstimator.calculateMetroFare(fromStation, toStation);
        int metroTime = metroAdapter.getMetroEstimatedTime(fromStation, toStation);
        plan.addLeg("METRO", metroFare, metroTime, fromStation + " → " + toStation);

        // Leg 3: Cab from metro station → office (uses FareEstimator)
        double cabFare2 = fareEstimator.calculateCabFare(2.5, "economy");
        plan.addLeg("CAB_FROM_METRO", cabFare2, 7, toStation + " → " + destination.getAddress());

        plan.calculateTotals();            // Sum all fares and times
        return plan;
    }
}
```

---

#### STATE PATTERN — Ride Lifecycle

```java
// STATE INTERFACE
public interface RideState {
    void accept(Ride ride);
    void start(Ride ride);
    void complete(Ride ride);
    void cancel(Ride ride);
}

// STATE: REQUESTED — only accept() and cancel() are valid
public class RequestedState implements RideState {
    @Override
    public void accept(Ride ride) {
        ride.setRideStatus(RideStatus.DRIVER_ASSIGNED);     // ✅ Valid
    }
    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Cannot start — no driver assigned"); // ❌
    }
    @Override
    public void complete(Ride ride) {
        throw new IllegalStateException("Cannot complete — ride not started"); // ❌
    }
    @Override
    public void cancel(Ride ride) {
        ride.setRideStatus(RideStatus.CANCELLED);           // ✅ Valid
    }
}

// STATE: DRIVER_ASSIGNED — only start() and cancel() are valid
public class DriverAssignedState implements RideState {
    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Driver already assigned");  // ❌
    }
    @Override
    public void start(Ride ride) {
        ride.setRideStatus(RideStatus.IN_PROGRESS);         // ✅ Valid
    }
}

// STATE: COMPLETED — all transitions blocked (terminal state)
public class CompletedState implements RideState {
    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride already completed");   // ❌
    }
    // All methods throw exceptions — you cannot change a completed ride
}

// STATE FACTORY — maps enum to state object
public class RideStateFactory {
    private static final Map<RideStatus, RideState> STATE_MAP = new HashMap<>();
    static {
        STATE_MAP.put(RideStatus.REQUESTED,      new RequestedState());
        STATE_MAP.put(RideStatus.DRIVER_ASSIGNED, new DriverAssignedState());
        STATE_MAP.put(RideStatus.IN_PROGRESS,    new InProgressState());
        STATE_MAP.put(RideStatus.COMPLETED,      new CompletedState());
        STATE_MAP.put(RideStatus.CANCELLED,      new CancelledState());
    }

    public static RideState getState(RideStatus status) {
        return STATE_MAP.get(status);
    }
}
```

---

## 4. SOLID Principles Explained

| Principle | Where Applied | How |
|---|---|---|
| **SRP** | 5 service classes | Each service handles only ONE domain (Rider, Driver, Booking, Ride, Payment) |
| **OCP** | FareStrategy | Add new pricing by creating new class — zero changes to existing code |
| **LSP** | SharedRide extends Ride | SharedRide works everywhere Ride is expected — RideFactory returns Ride reference |
| **DIP** | Controllers + PaymentService | Controllers depend on service INTERFACES. PaymentServiceImpl depends on PaymentGateway INTERFACE |

---

## 5. Design Patterns Explained

| Pattern | Type | Problem Solved | Key File |
|---|---|---|---|
| **Strategy** | Behavioral | Different algorithms for Economy/Premium/Shared fare calculation | `pattern/strategy/FareStrategy.java` |
| **Factory** | Creational | Create Ride or SharedRide based on type without exposing creation logic | `pattern/factory/RideFactory.java` |
| **Adapter** | Structural | Translate our interface (rupees) to external Razorpay SDK (paise) | `pattern/adapter/RazorpayAdapter.java` |
| **Facade** | Structural | Simplify complex multi-modal trip planning (3 services) into 1 call | `pattern/facade/MultiModalTripFacade.java` |
| **State** | Behavioral | Manage valid ride transitions (can't start a completed ride) | `pattern/state/RideState.java` |

---

## 6. UML Diagrams

See `UML_Mermaid_Code.md` for 11 complete Mermaid diagrams ready to paste into draw.io.

---

## 7. REST API Reference

| Method | Endpoint | Pattern Used | Use Case |
|---|---|---|---|
| `POST` | `/api/bookings` | Factory + Strategy | Book a ride |
| `PUT` | `/api/rides/{id}/accept?driverId=1` | State | Accept ride |
| `PUT` | `/api/rides/{id}/start` | State | Start ride |
| `PUT` | `/api/rides/{id}/complete` | State | Complete ride |
| `PUT` | `/api/rides/{id}/cancel` | State | Cancel ride |
| `GET` | `/api/rides/{id}/track` | — | Track ride |
| `GET` | `/api/rides/estimate?distance=15` | Strategy | Fare options |
| `POST` | `/api/payments/ride/{id}?mode=CARD` | Adapter | Pay for ride |
| `GET` | `/api/trips/multimodal?...` | Facade | Plan multi-modal trip |

---

## 8. Demo Flow

```
Step 1: Book     → POST /api/bookings            (Factory creates Ride, Strategy calculates fare)
Step 2: Accept   → PUT  /api/rides/1/accept       (State: REQUESTED → DRIVER_ASSIGNED)
Step 3: Start    → PUT  /api/rides/1/start        (State: DRIVER_ASSIGNED → IN_PROGRESS)
Step 4: Complete → PUT  /api/rides/1/complete      (State: IN_PROGRESS → COMPLETED)
Step 5: Pay      → POST /api/payments/ride/1      (Adapter: rupees → paise → Razorpay)
Step 6: Plan     → GET  /api/trips/multimodal     (Facade: cab + metro + cab = 1 call)
Step 7: Estimate → GET  /api/rides/estimate       (Strategy: compare Economy vs Premium vs Shared)
```

**Total: 64 Java source files · 4 SOLID principles · 5 Design Patterns · 20+ REST APIs**
