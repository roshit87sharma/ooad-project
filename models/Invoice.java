// Invoice Model

import java.time.LocalDateTime;

public class Invoice {
    private String invoiceId;
    private String rideId;
    private String customerId;
    private String driverId;
    private Fare fare;
    private Payment payment;
    private LocalDateTime issueDate;

    public Invoice(String invoiceId, String rideId, String customerId, 
                   String driverId, Fare fare, Payment payment) {
        this.invoiceId = invoiceId;
        this.rideId = rideId;
        this.customerId = customerId;
        this.driverId = driverId;
        this.fare = fare;
        this.payment = payment;
        this.issueDate = LocalDateTime.now();
    }

    public String getInvoiceId() { return invoiceId; }
    public String getRideId() { return rideId; }
    public String getCustomerId() { return customerId; }
    public String getDriverId() { return driverId; }
    public Fare getFare() { return fare; }
    public Payment getPayment() { return payment; }
    public LocalDateTime getIssueDate() { return issueDate; }

    public void printInvoice() {
        System.out.println("========== INVOICE ==========");
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Ride ID: " + rideId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Driver ID: " + driverId);
        System.out.println("Date: " + issueDate);
        System.out.println("\n" + fare.toString());
        System.out.println("\nPayment Status: " + payment.getStatus());
        System.out.println("============================");
    }
}
