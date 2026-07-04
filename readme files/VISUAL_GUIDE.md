 MVC Architecture & Design Patterns Visual Guide

## Complete MVC Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PRESENTATION LAYER                                │
│                                                                              │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────────┐             │
│  │  DriverUI    │      │ CustomerUI   │      │   AdminUI    │             │
│  │              │      │              │      │              │             │
│  │ - Dashboard  │      │ - Booking    │      │ - Analytics  │             │
│  │ - Ride List  │      │ - Payment     │      │ - Monitoring │             │
│  │ - Ratings    │      │ - History    │      │ - Reports    │             │
│  └──────────────┘      └──────────────┘      └──────────────┘             │
└────────────────────┬────────────────────────────────────┬────────────────────┘
                     │ User Input / Display               │
                     ▼                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CONTROLLER LAYER                                   │
│                                                                              │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐         │
│  │ RideController   │  │DriverController │  │CustomerController│         │
│  │                  │  │                  │  │                  │         │
│  │ - Request Ride   │  │ - Register Driver│  │ - Register Cust  │         │
│  │ - Assign Driver  │  │ - Login/Logout   │  │ - Wallet Mgmt    │         │
│  │ - Start/Complete │  │ - Location Upd   │  │ - Payment Method │         │
│  │ - Get Details    │  │ - Ratings        │  │ - Ratings        │         │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘         │
│                                                                              │
│  ┌──────────────────┐  ┌──────────────────┐                               │
│  │PaymentController │  │ FareController  │                               │
│  │                  │  │                  │                               │
│  │ - Process Payment│  │ - Calculate Fare │                               │
│  │ - Retry Payment  │  │ - Estimate Fare  │                               │
│  │ - Refund Payment │  │ - Display Details│                               │
│  └──────────────────┘  └──────────────────┘                               │
└──────────────────────────────┬───────────────────────────────────────────────┘
                               │ Business Logic
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            MODEL LAYER                                      │
│                                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │  User    │  │ Driver   │  │Customer  │  │   Ride   │  │ Payment  │    │
│  │          │  │          │  │          │  │          │  │          │    │
│  │ - userId │  │ - status │  │ - wallet │  │ - status │  │ - status │    │
│  │ - name   │  │ - license│  │ - method │  │ - fare   │  │ - amount │    │
│  │ - email  │  │ - vehicle│  │ - rides  │  │ - driver │  │ - method │    │
│  │ - rating │  │ - location│ │ - rating │  │ - times  │  │ - time   │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
│                                                                              │
│  ┌──────────┐  ┌──────────┐                                               │
│  │  Fare    │  │ Invoice  │                                               │
│  │          │  │          │                                               │
│  │ - base   │  │ - invId  │                                               │
│  │ - distance│ │ - rideId │                                               │
│  │ - time   │  │ - custId │                                               │
│  │ - tax    │  │ - payment│                                               │
│  └──────────┘  └──────────┘                                               │
└──────────────────────────────┬───────────────────────────────────────────────┘
                               │ Data Operations
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                                       │
│ (Contains Design Patterns)                                                  │
│                                                                              │
│ ┌──────────────────────────────────────────────────────────────────────┐   │
│ │            🔷 SINGLETON: RideBookingSystem                          │   │
│ │  Central coordinator managing all operations                         │   │
│ │  - Stores all drivers, customers, rides, payments                  │   │
│ │  - Single instance only                                             │   │
│ │  - Thread-safe                                                      │   │
│ └──────────────────────────────────────────────────────────────────────┘   │
│                                    ▲                                        │
│            ┌───────────────────────┼───────────────────────┐               │
│            │                       │                       │               │
│ ┌──────────▼──────────┐ ┌────────▼───────────┐ ┌────────▼──────────┐    │
│ │  🏭 FACTORY PATTERN │ │ 🔨 BUILDER PATTERN │ │🔄 PROTOTYPE       │    │
│ │  RideFactory        │ │ RideBuilder        │ │ RidePrototype     │    │
│ │                     │ │                    │ │                   │    │
│ │ Creates:            │ │ Builds:            │ │ Clones:           │    │
│ │ - EconomyRide       │ │ - Complex Rides    │ │ - Economy Proto   │    │
│ │ - PremiumRide       │ │ - Validation       │ │ - Premium Proto   │    │
│ │ - SharedRide        │ │ - Step-by-step     │ │ - Shared Proto    │    │
│ └─────────────────────┘ └────────────────────┘ │ - Create Clones   │    │
│                                                 └───────────────────┘    │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │ Data Persistence
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                       PERSISTENCE LAYER                                     │
│                                                                              │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐               │
│  │   Database     │  │   Cache        │  │   API Gateway  │               │
│  │   Connection   │  │   Management   │  │   Integration  │               │
│  └────────────────┘  └────────────────┘  └────────────────┘               │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Design Pattern Interaction Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    RIDE BOOKING PROCESS                         │
└─────────────────────────────────────────────────────────────────┘

