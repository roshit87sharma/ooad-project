package com.ridebooking.pattern.adapter;

import org.springframework.stereotype.Component;

/**
 * ADAPTER: Adapts the external Razorpay SDK to our PaymentGateway interface.
 *
 * Structure:
 *   PaymentGateway (Target)  ←  RazorpayAdapter (Adapter)  →  RazorpaySDK (Adaptee)
 *
 * PaymentServiceImpl depends on PaymentGateway interface (DIP).
 * This adapter translates our calls into Razorpay's API format.
 */
@Component("razorpayAdapter")
public class RazorpayAdapter implements PaymentGateway {

    // Simulated external Razorpay SDK (Adaptee)
    private final RazorpaySDK razorpaySDK = new RazorpaySDK();

    @Override
    public boolean processPayment(double amount, String method) {
        // ADAPTATION: Convert rupees to paise and call Razorpay's API
        int paiseAmount = (int) (amount * 100);
        String orderId = razorpaySDK.createOrder(paiseAmount, "INR");
        return razorpaySDK.capturePayment(orderId);
    }

    @Override
    public boolean refundPayment(double amount) {
        int paiseAmount = (int) (amount * 100);
        return razorpaySDK.initiateRefund(paiseAmount);
    }

    @Override
    public String getGatewayName() {
        return "Razorpay";
    }

    /**
     * Inner class simulating external Razorpay SDK (the ADAPTEE).
     * In a real project, this would be the actual Razorpay Java SDK.
     */
    private static class RazorpaySDK {

        public String createOrder(int amountInPaise, String currency) {
            System.out.println("[Razorpay SDK] Creating order: "
                    + amountInPaise + " " + currency);
            return "rzp_order_" + System.currentTimeMillis();
        }

        public boolean capturePayment(String orderId) {
            System.out.println("[Razorpay SDK] Payment captured: " + orderId);
            return true;
        }

        public boolean initiateRefund(int amountInPaise) {
            System.out.println("[Razorpay SDK] Refund initiated: " + amountInPaise);
            return true;
        }
    }
}
