# 🏛️ Online Ride Booking System — Complete OOAD Design Audit

> **Evaluator**: Senior Software Architect / OOAD Examiner  
> **Date**: April 20, 2026  
> **Project**: Online Ride Booking System (Java)  
> **Standard**: University-level OOAD Mini-Project Grading

---

## 📋 Summary (Overall Evaluation)

The project demonstrates a **reasonable understanding of creational design patterns** and an **attempt at MVC layering**, but suffers from **significant deviations from its own class diagram**, **absence of Structural and Behavioral patterns** (which are mandatory), **missing Service and Repository layers**, and **no database/JPA integration**. The project is a **console-only Java application pretending to have Spring Boot MVC**, when in reality it has no Spring Boot, no REST endpoints, no persistence layer, and no View layer.

> [!CAUTION]
> The project has fundamental gaps that an OOAD examiner **will** flag: missing Structural/Behavioral patterns, class diagram inconsistencies, and no actual MVC framework usage.

---

## 1. UML CLASS DIAGRAM CONSISTENCY

### 1.1 Classes: Diagram vs. Implementation

| Class (Diagram) | Implemented? | Implementation Status |
|---|---|---|
| **User** | ✅ Yes | `models/User.java` — abstract class |
| **Rider** | ❌ **NO** | Named `Customer` instead. **Name mismatch.** |
| **Driver** | ✅ Yes | `models/Driver.java` |
| **Booking** | ❌ **NO** | **Completely missing.** No `Booking` class exists anywhere. |
| **Vehicle** | ❌ **NO** | **Completely missing.** `vehicleNumber` is inlined into `Driver` as a `String`. |
| **Ride** | ✅ Yes | `models/Ride.java` |
| **SharedRide** (extends Ride) | ⚠️ Partial | `patterns/SharedRide.java` exists but extends `RideType`, **NOT** `Ride` as the diagram specifies. |
| **Location** | ❌ **NO** | **Completely missing.** Locations are raw `String` fields. |
| **Payment** | ✅ Yes | `models/Payment.java` |
| **FareEstimator** | ❌ **NO** | `Fare` model exists instead. `FareEstimator` with `calculateCabFare()`, `calculateSharedFare()`, `calculateMetroFare()`, `sortRideOptions()` is missing. |
| **RideOption** | ❌ **NO** | **Completely missing.** |
| **MultiModalTrip** | ❌ **NO** | **Completely missing.** |
| **MetroService** | ❌ **NO** | **Completely missing.** |

### 1.2 Attributes Mismatch

| Class | Diagram Attribute | Implementation |
|---|---|---|
| **User** | `userId : int` | `userId : String` — **Type mismatch** |
| **User** | `login()`, `logout()` | **Methods missing** |
| **Rider** | `riderRating : float` | Missing (class is named `Customer`, rating inherited from `User` as `double`) |
| **Rider** | `requestRide()`, `chooseRideOption()` | **Methods missing** |
| **Driver** | `driverRating : float` | Missing (inherited `rating : double` from `User`) |
| **Driver** | `acceptRide()`, `startRide()`, `endRide()` | **Methods missing** |
| **Ride** | `rideId : int` | `rideId : String` — **Type mismatch** |
| **Ride** | `calculateFare()` | **Method missing** from `Ride` — fare calculation is in `Fare` and `FareController` |
| **Payment** | `paymentId : int` | `paymentId : String` — **Type mismatch** |
| **Payment** | `processPayment()` | **Method missing** from `Payment` — handled externally by `PaymentController` |
| **SharedRide** | `maxPassengers`, `currentPassengers`, `addPassenger()`, `splitFare()` | **All missing** — `SharedRide` only overrides `displayDetails()` and `calculateFare()` |

### 1.3 Inheritance Relationships

| Diagram Relationship | Implementation Status |
|---|---|
| `User → Rider` | ❌ **Violated** — `Rider` class doesn't exist; `Customer extends User` instead |
| `User → Driver` | ✅ Correct — `Driver extends User` |
| `Ride → SharedRide` | ❌ **Violated** — `SharedRide extends RideType`, not `Ride` |

### 1.4 Associations & Multiplicities

