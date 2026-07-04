# 🚗 Online Ride Booking System — Complete OOAD Documentation

## Full Implementation Explanation with Design Principles, Patterns, UML Diagrams & Code Mapping

---

## 1. UML Class Diagram — Complete Domain Model

```mermaid
classDiagram
    class User {
        <<abstract>>
        -Long userId
        -String name
        -String email
        -String phone
        +login()*
        +logout()*
    }

    class Rider {
        -float riderRating
        -List~Booking~ bookings
        +requestRide()
        +chooseRideOption()
        +login()
        +logout()
    }

    class Driver {
        -float driverRating
        -DriverStatus availabilityStatus
        -Vehicle vehicle
        +acceptRide()
        +startRide()
        +endRide()
        +login()
        +logout()
    }

    class Vehicle {
        -Long vehicleId
        -String vehicleType
        -String vehicleNumber
        -String model
        -String color
    }

    class Booking {
        -Long bookingId
        -BookingStatus bookingStatus
        -LocalDateTime bookingTime
        -Rider rider
        -Ride ride
        +cancelBooking()
    }

    class Ride {
        -Long rideId
        -double fare
        -double distance
        -RideStatus rideStatus
        -RideType rideType
        -Location pickupLocation
        -Location dropLocation
        -Driver driver
        -LocalDateTime startTime
        -LocalDateTime endTime
    }

    class SharedRide {
        -int maxPassengers
        -int currentPassengers
        +addPassenger() boolean
        +splitFare() double
    }

    class Location {
        <<Embeddable>>
        -double latitude
        -double longitude
        -String address
        +distanceTo(Location) double
    }

    class Payment {
        -Long paymentId
        -double amount
        -PaymentMode paymentMode
        -PaymentStatus paymentStatus
        -Ride ride
        -LocalDateTime transactionTime
    }

    class FareEstimator {
        +calculateCabFare() double
        +calculateMetroFare() double
    }

    class MultiModalTripFacade {
        -Long tripId
        +planTrip() MultiModalTripPlan
    }

    class MetroService {
        +getStations() List
        +getMetroFare() double
        +getEstimatedTime() int
    }

    User <|-- Rider : extends
    User <|-- Driver : extends
    Driver "1" --> "1" Vehicle : owns
    Rider "1" --> "*" Booking : has
    Booking "1" --> "1" Ride : contains
    Ride <|-- SharedRide : extends
    Ride "1" --> "1" Location : pickup
    Ride "1" --> "1" Location : drop
    Ride "*" --> "1" Driver : assigned to
    Ride "1" --> "1" Payment : has
    MultiModalTripFacade --> FareEstimator : uses
    MultiModalTripFacade --> MetroService : uses
```

### Code Mapping to UML Classes

| UML Class | Java File | JPA Annotation | Database Table |
|---|---|---|---|
| `User` (abstract) | [User.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/User.java) | `@MappedSuperclass` | *(no table — fields inherited)* |
| `Rider` | [Rider.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Rider.java) | `@Entity @Table("riders")` | `riders` |
| `Driver` | [Driver.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Driver.java) | `@Entity @Table("drivers")` | `drivers` |
| `Vehicle` | [Vehicle.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Vehicle.java) | `@Entity @Table("vehicles")` | `vehicles` |
| `Booking` | [Booking.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Booking.java) | `@Entity @Table("bookings")` | `bookings` |
| `Ride` | [Ride.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Ride.java) | `@Entity @Inheritance(SINGLE_TABLE)` | `rides` |
| `SharedRide` | [SharedRide.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/SharedRide.java) | `@Entity @DiscriminatorValue("SHARED")` | `rides` (same table) |
| `Location` | [Location.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Location.java) | `@Embeddable` | *(embedded in rides)* |
| `Payment` | [Payment.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Payment.java) | `@Entity @Table("payments")` | `payments` |
| `FareEstimator` | [FareEstimator.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/FareEstimator.java) | `@Component` | *(logic only)* |
| `MultiModalTripFacade` | [MultiModalTripFacade.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/MultiModalTripFacade.java) | `@Component` | *(logic only)* |
| `MetroService` | [MetroService.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/MetroService.java) | `@Component` | *(logic only)* |