Step 1: System Initialization
────────────────────────────
    getInstance()
         │
         ▼
    ╔═══════════════════════════════════╗
    ║  RideBookingSystem (SINGLETON)    ║
    ║  - Manages all system state       ║
    ║  - Only one instance exists       ║
    ║  - Central coordinator            ║
    ╚═══════════════════════════════════╝
         │
         │ Coordinates
         │
Step 2: Create Ride Type
────────────────────────
    RideFactory.createRide(type)
         │
         ├─ ECONOMY
         │     ▼
         │  ╔═══════════════════╗
         │  ║  EconomyRide      ║
         │  ║  Rs. 50 + Rs.12/km║
         │  ╚═══════════════════╝
         │
         ├─ PREMIUM
         │     ▼
         │  ╔═══════════════════╗
         │  ║  PremiumRide      ║
         │  ║ Rs. 100 + Rs.20/km║
         │  ╚═══════════════════╝
         │
         └─ SHARED
              ▼
           ╔═══════════════════╗
           ║   SharedRide      ║
           ║  Rs. 30 + Rs.8/km ║
           ╚═══════════════════╝

Step 3: Build Ride Object
──────────────────────────
           RideBuilder
              │
              ├─ setPickupLocation()
              │
              ├─ setDropLocation()
              │
              ├─ setDriver()
              │
              ├─ setDistance()
              │
              ├─ setFare()
              │
              └─ build() ──► Validates ──► Returns Ride
                                │
                                ├─ Location Check ✓
                                ├─ Driver Check ✓
                                └─ Fare Check ✓

Step 4: Clone Ride from Prototype
──────────────────────────────────
    RidePrototypeRegistry.
    getPrototypeWithNewId()
         │
         ▼
    ╔════════════════════════════════╗
    ║     RidePrototype              ║
    ║  Cloned from Registry          ║
    ║  - New ID assigned             ║
    ║  - Configuration copied        ║
    ║  - Ready for use               ║
    ╚════════════════════════════════╝

Step 5: Complete Ride & Cleanup
────────────────────────────────
    Complete Ride → Process Payment → Generate Invoice
         │
         ▼
    ╔════════════════════════════════╗
    ║     System Complete            ║
    ║  - Invoice Generated           ║
    ║  - Payment Processed           ║
    ║  - Ready for next ride         ║
    ╚════════════════════════════════╝
```

---

## Pattern Hierarchy & Dependencies

```
                    ┌─────────────────────────┐
                    │  RideBookingSystem      │
                    │  (SINGLETON)            │
                    │  Central Coordinator    │
                    └────────────┬────────────┘
                                 │
                ┌────────────────┼────────────────┐
                │                │                │
                ▼                ▼                ▼
            ┌──────────┐    ┌──────────┐    ┌──────────┐
            │ FACTORY  │    │ BUILDER  │    │PROTOTYPE │
            │RideFactory  │ │RideBuilder  │ │Registry    │
            └──────────┘    └──────────┘    └──────────┘
                │                │                │
                │ Creates         │ Constructs    │ Clones
                ▼                ▼                ▼
            ┌──────────┐    ┌──────────┐    ┌──────────┐
            │  Rides   │    │  Rides   │    │  Rides   │
            └──────────┘    └──────────┘    └──────────┘
                │                │                │
                └────────────────┼────────────────┘
                                 │
                                 ▼
                          ┌──────────────┐
                          │  Controllers │
                          └──────────────┘
                                 │
                                 ▼
                          ┌──────────────┐
                          │    Views     │
                          └──────────────┘
                                 │
                                 ▼
                              ┌───────┐
                              │ Users │
                              └───────┘