| Diagram Association | Expected | Actual |
|---|---|---|
| Rider — Booking (1 to many) | `Rider` has `List<Booking>` | ❌ **No `Booking` class, no association** |
| Booking — Ride | `Booking` references `Ride` | ❌ **No `Booking` class** |
| Driver — Vehicle (1 to 1) | Separate `Vehicle` object | ❌ **Vehicle is just a `String vehicleNumber` inside `Driver`** |
| Ride — Payment (1 to 1) | `Ride` references `Payment` | ❌ **No `Payment` reference in `Ride`** |
| Ride — Location (1 pickup, 1 drop) | Separate `Location` objects | ❌ **Locations are raw `String` fields** |
| FareEstimator — RideOption (1 to many) | `FareEstimator` manages `RideOption` list | ❌ **Both classes missing** |

> [!WARNING]
> **Verdict**: The implementation matches **only ~30% of the class diagram**. 7 out of 13 classes are missing or misnamed. All multiplicities are incorrectly implemented or absent. This is a **critical failure** for class-diagram consistency.

---

## 2. OBJECT-ORIENTED DESIGN PRINCIPLES (SOLID)

### 2.1 Single Responsibility Principle (SRP)

| Finding | Severity |
|---|---|
| `RideBookingSystem` is a **God Object** — manages drivers, customers, rides, payments, invoices, AND generates IDs. | 🔴 **MAJOR VIOLATION** |
| `FareController` creates `Fare` objects AND contains business logic for estimation — should be a service. | 🟡 **MODERATE** |
| `PaymentController.java` defines 3 separate concerns in one file: controller, interface, and concrete gateway. | 🟡 **MODERATE** |

**Code Example — God Object:**
```java
// RideBookingSystem.java — does EVERYTHING
public class RideBookingSystem {
    private Map<String, Driver> drivers;       // Driver repo
    private Map<String, Customer> customers;   // Customer repo
    private Map<String, Ride> rides;           // Ride repo
    private Map<String, Payment> payments;     // Payment repo
    private Map<String, Invoice> invoices;     // Invoice repo
    
    public Ride createRide(...)  { /* business logic */ }
    public Invoice generateInvoice(...) { /* business logic */ }
    public List<Driver> getAvailableDrivers() { /* query logic */ }
    // ... mixes storage, business logic, and querying
}
```

**Fix**: Split into `DriverRepository`, `CustomerRepository`, `RideRepository`, `RideService`, `InvoiceService`, etc.

### 2.2 Open/Closed Principle (OCP)

| Finding | Severity |
|---|---|
| `RideFactory` uses a `switch` statement — adding a new ride type requires modifying `RideFactory.java`. | 🟡 **MODERATE** |
| `Fare` class has hardcoded constants (`BASE_FARE`, `COST_PER_KM`) — no way to extend for different fare strategies. | 🟡 **MODERATE** |
| `RideType` hierarchy (Factory) is open for extension ✅ — new types can extend `RideType`. | ✅ **Satisfactory** |

**Code Example — OCP Violation in Factory:**
```java
// Must modify this switch for every new ride type
public static RideType createRide(RideTypeEnum type, ...) {
    switch (type) {
        case ECONOMY:  return new EconomyRide(...);
        case PREMIUM:  return new PremiumRide(...);
        case SHARED:   return new SharedRide(...);
        // case XL: return new XLRide(...);  ← Must edit this file!
    }
}
```

**Fix**: Use a `Map<RideTypeEnum, Supplier<RideType>>` registry pattern so new types can self-register.

### 2.3 Liskov Substitution Principle (LSP)

| Finding | Severity |
|---|---|
| `SharedRide extends RideType` but diagram says `SharedRide extends Ride` — these are **completely different hierarchies**. A `SharedRide` cannot substitute a `Ride`. | 🔴 **MAJOR** |
| `Customer extends User` is fine — substitute wherever `User` is expected. | ✅ OK |
| `Driver extends User` is fine. | ✅ OK |

**Problem**: The system has **two parallel, disconnected ride hierarchies**:
- `Ride` (in models) — used by controllers, has `Customer`, `Driver`, `RideStatus`
- `RideType` → `EconomyRide` / `PremiumRide` / `SharedRide` (in patterns) — only used by Factory demo

These two hierarchies are **never connected**. A `RideType` cannot be used where a `Ride` is expected and vice versa.

### 2.4 Dependency Inversion Principle (DIP)

| Finding | Severity |
|---|---|
| `PaymentController` depends on `PaymentGateway` interface — ✅ Good use of DIP | ✅ **GOOD** |
| All controllers depend directly on `RideBookingSystem` concrete class — no interface/abstraction. | 🟡 **MODERATE** |
| `RideController` directly creates and mutates `Ride` and `Driver` objects — tight coupling to concrete models. | 🟡 **MODERATE** |
| `models/Ride.java` depends directly on concrete `Customer` and `Driver` classes. | 🟡 **MODERATE** |

