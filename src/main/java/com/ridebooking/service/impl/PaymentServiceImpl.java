package com.ridebooking.service.impl;

import com.ridebooking.exception.PaymentFailedException;
import com.ridebooking.exception.RideNotFoundException;
import com.ridebooking.model.Payment;
import com.ridebooking.model.Ride;
import com.ridebooking.model.enums.PaymentMode;
import com.ridebooking.model.enums.PaymentStatus;
import com.ridebooking.pattern.adapter.PaymentGateway;
import com.ridebooking.repository.PaymentRepository;
import com.ridebooking.repository.RideRepository;
import com.ridebooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * PaymentServiceImpl — SRP: Only handles payment processing.
 * DIP: Depends on PaymentGateway interface (not concrete class).
 * Integrates ADAPTER PATTERN for external payment gateway.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RideRepository rideRepository;
    private final PaymentGateway paymentGateway;  // ADAPTER PATTERN — DIP

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RideRepository rideRepository,
                              @Qualifier("razorpayAdapter") PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.rideRepository = rideRepository;
        this.paymentGateway = paymentGateway;
    }

    /**
     * USE CASE: processPayment()
     * ADAPTER PATTERN: PaymentGateway adapts external Razorpay SDK.
     */
    @Override
    public Payment processPayment(Long rideId, PaymentMode mode) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found: " + rideId));

        Payment payment = new Payment(ride.getFare(), mode, ride);

        // ADAPTER PATTERN — delegates to adapted external gateway
        boolean success = paymentGateway.processPayment(payment.getAmount(), mode.name());

        if (success) {
            payment.processPayment(); // Sets status = SUCCESS + timestamp
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new PaymentFailedException("Payment failed for ride: " + rideId);
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByRide(Long rideId) {
        Payment payment = paymentRepository.findByRideRideId(rideId);
        if (payment == null) {
            throw new RideNotFoundException("Payment not found for ride: " + rideId);
        }
        return payment;
    }

    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        boolean refunded = paymentGateway.refundPayment(payment.getAmount());
        if (refunded) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
        }

        return paymentRepository.save(payment);
    }
}
