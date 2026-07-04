// Payment Model

import java.time.LocalDateTime;

public class Payment {
    public enum PaymentStatus {
        PENDING, PROCESSING, SUCCESS, FAILED
    }

    private String paymentId;
    private String rideId;
    private double amount;
    private String method;
    private PaymentStatus status;
    private LocalDateTime transactionTime;

    public Payment(String paymentId, String rideId, double amount, String method) {
        this.paymentId = paymentId;
        this.rideId = rideId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

    public String getPaymentId() { return paymentId; }
    public String getRideId() { return rideId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getTransactionTime() { return transactionTime; }

    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setTransactionTime(LocalDateTime time) { this.transactionTime = time; }

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }
}