> [!NOTE]
> **SOLID Score**: ~3/10. Only `PaymentGateway` demonstrates DIP properly. SRP is severely violated by the God Object. OCP and LSP have notable violations.

---

## 3. DESIGN PATTERNS (MANDATORY REQUIREMENT)

### 3.1 Implemented Patterns (All Creational)

| Pattern | Class | Correct? | Notes |
|---|---|---|---|
| **Singleton** | `RideBookingSystem` | ✅ Correct | Thread-safe lazy init |
| **Factory** | `RideFactory` + `RideType` hierarchy | ✅ Correct | But disconnected from main `Ride` model |
| **Builder** | `RideBuilder` | ✅ Correct | Fluent interface with validation |
| **Prototype** | `RidePrototype` + `RidePrototypeRegistry` | ✅ Correct | Clone + registry |

> [!IMPORTANT]
> The project implements **4 Creational patterns**, which is impressive, but the mandatory requirement asks for **one Creational, one Structural, one Behavioral** — at minimum 3 different categories.

### 3.2 Missing Pattern Categories

#### ❌ Structural Pattern — **MISSING (MANDATORY)**

No Structural pattern (Adapter, Decorator, Facade, Composite, Proxy, Bridge, Flyweight) is implemented.

**Suggested Implementation — Adapter Pattern for Payment Gateway:**

```java
// Target: Unified payment interface
interface PaymentProcessor {
    PaymentResult pay(double amount, String currency);
}

// Adaptee: External "Razorpay" API  
class RazorpaySDK {
    public String initiateCharge(int paiseAmount, String cur) { ... }
}

// Adapter
class RazorpayAdapter implements PaymentProcessor {
    private RazorpaySDK razorpay;
    
    @Override
    public PaymentResult pay(double amount, String currency) {
        int paise = (int)(amount * 100);
        String txnId = razorpay.initiateCharge(paise, currency);
        return new PaymentResult(txnId, true);
    }
}
```

**Alternative — Decorator Pattern for Ride Enhancements:**
```java
public abstract class RideDecorator extends Ride {
    protected Ride decoratedRide;
    // Add features like WiFi, AC, child seat as decorators
}

public class ACRide extends RideDecorator {
    @Override
    public double calculateFare() {
        return decoratedRide.calculateFare() + 50.0; // AC surcharge
    }
}
```

#### ❌ Behavioral Pattern — **MISSING (MANDATORY)**

No Behavioral pattern (Strategy, Observer, State, Command, Template Method, Iterator, Chain of Responsibility) is implemented.

**Suggested Implementation — Strategy Pattern for Fare Calculation:**

```java
// Strategy interface
interface FareStrategy {
    double calculateFare(double distance, double duration);
}

// Concrete strategies
class EconomyFareStrategy implements FareStrategy {
    public double calculateFare(double distance, double duration) {
        return 50 + distance * 12 + duration * 1.5;
    }
}

class PremiumFareStrategy implements FareStrategy {
    public double calculateFare(double distance, double duration) {
        return 100 + distance * 20 + duration * 3.0;
    }
}

class SurgeFareStrategy implements FareStrategy {
    private double surgeMultiplier;
    public double calculateFare(double distance, double duration) {
        return (50 + distance * 15 + duration * 2) * surgeMultiplier;
    }
}

// Context
class Ride {
    private FareStrategy fareStrategy;
    public void setFareStrategy(FareStrategy strategy) { ... }
    public double calculateFare() { return fareStrategy.calculateFare(distance, duration); }
}
```

**Alternative — Observer Pattern for Ride Status Notifications:**
```java
interface RideObserver {
    void onRideStatusChanged(Ride ride, RideStatus newStatus);
}

class NotificationService implements RideObserver {
    public void onRideStatusChanged(Ride ride, RideStatus newStatus) {
        // Send push notification to rider/driver
    }
}

class Ride {
    private List<RideObserver> observers = new ArrayList<>();
    public void setStatus(RideStatus status) {
        this.status = status;
        observers.forEach(o -> o.onRideStatusChanged(this, status));
    }
}
```

> [!CAUTION]
> **Pattern Score**: 4/10. Four creational patterns are implemented, but the **mandatory Structural and Behavioral pattern categories are entirely missing**. An examiner will deduct heavily for this.

---

## 4. MVC ARCHITECTURE (Spring Boot)