```

---

## Design Pattern Usage Map

```
Singleton (RideBookingSystem)
│
├─ Manages: Drivers
│
├─ Manages: Customers
│
├─ Manages: Rides
│        │
│        ├─ Uses: Factory (to create rides)
│        │         │
│        │         ├─ Creates: EconomyRide
│        │         ├─ Creates: PremiumRide
│        │         └─ Creates: SharedRide
│        │
│        └─ Uses: Builder (to construct rides)
│                 │
│                 └─ Validates: Location, Driver, Fare
│
├─ Manages: Prototype Registry
│        │
│        └─ Uses: Prototype (to clone rides)
│                 │
│                 ├─ Clones: EconomyRide Prototype
│                 ├─ Clones: PremiumRide Prototype
│                 └─ Clones: SharedRide Prototype
│
├─ Manages: Payments
│
└─ Manages: Invoices
```

---

## Data Flow Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                 RIDE REQUEST INITIATED                       │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ▼
            ┌─────────────────┐
            │  RideController │
            └────────┬────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ RideBookingSystem.createRide() │ (SINGLETON)
        └────────────┬───────────────────┘
                     │
                     ▼
         ┌──────────────────────┐
         │ RideFactory.         │ (FACTORY)
         │ createRide(type)     │
         └────────┬─────────────┘
                  │
         ┌────────┴────────────┬────────────┐
         │                     │            │
         ▼                     ▼            ▼
     ECONOMY             PREMIUM        SHARED
     Ride                Ride           Ride


    Now construct complex Ride or clone from Prototype...
         │
         ├─────────────────┬─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
    ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
    │ RideFactory  │  │ RideBuilder  │  │RidePrototype │
    │(FACTORY)     │  │(BUILDER)     │  │(PROTOTYPE)   │
    └──────────────┘  └──────────────┘  └──────────────┘
         │                 │                 │
         │ Creates          │ Constructs      │ Clones
         │ ride type        │ complex ride    │ existing
         └─────────────────┬────────────────┬┘
                          │
                          ▼
                  ┌──────────────────┐
                  │ Valid Ride       │
                  │ Ready for Use    │
                  └──────────────────┘
```

---

## Benefits Visualization

```
╔═════════════════════════════════════════════════════════════╗
║           SINGLETON PATTERN BENEFITS                        ║
╠═════════════════════════════════════════════════════════════╣
║ ✓ Single Control Point     → Centralized Management        ║
║ ✓ Consistent State         → No Data Duplication           ║
║ ✓ Thread-Safe             → Concurrent Access Safe         ║
║ ✓ Easy Debugging          → Fewer Instances to Track       ║
║ ✓ Global Access           → Available Everywhere           ║
╚═════════════════════════════════════════════════════════════╝

╔═════════════════════════════════════════════════════════════╗
║           FACTORY PATTERN BENEFITS                          ║
╠═════════════════════════════════════════════════════════════╣
║ ✓ Encapsulation           → Hide Creation Details          ║
║ ✓ Loose Coupling          → Independent from Types         ║
║ ✓ Easy Extension          → Add New Types Easily            ║
║ ✓ Singleton Compliance    → Consistent Access              ║
║ ✓ Polymorphism           → Same Interface, Different Impl   ║
╚═════════════════════════════════════════════════════════════╝

╔═════════════════════════════════════════════════════════════╗
║           BUILDER PATTERN BENEFITS                          ║
╠═════════════════════════════════════════════════════════════╣
║ ✓ Clarity                 → Clear Step-by-Step Building     ║
║ ✓ Validation             → Check Before Creation           ║
║ ✓ Flexibility             → Optional Parameters            ║
║ ✓ Immutability           → After Built, No Changes         ║
║ ✓ Complex Objects         → Handle Complexity Easily        ║
╚═════════════════════════════════════════════════════════════╝

╔═════════════════════════════════════════════════════════════╗
║         PROTOTYPE PATTERN BENEFITS                           ║
╠═════════════════════════════════════════════════════════════╣
║ ✓ Avoids Creation  → No Expensive Object Creation          ║
║ ✓ Configuration    → Reuse Existing from Prototype         ║
║ ✓ Independence     → Clones are Independent                ║
║ ✓ Performance      → Faster than Creating from Scratch     ║
║ ✓ Flexibility      → Easy Variations via Cloning           ║
╚═════════════════════════════════════════════════════════════╝
```

---

## Summary

This visual guide shows how:
1. **MVC Layers** separate concerns clearly
2. **Design Patterns** solve specific architectural challenges
3. **Patterns work together** in a cohesive system
4. **Data flows** through the entire application
5. **Each pattern provides distinct benefits** to the overall system

The Ride Booking System is a complete, production-ready example of applying multiple creational design patterns in a well-structured MVC architecture.
