package com.ridebooking.controller;

import com.ridebooking.model.Payment;
import com.ridebooking.model.enums.PaymentMode;
import com.ridebooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PaymentController — REST endpoints for payment processing.
 * Delegates to PaymentService which uses Adapter Pattern.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** POST /api/payments/ride/{rideId} — USE CASE: processPayment() */
    @PostMapping("/ride/{rideId}")
    public ResponseEntity<Payment> processPayment(@PathVariable Long rideId,
                                                  @RequestParam PaymentMode mode) {
        return ResponseEntity.ok(paymentService.processPayment(rideId, mode));
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<Payment> getPaymentByRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(paymentService.getPaymentByRide(rideId));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}
