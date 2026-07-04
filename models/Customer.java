// Customer Model

public class Customer extends User {
    private String paymentMethod;
    private double walletBalance;
    private int totalRides;

    public Customer(String userId, String name, String email, String phone) {
        super(userId, name, email, phone);
        this.paymentMethod = "CARD";
        this.walletBalance = 0.0;
        this.totalRides = 0;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public double getWalletBalance() { return walletBalance; }
    public int getTotalRides() { return totalRides; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void addToWallet(double amount) { this.walletBalance += amount; }
    public void deductFromWallet(double amount) { this.walletBalance -= amount; }
    public void incrementTotalRides() { this.totalRides++; }

    public boolean hasEnoughBalance(double amount) {
        return walletBalance >= amount;
    }
}
