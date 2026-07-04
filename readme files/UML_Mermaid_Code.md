# UML Diagrams — Mermaid Code for draw.io

> **How to use in draw.io:**
> 1. Open [draw.io](https://app.diagrams.net)
> 2. Click **+ → Advanced → Mermaid**
> 3. Paste any code block below
> 4. Click **Insert**

---

## Diagram 1: Complete UML Class Diagram (Domain Model)

Copy this entire block:

```
classDiagram
    class User {
        <<abstract>>
        #Long userId
        #String name
        #String email
        #String phone
        +login()* void
        +logout()* void
    }

    class Rider {
        -float riderRating
        -List~Booking~ bookings
        +requestRide() void
        +chooseRideOption() void
        +login() void
        +logout() void
    }

    class Driver {
        -float driverRating
        -DriverStatus availabilityStatus
        -Vehicle vehicle
        +acceptRide() void
        +startRide() void
        +endRide() void
        +login() void
        +logout() void
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
        -LocalDateTime cancellationTime
        +cancelBooking() void
    }

    class Ride {
        -Long rideId
        -double fare
        -double distance
        -RideStatus rideStatus
        -RideType rideType
        -Location pickupLocation
        -Location dropLocation
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
        -LocalDateTime transactionTime
    }

    class RideOption {
        <<DTO>>
        -String optionType
        -double price
        -int estimatedTime
    }

    class FareEstimator {
        +calculateCabFare(distance, type) double
        +calculateMetroFare(from, to) double
        +getAvailableOptions(distance) List~RideOption~
    }

    class MultiModalTripFacade {
        -Long tripId
        -FareEstimator fareEstimator
        -MetroServiceAdapter metroAdapter
        +planTrip(origin, from, to, dest) MultiModalTripPlan
    }

    class MetroService {
        -List~String~ stations
        -Map~String_Double~ fareChart
        +getStations() List~String~
        +getMetroFare(from, to) double
        +getEstimatedTime(from, to) int
    }

    User <|-- Rider : extends
    User <|-- Driver : extends
    Driver "1" --> "1" Vehicle : owns
    Rider "1" --> "*" Booking : has bookings
    Booking "1" --> "1" Ride : contains
    Ride <|-- SharedRide : extends
    Ride "1" *-- "1" Location : pickup
    Ride "1" *-- "1" Location : drop
    Ride "*" --> "1" Driver : assigned to
    Ride "1" --> "0..1" Payment : has payment
    MultiModalTripFacade --> FareEstimator : uses
    MultiModalTripFacade --> MetroService : uses
```

---

## Diagram 2: Strategy Pattern — Fare Calculation

```
classDiagram
    class FareStrategy {
        <<interface>>
        +calculateFare(distance double, duration double) double
        +getStrategyName() String
    }

    class EconomyFareStrategy {
        -double BASE_FARE = 50
        -double PER_KM_RATE = 12
        +calculateFare(distance, duration) double
        +getStrategyName() String
    }

    class PremiumFareStrategy {
        -double BASE_FARE = 100
        -double PER_KM_RATE = 20
        +calculateFare(distance, duration) double
        +getStrategyName() String
    }

    class SharedFareStrategy {
        -double BASE_FARE = 30
        -double PER_KM_RATE = 8
        +calculateFare(distance, duration) double
        +getStrategyName() String
    }

    class SurgePricingStrategy {
        -FareStrategy baseStrategy
        -double SURGE_MULTIPLIER = 1.5
        +calculateFare(distance, duration) double
        +getStrategyName() String
    }

    class BookingServiceImpl {
        <<Context>>
        -Map~String_FareStrategy~ fareStrategies
        -RideFactory rideFactory
        +bookRide(request) Booking
        +cancelBooking(id) Booking
    }

    FareStrategy <|.. EconomyFareStrategy : implements
    FareStrategy <|.. PremiumFareStrategy : implements
    FareStrategy <|.. SharedFareStrategy : implements
    FareStrategy <|.. SurgePricingStrategy : implements
    SurgePricingStrategy --> FareStrategy : wraps base strategy
    BookingServiceImpl --> FareStrategy : uses (Spring injects Map)
```

---

## Diagram 3: Factory Pattern — Ride Creation

```
classDiagram
    class RideFactory {
        <<Factory>>
        +createRide(type RideType, pickup Location, drop Location) Ride
    }

    class Ride {
        <<Product>>
        -Long rideId
        -double fare
        -double distance
        -RideType rideType
        -RideStatus rideStatus
        -Location pickupLocation
        -Location dropLocation
        +Ride(pickup, drop, type)
    }

    class SharedRide {
        <<Concrete Product>>
        -int maxPassengers
        -int currentPassengers
        +SharedRide(pickup, drop)
        +addPassenger() boolean
        +splitFare() double
    }

    class RideType {
        <<enumeration>>
        ECONOMY
        PREMIUM
        SHARED
    }

    class BookingServiceImpl {
        <<Client>>
        -RideFactory rideFactory
        +bookRide(request) Booking
    }

    Ride <|-- SharedRide : extends
    RideFactory ..> Ride : creates
    RideFactory ..> SharedRide : creates
    RideFactory --> RideType : uses
    BookingServiceImpl --> RideFactory : uses
```

---

## Diagram 4: Adapter Pattern — Payment Gateway

```
classDiagram
    class PaymentGateway {
        <<interface - Target>>
        +processPayment(amount double, method String) boolean
        +refundPayment(amount double) boolean
        +getGatewayName() String
    }

    class RazorpayAdapter {
        <<Adapter>>
        -RazorpaySDK razorpaySDK
        +processPayment(amount, method) boolean
        +refundPayment(amount) boolean
        +getGatewayName() String
    }

    class RazorpaySDK {
        <<Adaptee - External>>
        +createOrder(amountInPaise int, currency String) String
        +capturePayment(orderId String) boolean
        +initiateRefund(amountInPaise int) boolean
    }

    class MetroServiceAdapter {
        <<Adapter>>
        -MetroService metroService
        +getMetroFare(from, to) double
        +getMetroEstimatedTime(from, to) int
        +getAvailableStations() List
    }

    class MetroService {
        <<Adaptee>>
        -List~String~ stations
        -Map~String_Double~ fareChart
        +getStations() List
        +getMetroFare(from, to) double
    }

    class PaymentServiceImpl {
        <<Client>>
        -PaymentGateway paymentGateway
        +processPayment(rideId, mode) Payment
    }

    PaymentGateway <|.. RazorpayAdapter : implements
    RazorpayAdapter --> RazorpaySDK : adapts rupees to paise
    MetroServiceAdapter --> MetroService : wraps
    PaymentServiceImpl --> PaymentGateway : depends on interface - DIP
```

---

## Diagram 5: Facade Pattern — Multi-Modal Trip

```
classDiagram
    class TripController {
        <<Client>>
        -MultiModalTripFacade tripFacade
        +planMultiModalTrip(origin, from, to, dest) MultiModalTripPlan
    }

    class MultiModalTripFacade {
        <<Facade>>
        -FareEstimator fareEstimator
        -MetroServiceAdapter metroAdapter
        -Long tripId
        +planTrip(origin, fromStation, toStation, dest) MultiModalTripPlan
        +getTripId() Long
    }

    class FareEstimator {
        <<Sub-system 1>>
        -Map~String_FareStrategy~ strategies
        +calculateCabFare(distance, type) double
        +calculateMetroFare(from, to) double
        +getAvailableOptions(distance) List~RideOption~
    }

    class MetroServiceAdapter {
        <<Sub-system 2>>
        -MetroService metroService
        +getMetroFare(from, to) double
        +getMetroEstimatedTime(from, to) int
        +getAvailableStations() List~String~
    }

    class MetroService {
        <<Sub-system 3>>
        -List~String~ stations
        -Map~String_Double~ fareChart
        +getStations() List
        +getMetroFare(from, to) double
        +getEstimatedTime(from, to) int
    }

    class MultiModalTripPlan {
        <<Result>>
        -Long tripId
        -List~TripLeg~ legs
        -double totalFare
        -int totalTime
        +addLeg(mode, fare, time, desc) void
        +calculateTotals() void
    }

    class TripLeg {
        -String mode
        -double fare
        -int timeMinutes
        -String description
    }

    TripController --> MultiModalTripFacade : calls planTrip
    MultiModalTripFacade --> FareEstimator : step 1 and 3 cab fare
    MultiModalTripFacade --> MetroServiceAdapter : step 2 metro fare
    MetroServiceAdapter --> MetroService : delegates
    MultiModalTripFacade ..> MultiModalTripPlan : returns
    MultiModalTripPlan --> TripLeg : contains legs
```

---

## Diagram 6: State Pattern — Ride Lifecycle

```
classDiagram
    class RideState {
        <<interface>>
        +accept(ride Ride) void
        +start(ride Ride) void
        +complete(ride Ride) void
        +cancel(ride Ride) void
    }

    class RequestedState {
        +accept(ride) void  ✅ DRIVER_ASSIGNED
        +start(ride) void   ❌ throws
        +complete(ride) void ❌ throws
        +cancel(ride) void  ✅ CANCELLED
    }

    class DriverAssignedState {
        +accept(ride) void  ❌ throws
        +start(ride) void   ✅ IN_PROGRESS
        +complete(ride) void ❌ throws
        +cancel(ride) void  ✅ CANCELLED
    }

    class InProgressState {
        +accept(ride) void  ❌ throws
        +start(ride) void   ❌ throws
        +complete(ride) void ✅ COMPLETED
        +cancel(ride) void  ✅ CANCELLED
    }

    class CompletedState {
        +accept(ride) void  ❌ throws
        +start(ride) void   ❌ throws
        +complete(ride) void ❌ throws
        +cancel(ride) void  ❌ throws
    }

    class CancelledState {
        +accept(ride) void  ❌ throws
        +start(ride) void   ❌ throws
        +complete(ride) void ❌ throws
        +cancel(ride) void  ❌ throws
    }

    class RideStateFactory {
        +getState(status RideStatus) RideState$
    }

    class RideServiceImpl {
        <<Context>>
        -RideRepository rideRepository
        -DriverService driverService
        +acceptRide(rideId, driverId) Ride
        +startRide(rideId) Ride
        +completeRide(rideId) Ride
        +cancelRide(rideId) Ride
    }

    RideState <|.. RequestedState : implements
    RideState <|.. DriverAssignedState : implements
    RideState <|.. InProgressState : implements
    RideState <|.. CompletedState : implements
    RideState <|.. CancelledState : implements
    RideStateFactory ..> RideState : creates
    RideServiceImpl --> RideStateFactory : gets current state
    RideServiceImpl --> RideState : calls transition
```

---

## Diagram 7: State Machine — Ride Status Transitions

```
stateDiagram-v2
    [*] --> REQUESTED : bookRide()
    REQUESTED --> DRIVER_ASSIGNED : accept(driverId)
    REQUESTED --> CANCELLED : cancel()
    DRIVER_ASSIGNED --> IN_PROGRESS : start()
    DRIVER_ASSIGNED --> CANCELLED : cancel()
    IN_PROGRESS --> COMPLETED : complete()
    IN_PROGRESS --> CANCELLED : cancel()
    COMPLETED --> [*]
    CANCELLED --> [*]
```

---

## Diagram 8: Sequence — Book Ride Flow

```
sequenceDiagram
    actor Rider
    participant BC as BookingController
    participant BS as BookingServiceImpl
    participant RS as RiderService
    participant RF as RideFactory
    participant FS as FareStrategy
    participant DB as BookingRepository

    Rider->>BC: POST /api/bookings
    BC->>BS: bookRide(request)
    BS->>RS: getRiderById(riderId)
    RS-->>BS: Rider
    BS->>BS: Create Location pickup and drop
    BS->>RF: createRide(ECONOMY, pickup, drop)
    Note over RF: FACTORY PATTERN
    RF-->>BS: Ride object
    BS->>FS: calculateFare(distance, 0)
    Note over FS: STRATEGY PATTERN
    FS-->>BS: fare = 112.22
    BS->>BS: ride.setFare(112.22)
    BS->>BS: new Booking(rider, ride)
    BS->>DB: save(booking)
    DB-->>BS: Booking saved
    BS-->>BC: Booking
    BC-->>Rider: 200 OK JSON
```

---

## Diagram 9: Sequence — Ride Lifecycle (State Pattern)

```
sequenceDiagram
    actor Driver
    participant RC as RideController
    participant RS as RideServiceImpl
    participant RSF as RideStateFactory
    participant State as RideState
    participant DB as RideRepository

    Note over Driver,DB: STEP 1 - Accept Ride
    Driver->>RC: PUT /rides/1/accept?driverId=1
    RC->>RS: acceptRide(1, 1)
    RS->>RSF: getState(REQUESTED)
    RSF-->>RS: RequestedState
    RS->>State: accept(ride)
    Note over State: REQUESTED → DRIVER_ASSIGNED
    RS->>DB: save(ride)
    RS-->>Driver: Ride DRIVER_ASSIGNED

    Note over Driver,DB: STEP 2 - Start Ride
    Driver->>RC: PUT /rides/1/start
    RC->>RS: startRide(1)
    RS->>RSF: getState(DRIVER_ASSIGNED)
    RSF-->>RS: DriverAssignedState
    RS->>State: start(ride)
    Note over State: DRIVER_ASSIGNED → IN_PROGRESS
    RS-->>Driver: Ride IN_PROGRESS

    Note over Driver,DB: STEP 3 - Complete Ride
    Driver->>RC: PUT /rides/1/complete
    RC->>RS: completeRide(1)
    RS->>RSF: getState(IN_PROGRESS)
    RSF-->>RS: InProgressState
    RS->>State: complete(ride)
    Note over State: IN_PROGRESS → COMPLETED
    RS-->>Driver: Ride COMPLETED
```

---

## Diagram 10: Sequence — Payment (Adapter Pattern)

```
sequenceDiagram
    actor User
    participant PC as PaymentController
    participant PS as PaymentServiceImpl
    participant PG as PaymentGateway
    participant RA as RazorpayAdapter
    participant SDK as RazorpaySDK

    User->>PC: POST /payments/ride/1?mode=CARD
    PC->>PS: processPayment(1, CARD)
    PS->>PS: getRide(1) fare=112.22
    PS->>PG: processPayment(112.22, CARD)
    Note over PG: Interface - DIP
    PG->>RA: processPayment(112.22, CARD)
    Note over RA: ADAPTER converts rupees to paise
    RA->>SDK: createOrder(11222, INR)
    SDK-->>RA: rzp_order_12345
    RA->>SDK: capturePayment(rzp_order_12345)
    SDK-->>RA: true
    RA-->>PS: true
    PS->>PS: new Payment(112.22, CARD, SUCCESS)
    PS-->>PC: Payment object
    PC-->>User: 200 OK JSON
```

---

## Diagram 11: MVC Architecture

```
graph TD
    subgraph Frontend
        R[React + Vite + Tailwind]
    end

    subgraph Controller Layer
        RC[RiderController]
        DC[DriverController]
        BKC[BookingController]
        RIC[RideController]
        PC[PaymentController]
        TC[TripController]
    end

    subgraph Service Layer
        RSI[RiderServiceImpl]
        DSI[DriverServiceImpl]
        BSI[BookingServiceImpl]
        RISI[RideServiceImpl]
        PSI[PaymentServiceImpl]
    end

    subgraph Pattern Layer
        STR[Strategy - FareStrategy]
        FAC[Factory - RideFactory]
        ADP[Adapter - RazorpayAdapter]
        FCD[Facade - MultiModalTripFacade]
        STA[State - RideState]
    end

    subgraph Repository Layer
        RR[RiderRepository]
        DR[DriverRepository]
        BR[BookingRepository]
        RIR[RideRepository]
        VR[VehicleRepository]
        PR[PaymentRepository]
    end

    subgraph Database
        H2[H2 In-Memory Database]
    end

    R -->|HTTP REST| RC
    R -->|HTTP REST| DC
    R -->|HTTP REST| BKC
    R -->|HTTP REST| RIC
    R -->|HTTP REST| PC
    R -->|HTTP REST| TC

    RC --> RSI
    DC --> DSI
    BKC --> BSI
    RIC --> RISI
    PC --> PSI
    TC --> FCD

    BSI --> STR
    BSI --> FAC
    RISI --> STA
    PSI --> ADP

    RSI --> RR
    DSI --> DR
    BSI --> BR
    RISI --> RIR
    PSI --> PR

    RR --> H2
    DR --> H2
    BR --> H2
    RIR --> H2
    VR --> H2
    PR --> H2
```
