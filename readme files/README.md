# 🚗 Online Ride Booking System

## OOAD Mini Project — Full Implementation Documentation

> **Course**: Object-Oriented Analysis and Design (OOAD)  
> **Tech Stack**: Spring Boot 3.4 + JPA/Hibernate + H2 Database + React + Tailwind CSS  
> **Design Patterns**: Strategy, Factory, Adapter, Facade, State  
> **SOLID Principles**: SRP, OCP, LSP, DIP  

---

## 📋 Table of Contents

1. [Project Overview](#1-project-overview)
2. [System Architecture](#2-system-architecture)
3. [How to Run](#3-how-to-run)
4. [Package Structure](#4-package-structure)
5. [Domain Model — Full Explanation](#5-domain-model--full-explanation)
6. [SOLID Design Principles — With Code Mapping](#6-solid-design-principles--with-code-mapping)
7. [Design Patterns — Detailed Explanation & UML](#7-design-patterns--detailed-explanation--uml)
8. [REST API Reference](#8-rest-api-reference)
9. [Database Schema](#9-database-schema)
10. [Frontend Architecture](#10-frontend-architecture)
11. [Demo Flow](#11-demo-flow)

---

## 1. Project Overview

The **Online Ride Booking System** is a full-stack web application that simulates ride-hailing platforms like Uber/Ola. The project demonstrates **Object-Oriented Analysis and Design** concepts through a professional Spring Boot backend with a React frontend.

### What the System Does

| Use Case | Description |
|---|---|
| **Book Ride** | Rider selects pickup/drop, chooses ride type → fare is calculated → ride is created |
| **Accept Ride** | Driver picks up a ride request → status changes to DRIVER_ASSIGNED |
| **Start Ride** | Driver begins the trip → status changes to IN_PROGRESS |
| **Complete Ride** | Driver finishes the trip → status changes to COMPLETED |
| **Cancel Ride** | Either party cancels → status changes to CANCELLED |
| **Process Payment** | After ride completion → payment via Card/UPI/Wallet/Cash |
| **Track Ride** | Real-time ride status tracking |
| **Fare Estimation** | Compare Economy/Premium/Shared prices before booking |
| **Multi-Modal Trip** | Plan trips combining Cab + Metro + Cab |

---

## 2. System Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                    │
│         http://localhost:5173  →  Proxy to :8080             │
└──────────────────────┬───────────────────────────────────────┘
                       │ HTTP REST (JSON)
┌──────────────────────▼───────────────────────────────────────┐
│                  CONTROLLER LAYER (REST APIs)                │
│      RiderController, DriverController, BookingController,   │
│      RideController, PaymentController, TripController       │
├──────────────────────────────────────────────────────────────┤
│                   SERVICE LAYER (Business Logic)             │
│  RiderServiceImpl, DriverServiceImpl, BookingServiceImpl,    │
│  RideServiceImpl, PaymentServiceImpl                         │
│  + Design Patterns: Strategy, Factory, State, Adapter, Facade│
├──────────────────────────────────────────────────────────────┤
│                  REPOSITORY LAYER (Data Access)              │
│  RiderRepository, DriverRepository, BookingRepository,       │
│  RideRepository, VehicleRepository, PaymentRepository        │
├──────────────────────────────────────────────────────────────┤
│                   DATABASE (H2 In-Memory)                    │
│         Tables: riders, drivers, vehicles, rides,            │
│                 bookings, payments                           │
└──────────────────────────────────────────────────────────────┘
```

**Why 4 Layers?**
- **Controller** → Handles HTTP requests, validates input, returns JSON
- **Service** → All business logic, pattern orchestration, validation rules
- **Repository** → SQL queries abstracted by Spring Data JPA
- **Model** → JPA entities mapping to database tables

This follows the **Separation of Concerns** principle. Each layer has a single responsibility and communicates only with adjacent layers.

---

## 3. How to Run

### Backend (Spring Boot on port 8080)
```bash
cd mini_project
mvn spring-boot:run
```

### Frontend (React on port 5173)
```bash
cd mini_project/frontend
npm install
npm run dev
```

### Access Points
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:ridebookingdb`)

---

## 4. Package Structure

```
src/main/java/com/ridebooking/
│
├── RideBookingApplication.java          ← Spring Boot entry point
│
├── model/                               ← DOMAIN MODEL (JPA Entities)
│   ├── User.java                        ← Abstract base class (@MappedSuperclass)
│   ├── Rider.java                       ← Extends User
│   ├── Driver.java                      ← Extends User
│   ├── Vehicle.java                     ← 1-to-1 with Driver
│   ├── Booking.java                     ← Links Rider to Ride
│   ├── Ride.java                        ← Core entity (SINGLE_TABLE inheritance)
│   ├── SharedRide.java                  ← Extends Ride
│   ├── Location.java                    ← @Embeddable for pickup/drop
│   ├── Payment.java                     ← 1-to-1 with Ride
│   ├── RideOption.java                  ← DTO for fare estimates
│   └── enums/                           ← Status enumerations
│
├── repository/                          ← DATA ACCESS (Spring Data JPA)
├── service/                             ← BUSINESS LOGIC (Interfaces)
│   └── impl/                            ← Service Implementations
├── controller/                          ← REST API ENDPOINTS
│
├── pattern/                             ← DESIGN PATTERNS
│   ├── strategy/                        ← Fare calculation strategies
│   ├── factory/                         ← Ride creation factory
│   ├── adapter/                         ← Payment gateway adapter
│   ├── facade/                          ← Multi-modal trip facade
│   └── state/                           ← Ride lifecycle states
│
├── dto/                                 ← Data Transfer Objects
├── exception/                           ← Custom exceptions + Global handler
└── config/                              ← Data initializer
```

---

## 5. Domain Model — Full Explanation

### 5.1 Class Descriptions

#### `User` (Abstract Base Class)
```java
@MappedSuperclass
public abstract class User {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    public abstract void login();
    public abstract void logout();
}
```
- **Why abstract?** Users never exist alone — they are always either a Rider or a Driver.
- **Why `@MappedSuperclass`?** Rider and Driver each get their own database table (`riders`, `drivers`) but share the same columns (userId, name, email, phone).

#### `Rider` (Extends User)
```java
@Entity @Table(name = "riders")
public class Rider extends User {
    private float riderRating;
    @OneToMany(mappedBy = "rider") // UML: Rider → Booking (1:N)
    private List<Booking> bookings;
}
```
- **Relationship**: One Rider can have MANY Bookings (`@OneToMany`).
- **Methods**: `requestRide()`, `chooseRideOption()` — delegates to service layer.

#### `Driver` (Extends User)
```java
@Entity @Table(name = "drivers")
public class Driver extends User {
    private float driverRating;
    private DriverStatus availabilityStatus;
    @OneToOne // UML: Driver → Vehicle (1:1, "owns")
    private Vehicle vehicle;
}
```
- **Relationship**: One Driver owns exactly ONE Vehicle (`@OneToOne`).
- **Methods**: `acceptRide()`, `startRide()`, `endRide()` — update `availabilityStatus`.

#### `Ride` (Core Entity with Inheritance)
```java
@Entity @Table(name = "rides")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ride_category")
@DiscriminatorValue("STANDARD")
public class Ride {
    private Long rideId;
    private double fare;
    private RideStatus rideStatus;
    private RideType rideType;
    @Embedded private Location pickupLocation;  // UML: Ride → Location (1:1)
    @Embedded private Location dropLocation;    // UML: Ride → Location (1:1)
    @ManyToOne  private Driver driver;          // UML: Ride → Driver (N:1)
}
```
- **SINGLE_TABLE Inheritance**: Both `Ride` and `SharedRide` are stored in the SAME table (`rides`), with a discriminator column `ride_category` ("STANDARD" or "SHARED").
- **Embedded Locations**: `@AttributeOverrides` renames columns so pickup and drop don't collide.

#### `SharedRide` (Extends Ride — LSP Compliant)
```java
@Entity @DiscriminatorValue("SHARED")
public class SharedRide extends Ride {
    private int maxPassengers;
    private int currentPassengers;
    public boolean addPassenger() { ... }
    public double splitFare() { return getFare() / currentPassengers; }
}
```
- **LSP**: `SharedRide` can substitute `Ride` anywhere — `RideFactory` returns `Ride` reference, and the caller doesn't know if it's a `SharedRide`.

#### `Booking` (Links Rider to Ride)
```java
@Entity
public class Booking {
    @ManyToOne private Rider rider;   // UML: Rider → Booking (N:1)
    @OneToOne  private Ride ride;     // UML: Booking → Ride (1:1)
    private BookingStatus bookingStatus;
}
```
- **Why separate from Ride?** A Booking is a *business transaction*. A Ride is a *physical trip*. Separating them follows SRP.

#### `Payment` (1-to-1 with Ride)
```java
@Entity
public class Payment {
    private double amount;
    private PaymentMode paymentMode;   // CARD, UPI, WALLET, CASH
    private PaymentStatus paymentStatus;
    @OneToOne private Ride ride;       // UML: Ride → Payment (1:1)
}
```

### 5.2 Relationships Summary

```
User (abstract)
 ├── Rider ──(1:N)──→ Booking ──(1:1)──→ Ride
 │                                         ├── Location (pickup, embedded)
 │                                         ├── Location (drop, embedded)
 │                                         ├── Driver (N:1)
 │                                         └── Payment (1:1)
 └── Driver ──(1:1)──→ Vehicle

Ride ──(inheritance)──→ SharedRide
```

---

## 6. SOLID Design Principles — With Code Mapping

### 6.1 Single Responsibility Principle (SRP)

> **Definition**: A class should have only ONE reason to change — it should have only ONE job.

**How we applied it:**

| Class | Single Responsibility |
|---|---|
| `RiderServiceImpl` | Only manages rider registration, retrieval, and rating |
| `DriverServiceImpl` | Only manages driver availability and retrieval |
| `BookingServiceImpl` | Only manages booking creation and cancellation |
| `RideServiceImpl` | Only manages ride state transitions |
| `PaymentServiceImpl` | Only manages payment processing and refunds |

**Code Example — Before SRP (Bad Design):**
```java
// ❌ GOD CLASS — does everything
class RideManager {
    void registerRider() { ... }
    void registerDriver() { ... }
    void bookRide() { ... }
    void processPayment() { ... }
    void sendNotification() { ... }
}
```

**Code Example — After SRP (Our Design):**
```java
// ✅ Each service has ONE responsibility
@Service class RiderServiceImpl implements RiderService { ... }
@Service class DriverServiceImpl implements DriverService { ... }
@Service class BookingServiceImpl implements BookingService { ... }
@Service class RideServiceImpl implements RideService { ... }
@Service class PaymentServiceImpl implements PaymentService { ... }
```

### 6.2 Open/Closed Principle (OCP)

> **Definition**: Classes should be OPEN for extension but CLOSED for modification.

**How we applied it:** The **Strategy Pattern** for fare calculation.

```java
// ✅ FareStrategy interface — CLOSED (never changes)
public interface FareStrategy {
    double calculateFare(double distance, double duration);
}

// ✅ Add new pricing by EXTENDING — creating new class, zero changes to existing
@Component("economyFareStrategy")
public class EconomyFareStrategy implements FareStrategy { ... }

@Component("premiumFareStrategy")
public class PremiumFareStrategy implements FareStrategy { ... }

@Component("sharedFareStrategy")
public class SharedFareStrategy implements FareStrategy { ... }

// 🆕 To add a new ride type (e.g., BIKE), just add:
@Component("bikeFareStrategy")
public class BikeFareStrategy implements FareStrategy { ... }
// No existing code changes needed!
```

### 6.3 Liskov Substitution Principle (LSP)

> **Definition**: Subclasses must be substitutable for their base classes without breaking behavior.

**How we applied it:** `SharedRide extends Ride`.

```java
// ✅ RideFactory returns Ride (base class reference)
public class RideFactory {
    public Ride createRide(RideType type, Location pickup, Location drop) {
        switch (type) {
            case SHARED:
                return new SharedRide(pickup, drop);  // Returns SharedRide AS Ride
            default:
                return new Ride(pickup, drop, type);   // Returns Ride
        }
    }
}

// ✅ BookingServiceImpl works with Ride — doesn't know about SharedRide
Ride ride = rideFactory.createRide(request.getRideType(), pickup, drop);
ride.setFare(fare);           // Works for both Ride and SharedRide
booking.setRide(ride);        // Polymorphism — LSP in action
```

**Why this satisfies LSP:** The `BookingServiceImpl` calls `ride.setFare()`, `ride.getDistance()`, etc. — all these work correctly whether the `ride` is a `Ride` or a `SharedRide`. The subclass doesn't break any parent behavior.

### 6.4 Dependency Inversion Principle (DIP)

> **Definition**: High-level modules should NOT depend on low-level modules. Both should depend on ABSTRACTIONS (interfaces).

**How we applied it:**

```java
// ✅ Controller depends on SERVICE INTERFACE (not implementation)
@RestController
public class BookingController {
    private final BookingService bookingService;  // ← Interface, not BookingServiceImpl
    
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;     // Spring injects BookingServiceImpl
    }
}

// ✅ PaymentServiceImpl depends on GATEWAY INTERFACE (not Razorpay directly)
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentGateway paymentGateway;  // ← Interface, not RazorpayAdapter
    
    @Autowired
    public PaymentServiceImpl(@Qualifier("razorpayAdapter") PaymentGateway gateway) {
        this.paymentGateway = gateway;            // Spring injects RazorpayAdapter
    }
}
```

**DIP Dependency Flow:**
```
Controller → depends on → Service Interface ← implemented by ← ServiceImpl
                                                                    ↓
                                           depends on → PaymentGateway Interface
                                                                    ↑
                                                    implemented by ← RazorpayAdapter
```

---

## 7. Design Patterns — Detailed Explanation & UML

### 7.1 Strategy Pattern (Behavioral)

**What it solves:** Different ride types need different fare calculation algorithms. Without Strategy, you'd need `if-else` chains inside one method — violating OCP.

**UML Diagram:**
```
┌─────────────────────────┐
│   <<interface>>          │
│    FareStrategy          │
├─────────────────────────┤
│ + calculateFare()        │
│ + getStrategyName()      │
└─────────┬───────────────┘
          │ implements
    ┌─────┼──────────────┐
    │     │              │
    ▼     ▼              ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌──────────────┐
│Economy │ │Premium │ │Shared  │ │SurgePricing  │
│Strategy│ │Strategy│ │Strategy│ │Strategy      │
├────────┤ ├────────┤ ├────────┤ ├──────────────┤
│₹50+12/km│ │₹100+20/km│ │₹30+8/km│ │base × 1.5x  │
└────────┘ └────────┘ └────────┘ └──────────────┘
```

**Code Mapping:**

| UML Element | Java Code | File |
|---|---|---|
| `FareStrategy` interface | `public interface FareStrategy` | `pattern/strategy/FareStrategy.java` |
| `EconomyFareStrategy` | `@Component("economyFareStrategy")` | `pattern/strategy/EconomyFareStrategy.java` |
| `PremiumFareStrategy` | `@Component("premiumFareStrategy")` | `pattern/strategy/PremiumFareStrategy.java` |
| `SharedFareStrategy` | `@Component("sharedFareStrategy")` | `pattern/strategy/SharedFareStrategy.java` |
| Context (uses strategy) | `BookingServiceImpl` with `Map<String, FareStrategy>` | `service/impl/BookingServiceImpl.java` |

**How Spring makes it work:** Spring auto-collects ALL beans implementing `FareStrategy` and injects them as a `Map<String, FareStrategy>` where the key is the bean name (e.g., `"economyFareStrategy"`). The service picks the right strategy by ride type.

---

### 7.2 Factory Pattern (Creational)

**What it solves:** The system needs to create different types of Ride objects (`Ride` or `SharedRide`) based on user input. The Factory encapsulates this decision — the client doesn't know which subclass was created.

**UML Diagram:**
```
                    ┌──────────────┐
                    │ RideFactory   │
                    ├──────────────┤
                    │ + createRide()│
                    └──────┬───────┘
                           │ creates
              ┌────────────┼────────────┐
              ▼            ▼            ▼
        ┌──────────┐ ┌──────────┐ ┌──────────────┐
        │   Ride   │ │   Ride   │ │  SharedRide  │
        │(ECONOMY) │ │(PREMIUM) │ │  extends Ride│
        └──────────┘ └──────────┘ └──────────────┘
```

**Code Mapping:**

```java
// File: pattern/factory/RideFactory.java
@Component
public class RideFactory {
    public Ride createRide(RideType type, Location pickup, Location drop) {
        switch (type) {
            case SHARED:
                SharedRide shared = new SharedRide(pickup, drop);
                shared.setMaxPassengers(4);
                return shared;                          // Returns Ride reference (LSP)
            case PREMIUM:
                return new Ride(pickup, drop, RideType.PREMIUM);
            case ECONOMY:
            default:
                return new Ride(pickup, drop, RideType.ECONOMY);
        }
    }
}

// Used in: service/impl/BookingServiceImpl.java (Line 63)
Ride ride = rideFactory.createRide(request.getRideType(), pickup, drop);
```

---

### 7.3 Adapter Pattern (Structural)

**What it solves:** Our system needs to process payments through Razorpay, but Razorpay's SDK has a DIFFERENT interface (uses paise, different method names). The Adapter translates between our interface and Razorpay's.

**UML Diagram:**
```
┌────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│    CLIENT       │     │   <<interface>>   │     │    ADAPTEE       │
│ PaymentService  │────▶│  PaymentGateway   │     │   RazorpaySDK    │
│ Impl            │     ├──────────────────┤     ├──────────────────┤
│                 │     │+processPayment() │     │+createOrder()    │
│ Uses rupees     │     │+refundPayment()  │     │+capturePayment() │
│ and our types   │     │+getGatewayName() │     │ Uses PAISE       │
└────────────────┘     └────────┬─────────┘     └────────▲─────────┘
                                │ implements              │ wraps
                       ┌────────▼─────────┐              │
                       │ RazorpayAdapter   │──────────────┘
                       ├──────────────────┤
                       │ Converts rupees   │
                       │ to paise, calls   │
                       │ SDK methods       │
                       └──────────────────┘
```

**Code Mapping:**

| UML Role | Java Class | What it Does |
|---|---|---|
| **Target** (our interface) | `PaymentGateway` | Defines `processPayment(double amount, String method)` |
| **Adapter** (translator) | `RazorpayAdapter` | Converts rupees → paise, calls SDK |
| **Adaptee** (external SDK) | `RazorpaySDK` (inner class) | Has `createOrder(int paise, String currency)` |
| **Client** (uses target) | `PaymentServiceImpl` | Depends on `PaymentGateway` interface (DIP) |

```java
// File: pattern/adapter/RazorpayAdapter.java
@Component("razorpayAdapter")
public class RazorpayAdapter implements PaymentGateway {
    private final RazorpaySDK razorpaySDK = new RazorpaySDK();

    @Override
    public boolean processPayment(double amount, String method) {
        // ADAPTATION: Convert rupees to paise
        int paiseAmount = (int) (amount * 100);
        String orderId = razorpaySDK.createOrder(paiseAmount, "INR");
        return razorpaySDK.capturePayment(orderId);
    }
}
```

There is also a **MetroServiceAdapter** that adapts the `MetroService` API to a standard fare format used by `FareEstimator`.

---

### 7.4 Facade Pattern (Structural)

**What it solves:** Planning a multi-modal trip requires coordinating 3 services (cab fare calculation, metro fare lookup, cab fare again). The Facade hides this complexity — the client calls ONE method.

**UML Diagram:**
```
┌───────────────┐         ┌──────────────────────┐
│    CLIENT      │         │  MultiModalTripFacade │
│ TripController │────────▶│                      │
│                │         │ + planTrip()          │
└───────────────┘         └───────┬──────────────┘
                                  │ coordinates
                    ┌─────────────┼─────────────┐
                    ▼             ▼             ▼
            ┌──────────────┐ ┌──────────┐ ┌──────────────┐
            │FareEstimator │ │MetroService│ │MetroService │
            │              │ │Adapter    │ │             │
            │calculateCab  │ │getMetro   │ │calculateMetro│
            │Fare()        │ │Fare()     │ │Fare()       │
            └──────────────┘ └──────────┘ └──────────────┘
```

**Code Mapping:**

```java
// File: pattern/facade/MultiModalTripFacade.java
@Component
public class MultiModalTripFacade {
    private final FareEstimator fareEstimator;       // Sub-system 1
    private final MetroServiceAdapter metroAdapter;  // Sub-system 2

    public MultiModalTripPlan planTrip(Location origin, String fromStation,
                                       String toStation, Location destination) {
        // Leg 1: Cab from origin to metro station
        double cabFare1 = fareEstimator.calculateCabFare(3.0, "economy");

        // Leg 2: Metro ride (uses Adapter Pattern)
        double metroFare = fareEstimator.calculateMetroFare(fromStation, toStation);

        // Leg 3: Cab from metro station to destination
        double cabFare2 = fareEstimator.calculateCabFare(2.5, "economy");

        // Combine all legs into a single plan
        plan.calculateTotals();
        return plan;
    }
}

// Used in: controller/TripController.java
// Client calls ONE method instead of coordinating 3 services
MultiModalTripPlan plan = tripFacade.planTrip(origin, from, to, dest);
```

---

### 7.5 State Pattern (Behavioral)

**What it solves:** A ride goes through multiple states (REQUESTED → DRIVER_ASSIGNED → IN_PROGRESS → COMPLETED). Each state allows only certain transitions. Without State Pattern, you'd have massive `if-else` chains.

**UML State Machine Diagram:**
```
              ┌──────────┐
              │ REQUESTED │
              └─────┬─────┘
         accept()   │   cancel()
                    ▼       ▼
      ┌─────────────────┐  ┌───────────┐
      │ DRIVER_ASSIGNED  │  │ CANCELLED │
      └────────┬────────┘  └───────────┘
        start()│   cancel()     ▲
               ▼       ▼       │
        ┌──────────────┐       │
        │ IN_PROGRESS   │──────┘
        └───────┬──────┘  cancel()
       complete()│
                ▼
        ┌──────────┐
        │ COMPLETED │
        └──────────┘
```

**UML Class Diagram:**
```
┌─────────────────────┐
│   <<interface>>      │
│     RideState        │
├─────────────────────┤
│ + accept(Ride)       │
│ + start(Ride)        │
│ + complete(Ride)     │
│ + cancel(Ride)       │
└─────────┬───────────┘
          │ implements
  ┌───────┼────────┬──────────┬────────────┬───────────┐
  ▼       ▼        ▼          ▼            ▼           ▼
┌────┐ ┌───────┐ ┌─────────┐ ┌──────────┐ ┌─────────┐
│Req.│ │Driver │ │InProg.  │ │Completed │ │Cancelled│
│State│ │Assign.│ │State    │ │State     │ │State    │
├────┤ ├───────┤ ├─────────┤ ├──────────┤ ├─────────┤
│✅acc│ │❌acc  │ │❌acc    │ │❌ all    │ │❌ all   │
│❌sta│ │✅sta  │ │❌sta    │ │          │ │         │
│❌com│ │❌com  │ │✅com   │ │          │ │         │
│✅can│ │✅can  │ │✅can   │ │          │ │         │
└────┘ └───────┘ └─────────┘ └──────────┘ └─────────┘
```
*(✅ = allowed, ❌ = throws IllegalStateException)*

**Code Mapping:**

```java
// File: service/impl/RideServiceImpl.java (Line 46-47)
public Ride acceptRide(Long rideId, Long driverId) {
    Ride ride = getRideById(rideId);
    
    // STATE PATTERN — get current state and validate transition
    RideState currentState = RideStateFactory.getState(ride.getRideStatus());
    currentState.accept(ride);  // If status is REQUESTED → sets to DRIVER_ASSIGNED
                                // If status is COMPLETED → throws IllegalStateException
    ...
}

// File: pattern/state/RequestedState.java
public class RequestedState implements RideState {
    @Override
    public void accept(Ride ride) {
        ride.setRideStatus(RideStatus.DRIVER_ASSIGNED);  // ✅ Valid transition
    }
    @Override
    public void start(Ride ride) {
        throw new IllegalStateException("Cannot start — no driver assigned");  // ❌
    }
}

// File: pattern/state/CompletedState.java
public class CompletedState implements RideState {
    @Override
    public void accept(Ride ride) {
        throw new IllegalStateException("Ride is already completed");  // ❌ All blocked
    }
    // ... all methods throw exceptions (terminal state)
}
```

---

## 8. REST API Reference

### Riders — `/api/riders`
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/riders` | Register a new rider |
| `GET` | `/api/riders/{id}` | Get rider by ID |
| `GET` | `/api/riders` | List all riders |
| `PUT` | `/api/riders/{id}/rate?rating=4.5` | Rate a rider |

### Drivers — `/api/drivers`
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/drivers` | Register a new driver |
| `GET` | `/api/drivers/{id}` | Get driver by ID |
| `GET` | `/api/drivers/available` | List available drivers |
| `PUT` | `/api/drivers/{id}/login` | Set driver online |
| `PUT` | `/api/drivers/{id}/logout` | Set driver offline |

### Bookings — `/api/bookings`
| Method | Endpoint | Pattern Used |
|---|---|---|
| `POST` | `/api/bookings` | **Factory** + **Strategy** |
| `PUT` | `/api/bookings/{id}/cancel` | — |
| `GET` | `/api/bookings/{id}` | — |
| `GET` | `/api/bookings/rider/{riderId}` | — |

### Rides — `/api/rides`
| Method | Endpoint | Pattern Used |
|---|---|---|
| `PUT` | `/api/rides/{id}/accept?driverId=1` | **State** (REQUESTED → ASSIGNED) |
| `PUT` | `/api/rides/{id}/start` | **State** (ASSIGNED → IN_PROGRESS) |
| `PUT` | `/api/rides/{id}/complete` | **State** (IN_PROGRESS → COMPLETED) |
| `PUT` | `/api/rides/{id}/cancel` | **State** (any → CANCELLED) |
| `GET` | `/api/rides/{id}/track` | — |
| `GET` | `/api/rides/estimate?distance=15` | **Strategy** (returns sorted options) |

### Payments — `/api/payments`
| Method | Endpoint | Pattern Used |
|---|---|---|
| `POST` | `/api/payments/ride/{rideId}?mode=CARD` | **Adapter** (Razorpay) |
| `GET` | `/api/payments/ride/{rideId}` | — |
| `POST` | `/api/payments/{id}/refund` | **Adapter** |

### Trips — `/api/trips`
| Method | Endpoint | Pattern Used |
|---|---|---|
| `GET` | `/api/trips/multimodal?originAddress=...&fromStation=...&toStation=...&destinationAddress=...` | **Facade** |

---

## 9. Database Schema

```sql
riders (user_id PK, name, email UNIQUE, phone, rider_rating)
    ↑
    │ FK: rider_id
bookings (booking_id PK, booking_status, rider_id FK, ride_id FK, booking_time)
    │
    │ FK: ride_id
    ↓
rides (ride_id PK, ride_category, fare, ride_status, ride_type,
       pickup_latitude, pickup_longitude, pickup_address,
       drop_latitude, drop_longitude, drop_address,
       driver_id FK, distance, start_time, end_time,
       max_passengers, current_passengers)
    │
    │ FK: driver_id
    ↓
drivers (user_id PK, name, email UNIQUE, phone, driver_rating,
         availability_status, vehicle_id FK)
    │
    │ FK: vehicle_id
    ↓
vehicles (vehicle_id PK, vehicle_type, vehicle_number UNIQUE, model, color)

payments (payment_id PK, amount, payment_mode, payment_status,
          ride_id FK, transaction_time)
```

---

## 10. Frontend Architecture

```
frontend/src/
├── services/api.js           ← Axios instance + all API methods
├── components/
│   ├── Navbar.jsx            ← Navigation bar with active state
│   ├── UI.jsx                ← Card, Button, Badge, Input, Select, Skeleton
│   └── RideStatusStepper.jsx ← Visual 4-step progress bar (State Pattern)
├── pages/
│   ├── Home.jsx              ← Landing page with pattern showcase
│   ├── BookRide.jsx          ← Booking form + fare estimation
│   ├── RiderDashboard.jsx    ← Rider profiles + booking history
│   ├── DriverDashboard.jsx   ← Driver list + vehicle info + login/logout
│   ├── RideTracking.jsx      ← Ride lifecycle controls + State stepper
│   ├── PaymentPage.jsx       ← Payment processing + Adapter display
│   └── TripPlanner.jsx       ← Multi-modal trip + Facade display
├── App.jsx                   ← Routes (React Router)
├── main.jsx                  ← Entry point
└── index.css                 ← Tailwind CSS theme + animations
```

---

## 11. Demo Flow

### Complete Lifecycle Demo:

```
Step 1: Book Ride   →  POST /api/bookings (Factory + Strategy)
Step 2: Accept Ride →  PUT /api/rides/1/accept?driverId=1 (State: REQUESTED → ASSIGNED)
Step 3: Start Ride  →  PUT /api/rides/1/start (State: ASSIGNED → IN_PROGRESS)
Step 4: Complete    →  PUT /api/rides/1/complete (State: IN_PROGRESS → COMPLETED)
Step 5: Pay         →  POST /api/payments/ride/1?mode=CARD (Adapter)
Step 6: Trip Plan   →  GET /api/trips/multimodal?... (Facade)
Step 7: Estimate    →  GET /api/rides/estimate?distance=15 (Strategy)
```

All steps can be demonstrated via the React frontend at http://localhost:5173.

---

## Summary — Patterns & Principles Mapping

| Pattern/Principle | Category | Where Applied | Key Files |
|---|---|---|---|
| **SRP** | SOLID | 5 separate services, each one responsibility | `service/impl/*.java` |
| **OCP** | SOLID | Add new fare strategies without changing existing code | `pattern/strategy/*.java` |
| **LSP** | SOLID | `SharedRide` substitutes `Ride` transparently | `model/Ride.java`, `SharedRide.java` |
| **DIP** | SOLID | Controllers depend on service interfaces, not implementations | All controllers, `PaymentServiceImpl` |
| **Strategy** | Behavioral | Fare calculation with 4 interchangeable algorithms | `pattern/strategy/` |
| **Factory** | Creational | Creates `Ride` or `SharedRide` based on type | `pattern/factory/RideFactory.java` |
| **Adapter** | Structural | Adapts Razorpay SDK and Metro API to our interfaces | `pattern/adapter/` |
| **Facade** | Structural | Simplifies multi-modal trip planning into one call | `pattern/facade/MultiModalTripFacade.java` |
| **State** | Behavioral | Manages valid ride lifecycle transitions | `pattern/state/` |
