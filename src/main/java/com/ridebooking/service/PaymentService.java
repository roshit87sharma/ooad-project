package com.ridebooking.service;

import com.ridebooking.model.Payment;
import com.ridebooking.model.enums.PaymentMode;

/**
 * PaymentService — DIP: Controllers depend on this interface.
 * Uses Adapter Pattern internally for payment gateway integration.
 */
public interface PaymentService {
    Payment processPayment(Long rideId, PaymentMode mode);
    Payment getPaymentByRide(Long rideId);
    Payment refundPayment(Long paymentId);
}