### 4.1 Layer Assessment

| Layer | Expected | Actual | Verdict |
|---|---|---|---|
| **Controller** (REST endpoints) | `@RestController`, `@GetMapping`, etc. | Plain Java classes with no annotations, no HTTP endpoints | ❌ **NOT Spring Boot** |
| **Service** (business logic) | `@Service` classes | **Completely absent** — logic split between controllers and `RideBookingSystem` | ❌ **Missing** |
| **Repository** (data access) | `@Repository` / JPA `CrudRepository` | **Completely absent** — `HashMap` stores in `RideBookingSystem` | ❌ **Missing** |
| **Model** (entities) | `@Entity` with JPA annotations | Plain POJOs, no JPA, no `@Entity`, no `@Id` | ❌ **Not JPA** |
| **View** | REST JSON responses or Thymeleaf | Listed in docs as `DriverUI`, `CustomerUI`, `AdminUI` but **none exist in code** | ❌ **Missing** |

### 4.2 Separation of Concerns Violations

```
╔══════════════════════════════════════════════════════════════════╗
║  WHAT THE PROJECT CLAIMS          vs.    WHAT ACTUALLY EXISTS   ║
╠══════════════════════════════════════════════════════════════════╣
║  Spring Boot MVC                        Plain Java + main()    ║
║  REST Endpoints                         Method calls in Demo   ║
║  Service Layer                          RideBookingSystem God  ║
║  JPA Repository                         HashMap<String, ...>   ║
║  Database Persistence                   In-memory only         ║
║  View Layer (UI classes)                Not implemented         ║
╚══════════════════════════════════════════════════════════════════╝
```

### 4.3 Direct Logic in Controllers

```java
// RideController.java — contains business logic directly
public boolean completeRide(String rideId, double distance) {
    Ride ride = rideSystem.getRide(rideId);
    ride.setDistance(distance);
    ride.setStatus(Ride.RideStatus.COMPLETED);
    ride.getDriver().setStatus(Driver.DriverStatus.AVAILABLE);  // Mutating Driver!
    ride.getDriver().incrementCompletedRides();                   // Business logic!
    return true;
}
```

This should be in a **RideService** class, not the controller.

> [!WARNING]
> **MVC Score**: 2/10. The project labels its folders as "models" and "controllers" but has **no Spring Boot**, **no Service layer**, **no Repository layer**, **no View layer**, and **no database**. The "MVC" is superficial folder naming only.

---

## 5. FUNCTIONAL CORRECTNESS

### 5.1 Use Case Implementation Status

| Use Case | Status | Details |
|---|---|---|
| **Book Ride** | ⚠️ Partial | `RideController.requestRide()` creates a `Ride`, but no `Booking` entity, no Rider involved |
| **Accept/Reject Ride** | ❌ Missing | No `acceptRide()` or `rejectRide()` in `Driver`. `assignDriver()` is forced assignment by controller. |
| **Cancel Ride** | ❌ Missing | No `cancelRide()` method anywhere. `CANCELLED` status exists in enum but is never used. |
| **Fare Calculation** | ⚠️ Partial | `Fare` class calculates, but disconnected from `RideType.calculateFare()`. Two parallel fare systems. |
| **Payment Processing** | ✅ Works | `PaymentController` + `PaymentGateway` interface — most complete feature |
| **Shared Ride Logic** | ❌ Missing | `SharedRide` has no `addPassenger()`, `splitFare()`, `maxPassengers`, `currentPassengers`. It only sets `baseFare` and `capacity`. |
| **Invoice Generation** | ✅ Works | `Invoice` class with `printInvoice()` method |

### 5.2 Critical Logic Bugs

1. **`completeRide()` — No null check on driver**:
```java
public boolean completeRide(String rideId, double distance) {
    Ride ride = rideSystem.getRide(rideId);
    if (ride == null) return false;
    ride.getDriver().setStatus(Driver.DriverStatus.AVAILABLE);  // NPE if driver is null!
}
```

2. **`rateDriver()` and `rateCustomer()` — Stub implementations**:
```java
public boolean rateDriver(String customerId, String driverId, double rating) {
    if (rating < 1.0 || rating > 5.0) return false;
    // Update driver rating   ← COMMENT ONLY, no actual implementation!
    return true;
}
```

3. **`getRideFareDetails()` — Hardcoded duration**:
```java
public Fare getRideFareDetails(String rideId) {
    double durationMinutes = 10; // Default, should be calculated from ride times
    return new Fare(ride.getDistance(), durationMinutes);
}
```