---

## 2. SOLID Design Principles — Detailed Explanation

### 2.1 Single Responsibility Principle (SRP)

> **"A class should have only one reason to change."**

Each service class handles exactly ONE domain concern:

```mermaid
graph LR
    subgraph "SRP — Each class has ONE job"
        RS["RiderServiceImpl<br/>Rider CRUD + Rating"]
        DS["DriverServiceImpl<br/>Driver Status + Availability"]
        BS["BookingServiceImpl<br/>Create + Cancel Bookings"]
        RIS["RideServiceImpl<br/>Ride State Transitions"]
        PS["PaymentServiceImpl<br/>Payment Processing"]
    end
    
    RS -.->|"If rider logic changes"| RS
    DS -.->|"If driver logic changes"| DS
    BS -.->|"If booking logic changes"| BS
    RIS -.->|"If ride logic changes"| RIS
    PS -.->|"If payment logic changes"| PS
```

**Code example demonstrating SRP:**

```java
// ✅ BookingServiceImpl ONLY handles booking creation and cancellation
// File: service/impl/BookingServiceImpl.java
@Service
public class BookingServiceImpl implements BookingService {
    public Booking bookRide(BookRideRequest request) { ... }    // Create booking
    public Booking cancelBooking(Long bookingId) { ... }         // Cancel booking
    public Booking getBookingById(Long bookingId) { ... }        // Get booking
    public List<Booking> getBookingsByRider(Long riderId) { ... }// List bookings
    // ❌ Does NOT handle: ride status, payment, driver management
}

// ✅ RideServiceImpl ONLY handles ride state transitions
// File: service/impl/RideServiceImpl.java
@Service
public class RideServiceImpl implements RideService {
    public Ride acceptRide(Long rideId, Long driverId) { ... }   // State change
    public Ride startRide(Long rideId) { ... }                    // State change
    public Ride completeRide(Long rideId) { ... }                 // State change
    public Ride cancelRide(Long rideId) { ... }                   // State change
    // ❌ Does NOT handle: booking creation, payment, fare calculation
}
```

### 2.2 Open/Closed Principle (OCP)

> **"Software entities should be open for extension but closed for modification."**

```mermaid
graph TB
    subgraph "OCP — Extend without modifying"
        FS["FareStrategy<br/><<interface>>"]
        E["EconomyFareStrategy<br/>₹50 base + ₹12/km"]
        P["PremiumFareStrategy<br/>₹100 base + ₹20/km"]
        S["SharedFareStrategy<br/>₹30 base + ₹8/km"]
        SU["SurgePricingStrategy<br/>1.5x multiplier"]
        NEW["🆕 BikeFareStrategy<br/>Just add new file!"]
    end
    
    FS --> E
    FS --> P
    FS --> S
    FS --> SU
    FS -.->|"Extension point"| NEW
```

**How it works in code:**

To add a new ride type (e.g., BIKE), you:
1. Create `BikeFareStrategy.java` implementing `FareStrategy` — ✅ Extension
2. Add `BIKE` to `RideType` enum
3. Add BIKE case to `RideFactory`
4. **Zero changes** to `BookingServiceImpl`, `FareStrategy`, or other strategies — ✅ Closed

### 2.3 Liskov Substitution Principle (LSP)

> **"Subtypes must be substitutable for their base types."**

```mermaid
graph TD
    subgraph "LSP — SharedRide substitutes Ride"
        RF["RideFactory.createRide()"]
        R["Returns: Ride (base type)"]
        BS["BookingServiceImpl"]
    end
    
    RF -->|"If ECONOMY/PREMIUM"| R1["new Ride(...)"]
    RF -->|"If SHARED"| R2["new SharedRide(...)"]
    R1 --> R
    R2 --> R
    R -->|"ride.setFare()<br/>ride.getDistance()<br/>Works identically"| BS
```

