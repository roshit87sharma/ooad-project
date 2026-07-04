// Payment Controller - Manages payment operations

public class PaymentController {
    private RideBookingSystem rideSystem;
    private PaymentGateway paymentGateway;

    public PaymentController(RideBookingSystem rideSystem, PaymentGateway paymentGateway) {
        this.rideSystem = rideSystem;
        this.paymentGateway = paymentGateway;
    }

    public Payment processPayment(String rideId, double amount, String method) {
        Ride ride = rideSystem.getRide(rideId);
        if (ride == null) {
            return null;
        }

        String paymentId = "PAY_" + System.currentTimeMillis();
        Payment payment = new Payment(paymentId, rideId, amount, method);

        if (paymentGateway.processTransaction(paymentId, amount, method)) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setTransactionTime(java.time.LocalDateTime.now());
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        return payment;
    }

    public Payment retryPayment(Payment payment) {
        if (payment.getStatus() == Payment.PaymentStatus.FAILED) {
            if (paymentGateway.processTransaction(payment.getPaymentId(), 
                    payment.getAmount(), payment.getMethod())) {
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                payment.setTransactionTime(java.time.LocalDateTime.now());
            }
        }
        return payment;
    }

    public boolean refundPayment(String paymentId, double amount) {
        return paymentGateway.refundTransaction(paymentId, amount);
    }

    public Payment getPaymentDetails(String paymentId) {
        return rideSystem.getPayment(paymentId);
    }
}

// Payment Gateway Interface
interface PaymentGateway {
    boolean processTransaction(String transactionId, double amount, String method);
    boolean refundTransaction(String transactionId, double amount);
}

// Concrete Payment Gateway Implementation
class ConcretePaymentGateway implements PaymentGateway {
    @Override
    public boolean processTransaction(String transactionId, double amount, String method) {
        // Simulate payment processing
        System.out.println("Processing payment: " + transactionId + " Amount: " + amount);
        return true; // Assume success
    }

    @Override
    public boolean refundTransaction(String transactionId, double amount) {
        System.out.println("Refunding payment: " + transactionId + " Amount: " + amount);
        return true;
    }
}