4. **Fare overwrites in demo** — `fareController.updateRideFare()` is called, then immediately overwritten:
```java
fareController.updateRideFare(ride.getRideId(), 22.5, 35.5);  // Calculates fare
ride.setFare(450.0);  // Immediately overwrites with hardcoded value!
```

> **Functional Score**: 3/10. Only 2 of 6 core use cases work fully. Critical logic bugs exist.

---

## 6. CODE QUALITY & BEST PRACTICES

### 6.1 Naming Conventions

| Aspect | Assessment |
|---|---|
| Class names | ✅ PascalCase, descriptive |
| Method names | ✅ camelCase, verb-based |
| Variable names | ✅ Descriptive |
| Package structure | ⚠️ No Java packages declared — all files are in default package |
| Constants | ✅ `UPPER_SNAKE_CASE` in `Fare` |

> [!WARNING]
> **All Java files are in the default package** — no `package` declarations anywhere. This means the code **cannot be properly compiled** as a real project and is a disqualifying issue for any production-grade assessment.

### 6.2 Code Duplication

- `FareController.getRideFareDetails()` and `FareController.displayFareBreakdown()` duplicate the same "create Fare from ride" logic with the same hardcoded `10` minute duration.
- `RidePrototype` duplicates most fields of `Ride` — both have `rideId`, `customer`, `pickupLocation`, `dropLocation`, `status`, `distance`, `fare`.

### 6.3 Exception Handling

| Assessment |
|---|
| ❌ No custom exceptions — only `IllegalArgumentException` and `IllegalStateException` used |
| ❌ `RidePrototype.clone()` silently returns `null` on `CloneNotSupportedException` |
| ❌ Methods return `null` on failure (`PaymentController.processPayment`) — should throw or return `Optional` |
| ❌ No validation framework |

### 6.4 Use of Interfaces and Abstraction

| Good | Bad |
|---|---|
| `PaymentGateway` interface ✅ | All controllers depend on concrete `RideBookingSystem` |
| `User` abstract class ✅ | `RideType` abstract class ✅ |
| | No service interfaces |
| | No repository interfaces |

### 6.5 Modularity

- **No Java packages** — all classes compile into the default package.
- **Folder structure** exists but has no corresponding `package` declarations.
- `PaymentController.java` defines 3 classes/interfaces in one file (controller + interface + concrete implementation).

> **Code Quality Score**: 4/10.

---

## 7. PROJECT COMPLETENESS (OOAD GUIDELINES)

| Requirement | Status |
|---|---|
| At least 4 major use cases implemented | ⚠️ Partial — only ~2 fully work (Book Ride incomplete, Payment, Invoice) |
| Proper layering and integration | ❌ No Service layer, no Repository layer |
| Database usage (JPA or equivalent) | ❌ **No database at all** — everything is in-memory `HashMap` |
| Readiness for demo | ⚠️ Console demo works (`RideBookingSystemDemo.java` compiles and runs) |
| UML diagram consistency | ❌ ~30% match only |
| 1 Creational + 1 Structural + 1 Behavioral pattern | ❌ Only Creational (4 patterns, but 0 Structural, 0 Behavioral) |
| Spring Boot / MVC framework | ❌ Not a Spring Boot project |
| View layer | ❌ Listed in docs but not implemented |

---

## 8. STRUCTURED EVALUATION

### ✅ Strengths

1. **Four well-implemented Creational patterns** — Singleton, Factory, Builder, and Prototype are correctly implemented with proper documentation.
2. **`PaymentGateway` interface** — The only example of Dependency Inversion, but it's done well.
3. **Demo class is comprehensive** — `RideBookingSystemDemo.java` demonstrates all patterns clearly with a good end-to-end flow.
4. **Code readability** — Consistent formatting, decent comments, clear naming.
5. **`RideType` hierarchy** — Clean abstract class with proper polymorphism via `calculateFare()`.
6. **Documentation** — `DESIGN_PATTERNS_DOCUMENTATION.md` is thorough and well-written.

### 🔴 Critical Issues (Must Fix)