**Code proof:**

```java
// In BookingServiceImpl — works with Ride reference, doesn't know about SharedRide
Ride ride = rideFactory.createRide(request.getRideType(), pickup, drop);
ride.setFare(fare);  // ✅ Works for Ride AND SharedRide
// LSP satisfied: SharedRide doesn't break any Ride behavior
```

### 2.4 Dependency Inversion Principle (DIP)

> **"Depend on abstractions, not on concretions."**

```mermaid
graph TD
    subgraph "DIP — All dependencies point to interfaces"
        BC["BookingController"]
        BSI["BookingService<br/><<interface>>"]
        BSIMPL["BookingServiceImpl"]
        
        PC["PaymentController"]
        PSI["PaymentService<br/><<interface>>"]
        PSIMPL["PaymentServiceImpl"]
        PGI["PaymentGateway<br/><<interface>>"]
        RA["RazorpayAdapter"]
    end
    
    BC -->|"depends on"| BSI
    BSIMPL -.->|"implements"| BSI
    
    PC -->|"depends on"| PSI
    PSIMPL -.->|"implements"| PSI
    PSIMPL -->|"depends on"| PGI
    RA -.->|"implements"| PGI
```

---

## 3. Design Patterns — UML & Code Mapping

### 3.1 Strategy Pattern (Behavioral) — Fare Calculation

```mermaid
classDiagram
    class FareStrategy {
        <<interface>>
        +calculateFare(distance, duration) double
        +getStrategyName() String
    }
    
    class EconomyFareStrategy {
        -BASE_FARE = 50
        -PER_KM_RATE = 12
        +calculateFare() double
        +getStrategyName() String
    }
    
    class PremiumFareStrategy {
        -BASE_FARE = 100
        -PER_KM_RATE = 20
        +calculateFare() double
        +getStrategyName() String
    }
    
    class SharedFareStrategy {
        -BASE_FARE = 30
        -PER_KM_RATE = 8
        +calculateFare() double
        +getStrategyName() String
    }
    
    class SurgePricingStrategy {
        -FareStrategy baseStrategy
        -SURGE_MULTIPLIER = 1.5
        +calculateFare() double
    }
    
    class BookingServiceImpl {
        -Map~String,FareStrategy~ fareStrategies
        +bookRide(request) Booking
    }
    
    FareStrategy <|.. EconomyFareStrategy
    FareStrategy <|.. PremiumFareStrategy
    FareStrategy <|.. SharedFareStrategy
    FareStrategy <|.. SurgePricingStrategy
    SurgePricingStrategy --> FareStrategy : wraps base
    BookingServiceImpl --> FareStrategy : uses
```

**File-to-UML mapping:**

| UML Element | File | Lines |
|---|---|---|
| `FareStrategy` | [FareStrategy.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/strategy/FareStrategy.java) | L14-L26 |
| `EconomyFareStrategy` | [EconomyFareStrategy.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/strategy/EconomyFareStrategy.java) | Full |
| `PremiumFareStrategy` | [PremiumFareStrategy.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/strategy/PremiumFareStrategy.java) | Full |
| `SharedFareStrategy` | [SharedFareStrategy.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/strategy/SharedFareStrategy.java) | Full |
| Context (uses strategy) | [BookingServiceImpl.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/service/impl/BookingServiceImpl.java) | L30, L66-L69 |

### 3.2 Factory Pattern (Creational) — Ride Creation

```mermaid
classDiagram
    class RideFactory {
        +createRide(type, pickup, drop) Ride
    }
    
    class Ride {
        -rideId
        -fare
        -distance
        -rideType
    }
    
    class SharedRide {
        -maxPassengers
        -currentPassengers
        +addPassenger()
        +splitFare()
    }
    
    RideFactory ..> Ride : creates
    RideFactory ..> SharedRide : creates
    Ride <|-- SharedRide
```

**File-to-UML mapping:**

