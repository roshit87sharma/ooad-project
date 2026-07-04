package com.ridebooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ridebooking.model.enums.PaymentMode;
import com.ridebooking.model.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Payment — matches UML class diagram.
 * Attributes: paymentId, amount, paymentMode.
 * Methods: processPayment().
 * Association: Ride → Payment (1 to 1).
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // UML: Ride → Payment (1 to 1). FK is on Payment side.
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id")
    @JsonIgnore  // Avoids circular JSON serialization
    private Ride ride;

    private LocalDateTime transactionTime;

    public Payment() {}

    public Payment(double amount, PaymentMode mode, Ride ride) {
        this.amount = amount;
        this.paymentMode = mode;
        this.ride = ride;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    // UML diagram method
    public void processPayment() {
        this.paymentStatus = PaymentStatus.SUCCESS;
        this.transactionTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode mode) { this.paymentMode = mode; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus status) { this.paymentStatus = status; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime time) { this.transactionTime = time; }
}
