package com.ridebooking.pattern.adapter;

/**
 * ═══════════════════════════════════════════════════════════
 *  STRUCTURAL DESIGN PATTERN: ADAPTER (Target Interface)
 * ═══════════════════════════════════════════════════════════
 * Unified interface for payment processing.
 * DIP: PaymentServiceImpl depends on this interface, not concrete gateways.
 * Adapts external payment SDKs (Razorpay, PayU, etc.) to our system.
 */
public interface PaymentGateway {

    /**
     * Process a payment transaction.
     * @param amount  payment amount
     * @param method  payment method (CARD, UPI, WALLET, CASH)
     * @return true if payment successful
     */
    boolean processPayment(double amount, String method);

    /**
     * Refund a previously processed payment.
     * @param amount  amount to refund
     * @return true if refund successful
     */
    boolean refundPayment(double amount);

    /** Returns the gateway provider name */
    String getGatewayName();
}