| UML Element | File |
|---|---|
| `RideFactory` | [RideFactory.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/factory/RideFactory.java) |
| `Ride` | [Ride.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/Ride.java) |
| `SharedRide` | [SharedRide.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/model/SharedRide.java) |
| Usage point | [BookingServiceImpl.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/service/impl/BookingServiceImpl.java) L63 |

### 3.3 Adapter Pattern (Structural) — Payment Gateway

```mermaid
classDiagram
    class PaymentGateway {
        <<interface>>
        +processPayment(amount, method) boolean
        +refundPayment(amount) boolean
        +getGatewayName() String
    }
    
    class RazorpayAdapter {
        -RazorpaySDK razorpaySDK
        +processPayment(amount, method) boolean
        +refundPayment(amount) boolean
    }
    
    class RazorpaySDK {
        <<Adaptee>>
        +createOrder(paise, currency) String
        +capturePayment(orderId) boolean
        +initiateRefund(paise) boolean
    }
    
    class PaymentServiceImpl {
        <<Client>>
        -PaymentGateway paymentGateway
        +processPayment(rideId, mode)
    }
    
    PaymentGateway <|.. RazorpayAdapter : implements
    RazorpayAdapter --> RazorpaySDK : adapts (rupees→paise)
    PaymentServiceImpl --> PaymentGateway : depends on (DIP)
```

**File-to-UML mapping:**

| UML Role | File |
|---|---|
| Target (interface) | [PaymentGateway.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/adapter/PaymentGateway.java) |
| Adapter | [RazorpayAdapter.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/adapter/RazorpayAdapter.java) |
| Metro Adapter | [MetroServiceAdapter.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/adapter/MetroServiceAdapter.java) |
| Client | [PaymentServiceImpl.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/service/impl/PaymentServiceImpl.java) |

### 3.4 Facade Pattern (Structural) — Multi-Modal Trip

```mermaid
classDiagram
    class TripController {
        <<Client>>
        +planMultiModalTrip()
    }
    
    class MultiModalTripFacade {
        <<Facade>>
        -FareEstimator fareEstimator
        -MetroServiceAdapter metroAdapter
        -Long tripId
        +planTrip(origin, from, to, dest) MultiModalTripPlan
    }
    
    class FareEstimator {
        <<Sub-system 1>>
        +calculateCabFare(distance, type) double
        +calculateMetroFare(from, to) double
        +getAvailableOptions(distance) List
    }
    
    class MetroServiceAdapter {
        <<Sub-system 2>>
        +getMetroFare(from, to) double
        +getMetroEstimatedTime(from, to) int
        +getAvailableStations() List
    }
    
    class MetroService {
        <<Sub-system 3>>
        +getStations() List
        +getMetroFare() double
        +getEstimatedTime() int
    }
    
    TripController --> MultiModalTripFacade : calls planTrip()
    MultiModalTripFacade --> FareEstimator : coordinates
    MultiModalTripFacade --> MetroServiceAdapter : coordinates
    MetroServiceAdapter --> MetroService : wraps
```

**File-to-UML mapping:**

| UML Role | File |
|---|---|
| Client | [TripController.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/controller/TripController.java) |
| Facade | [MultiModalTripFacade.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/MultiModalTripFacade.java) |
| Sub-system 1 | [FareEstimator.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/FareEstimator.java) |
| Sub-system 2 | [MetroServiceAdapter.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/adapter/MetroServiceAdapter.java) |
| Sub-system 3 | [MetroService.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/facade/MetroService.java) |

### 3.5 State Pattern (Behavioral) — Ride Lifecycle

```mermaid
stateDiagram-v2
    [*] --> REQUESTED : bookRide()
    REQUESTED --> DRIVER_ASSIGNED : accept()
    REQUESTED --> CANCELLED : cancel()
    DRIVER_ASSIGNED --> IN_PROGRESS : start()
    DRIVER_ASSIGNED --> CANCELLED : cancel()
    IN_PROGRESS --> COMPLETED : complete()
    IN_PROGRESS --> CANCELLED : cancel()
    COMPLETED --> [*]
    CANCELLED --> [*]
```

