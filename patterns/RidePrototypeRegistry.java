// Prototype Registry - manages prototypes

import java.util.*;

public class RidePrototypeRegistry {
    private static RidePrototypeRegistry instance;
    private Map<String, RidePrototype> prototypes;

    private RidePrototypeRegistry() {
        prototypes = new HashMap<>();
    }

    public static synchronized RidePrototypeRegistry getInstance() {
        if (instance == null) {
            instance = new RidePrototypeRegistry();
        }
        return instance;
    }

    // Register a prototype
    public void registerPrototype(String name, RidePrototype prototype) {
        prototypes.put(name, prototype);
        System.out.println("Prototype registered: " + name);
    }

    // Get a clone of registered prototype
    public RidePrototype getPrototype(String name) {
        RidePrototype prototype = prototypes.get(name);
        if (prototype == null) {
            System.out.println("Prototype not found: " + name);
            return null;
        }
        return prototype.clone();
    }

    // Get a clone with new ID
    public RidePrototype getPrototypeWithNewId(String prototypeName, String newRideId) {
        RidePrototype prototype = prototypes.get(prototypeName);
        if (prototype == null) {
            System.out.println("Prototype not found: " + prototypeName);
            return null;
        }
        return prototype.cloneWithNewId(newRideId);
    }

    // List all registered prototypes
    public void listPrototypes() {
        System.out.println("\n========== REGISTERED PROTOTYPES ==========");
        prototypes.forEach((name, proto) -> {
            System.out.println("✓ " + name + " - Pickup: " + proto.getPickupLocation() 
                             + " → Drop: " + proto.getDropLocation());
        });
        System.out.println("==========================================\n");
    }

    // Display prototype details
    public void displayPrototypeDetails(String name) {
        RidePrototype prototype = prototypes.get(name);
        if (prototype != null) {
            System.out.println("\n--- Prototype Details: " + name + " ---");
            System.out.println("Pickup: " + prototype.getPickupLocation());
            System.out.println("Drop: " + prototype.getDropLocation());
            System.out.println("Status: " + prototype.getStatus());
            System.out.println("Distance: " + prototype.getDistance());
            System.out.println("Fare: " + prototype.getFare());
            System.out.println("-----------------------------------\n");
        }
    }

    // Clear all prototypes
    public void clearPrototypes() {
        prototypes.clear();
        System.out.println("All prototypes cleared");
    }
}