| # | Issue | Impact |
|---|---|---|
| 1 | **7 of 13 classes from class diagram are missing** (`Rider`, `Booking`, `Vehicle`, `Location`, `FareEstimator`, `RideOption`, `MultiModalTrip`, `MetroService`) | Diagram-code mismatch = automatic deduction |
| 2 | **No Structural pattern** (mandatory) | Missing category = pattern requirement not met |
| 3 | **No Behavioral pattern** (mandatory) | Missing category = pattern requirement not met |
| 4 | **No Spring Boot / No actual MVC** | MVC requirement not met |
| 5 | **No database / No JPA** | Persistence requirement not met |
| 6 | **No Java packages** — all classes in default package | Cannot be compiled as a proper project |
| 7 | **`SharedRide extends RideType`** instead of **`SharedRide extends Ride`** | Inheritance hierarchy doesn't match diagram |

### ⚠️ Design Violations

1. **God Object** — `RideBookingSystem` handles 5+ responsibilities (SRP violation)
2. **Two parallel, disconnected Ride hierarchies** — `Ride` (model) and `RideType` (pattern) never interact
3. **OCP Violation** — Factory uses `switch` statement requiring modification for new types
4. **LSP Violation** — `SharedRide` can't substitute `Ride` because they're in different hierarchies
5. **No abstraction for system access** — all controllers directly depend on `RideBookingSystem` concrete class

### ❌ Missing Patterns/Principles

| Missing | Category | Suggestion |
|---|---|---|
| **Strategy Pattern** | Behavioral | Fare calculation strategies (Economy, Premium, Surge) |
| **Observer Pattern** | Behavioral | Ride status change notifications |
| **State Pattern** | Behavioral | Ride state machine (REQUESTED → ASSIGNED → IN_PROGRESS → COMPLETED) |
| **Adapter Pattern** | Structural | Payment gateway adapter for multiple providers |
| **Decorator Pattern** | Structural | Ride enhancements (AC, WiFi, child seat) |
| **Facade Pattern** | Structural | Simplified interface to the booking subsystem |

### 💡 Suggestions for Improvement

1. **Add `package` declarations** to all Java files immediately — this is a 5-minute fix.
2. **Implement at least one Structural pattern** — Adapter for PaymentGateway is easiest since the interface already exists.
3. **Implement at least one Behavioral pattern** — Strategy for fare calculation is the natural fit.
4. **Create missing classes** to match the class diagram: `Booking`, `Location`, `Vehicle` at minimum.
5. **Rename `Customer` to `Rider`** to match the class diagram (or update the diagram).
6. **Make `SharedRide extend Ride`** instead of `RideType` to match the diagram.
7. **Add a Service layer** between controllers and `RideBookingSystem`.
8. **Split `RideBookingSystem`** into separate repository classes.
9. **Fix the `cancelRide` use case** — add the method.
10. **Fix null-safety bugs** in `completeRide()` and rating methods.

---

## 📊 Final Scoring

| Criterion | Weight | Score | Weighted |
|---|---|---|---|
| 1. UML Class Diagram Consistency | 15% | 3/10 | 0.45 |
| 2. SOLID Principles | 15% | 3/10 | 0.45 |
| 3. Design Patterns (Creational + Structural + Behavioral) | 20% | 4/10 | 0.80 |
| 4. MVC Architecture | 15% | 2/10 | 0.30 |
| 5. Functional Correctness | 15% | 3/10 | 0.45 |
| 6. Code Quality & Best Practices | 10% | 4/10 | 0.40 |
| 7. Project Completeness | 10% | 3/10 | 0.30 |
| **TOTAL** | **100%** | | **3.15 / 10** |

### Grade Interpretation

| Score Range | Grade | Interpretation |
|---|---|---|
| 9-10 | A+ | Exceptional |
| 8-8.9 | A | Excellent |
| 7-7.9 | B | Good |
| 6-6.9 | C | Satisfactory |
| 5-5.9 | D | Below Average |
| **3-4.9** | **F** | **Failing — Significant gaps** |
| 0-2.9 | F- | Incomplete |

> [!CAUTION]
> ## Final Verdict: **3.15/10 — F (Failing)**
> 
> The project demonstrates **conceptual understanding of creational design patterns** but fails to meet the core OOAD mini-project requirements:
> - Class diagram is not faithfully implemented (~30% match)
> - Two of three mandatory pattern categories (Structural, Behavioral) are missing
> - No actual Spring Boot/MVC — just console application with "Controller" naming
> - No database persistence
> - Multiple SOLID violations
> - Critical use cases are incomplete or missing
> 
> **To pass (≥6/10)**: Must add Structural + Behavioral patterns, implement missing diagram classes, add package declarations, fix inheritance hierarchy, and implement cancel ride + shared ride logic. Database/Spring Boot would push toward a B grade.