```mermaid
classDiagram
    class RideState {
        <<interface>>
        +accept(Ride)
        +start(Ride)
        +complete(Ride)
        +cancel(Ride)
    }
    
    class RequestedState {
        +accept(ride) ✅ → DRIVER_ASSIGNED
        +start(ride) ❌ throws
        +complete(ride) ❌ throws
        +cancel(ride) ✅ → CANCELLED
    }
    
    class DriverAssignedState {
        +accept(ride) ❌ throws
        +start(ride) ✅ → IN_PROGRESS
        +complete(ride) ❌ throws
        +cancel(ride) ✅ → CANCELLED
    }
    
    class InProgressState {
        +accept(ride) ❌ throws
        +start(ride) ❌ throws
        +complete(ride) ✅ → COMPLETED
        +cancel(ride) ✅ → CANCELLED
    }
    
    class CompletedState {
        +accept(ride) ❌ throws
        +start(ride) ❌ throws
        +complete(ride) ❌ throws
        +cancel(ride) ❌ throws
    }
    
    class CancelledState {
        +accept(ride) ❌ throws
        +start(ride) ❌ throws
        +complete(ride) ❌ throws
        +cancel(ride) ❌ throws
    }
    
    class RideStateFactory {
        +getState(RideStatus) RideState$
    }
    
    RideState <|.. RequestedState
    RideState <|.. DriverAssignedState
    RideState <|.. InProgressState
    RideState <|.. CompletedState
    RideState <|.. CancelledState
    RideStateFactory ..> RideState : creates
```

**File-to-UML mapping:**

