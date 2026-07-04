// Customer Controller - Manages customer operations

public class CustomerController {
    private RideBookingSystem rideSystem;

    public CustomerController(RideBookingSystem rideSystem) {
        this.rideSystem = rideSystem;
    }

    public Customer registerCustomer(String userId, String name, String email, String phone) {
        Customer customer = new Customer(userId, name, email, phone);
        rideSystem.registerCustomer(customer);
        return customer;
    }

    public boolean addWallet(String customerId, double amount) {
        Customer customer = rideSystem.getCustomer(customerId);
        if (customer == null || amount <= 0) {
            return false;
        }
        customer.addToWallet(amount);
        return true;
    }

    public boolean setPaymentMethod(String customerId, String method) {
        Customer customer = rideSystem.getCustomer(customerId);
        if (customer == null) {
            return false;
        }
        customer.setPaymentMethod(method);
        return true;
    }

    public double getWalletBalance(String customerId) {
        Customer customer = rideSystem.getCustomer(customerId);
        if (customer == null) {
            return -1;
        }
        return customer.getWalletBalance();
    }

    public boolean rateDriver(String customerId, String driverId, double rating) {
        if (rating < 1.0 || rating > 5.0) {
            return false;
        }
        // Update driver rating
        return true;
    }

    public Customer getCustomerDetails(String customerId) {
        return rideSystem.getCustomer(customerId);
    }
}