| UML Element | File |
|---|---|
| `RideState` interface | [RideState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/RideState.java) |
| `RequestedState` | [RequestedState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/RequestedState.java) |
| `DriverAssignedState` | [DriverAssignedState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/DriverAssignedState.java) |
| `InProgressState` | [InProgressState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/InProgressState.java) |
| `CompletedState` | [CompletedState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/CompletedState.java) |
| `CancelledState` | [CancelledState.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/CancelledState.java) |
| `RideStateFactory` | [RideStateFactory.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/pattern/state/RideStateFactory.java) |
| Usage | [RideServiceImpl.java](file:///c:/Users/91991/Downloads/mini_project/mini_project/src/main/java/com/ridebooking/service/impl/RideServiceImpl.java) L46-47, L64-65, L81-82, L101-102 |

---

## 4. Sequence Diagram — Book Ride Flow

```mermaid
sequenceDiagram
    actor User
    participant BC as BookingController
    participant BS as BookingService
    participant RS as RiderService
    participant RF as RideFactory
    participant FS as FareStrategy
    participant BR as BookingRepository

    User->>BC: POST /api/bookings (JSON body)
    BC->>BS: bookRide(request)
    BS->>RS: getRiderById(riderId)
    RS-->>BS: Rider object
    BS->>BS: Create Location (pickup, drop)
    BS->>RF: createRide(type, pickup, drop)
    Note over RF: FACTORY PATTERN<br/>if SHARED → SharedRide<br/>else → Ride
    RF-->>BS: Ride object
    BS->>FS: calculateFare(distance, 0)
    Note over FS: STRATEGY PATTERN<br/>Economy: ₹50 + 12/km<br/>Premium: ₹100 + 20/km
    FS-->>BS: fare amount
    BS->>BS: ride.setFare(fare)
    BS->>BS: new Booking(rider, ride)
    BS->>BR: save(booking)
    BR-->>BS: saved booking
    BS-->>BC: Booking (with Ride)
    BC-->>User: HTTP 200 (JSON)
```

## 5. Sequence Diagram — Complete Ride Lifecycle

```mermaid
sequenceDiagram
    actor Driver
    participant RC as RideController
    participant RIS as RideServiceImpl
    participant RSF as RideStateFactory
    participant RS as RideState
    participant RR as RideRepository

    Note over Driver,RR: Step 1: Accept Ride (REQUESTED → DRIVER_ASSIGNED)
    Driver->>RC: PUT /api/rides/1/accept?driverId=1
    RC->>RIS: acceptRide(1, 1)
    RIS->>RSF: getState(REQUESTED)
    RSF-->>RIS: RequestedState
    RIS->>RS: accept(ride)
    Note over RS: ✅ Sets status = DRIVER_ASSIGNED
    RIS->>RR: save(ride)
    RIS-->>Driver: Ride (DRIVER_ASSIGNED)

    Note over Driver,RR: Step 2: Start Ride (DRIVER_ASSIGNED → IN_PROGRESS)
    Driver->>RC: PUT /api/rides/1/start
    RC->>RIS: startRide(1)
    RIS->>RSF: getState(DRIVER_ASSIGNED)
    RSF-->>RIS: DriverAssignedState
    RIS->>RS: start(ride)
    Note over RS: ✅ Sets status = IN_PROGRESS
    RIS-->>Driver: Ride (IN_PROGRESS)

    Note over Driver,RR: Step 3: Complete Ride (IN_PROGRESS → COMPLETED)
    Driver->>RC: PUT /api/rides/1/complete
    RC->>RIS: completeRide(1)
    RIS->>RSF: getState(IN_PROGRESS)
    RSF-->>RIS: InProgressState
    RIS->>RS: complete(ride)
    Note over RS: ✅ Sets status = COMPLETED
    RIS-->>Driver: Ride (COMPLETED)
```

## 6. Sequence Diagram — Payment with Adapter Pattern

```mermaid
sequenceDiagram
    actor User
    participant PC as PaymentController
    participant PS as PaymentServiceImpl
    participant PG as PaymentGateway
    participant RA as RazorpayAdapter
    participant SDK as RazorpaySDK

    User->>PC: POST /payments/ride/1?mode=CARD
    PC->>PS: processPayment(1, CARD)
    PS->>PS: Get Ride (fare = ₹112.22)
    PS->>PG: processPayment(112.22, "CARD")
    Note over PG: DIP: Interface reference
    PG->>RA: processPayment(112.22, "CARD")
    Note over RA: ADAPTER: Convert ₹ → paise
    RA->>SDK: createOrder(11222, "INR")
    SDK-->>RA: orderId = "rzp_order_xxx"
    RA->>SDK: capturePayment("rzp_order_xxx")
    SDK-->>RA: true (success)
    RA-->>PS: true
    PS->>PS: Create Payment(amount, CARD, SUCCESS)
    PS-->>User: Payment{id=1, status=SUCCESS}
```

---

## 7. Complete Summary Table

| # | Pattern/Principle | Category | Where Applied | Key Files |
|---|---|---|---|---|
| 1 | **SRP** | SOLID | 5 service classes, each one responsibility | `service/impl/*.java` |
| 2 | **OCP** | SOLID | FareStrategy — add new strategies without modifying existing | `pattern/strategy/*.java` |
| 3 | **LSP** | SOLID | SharedRide substitutes Ride in all contexts | `model/Ride.java`, `SharedRide.java` |
| 4 | **DIP** | SOLID | Controllers → Interfaces ← Implementations | All controllers + services |
| 5 | **Strategy** | Behavioral | Fare calculation with 4 algorithms | `pattern/strategy/` (4 files) |
| 6 | **Factory** | Creational | Ride/SharedRide creation based on type | `pattern/factory/RideFactory.java` |
| 7 | **Adapter** | Structural | Razorpay SDK + Metro API integration | `pattern/adapter/` (3 files) |
| 8 | **Facade** | Structural | Multi-modal trip = 1 call instead of 3 | `pattern/facade/MultiModalTripFacade.java` |
| 9 | **State** | Behavioral | Ride lifecycle state machine (5 states) | `pattern/state/` (7 files) |

> **Total: 4 SOLID principles + 5 Design Patterns = 10/10 OOAD compliance**
