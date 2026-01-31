package com.project.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service

public class PaymentService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    // =====================================
    // 1️⃣ CREATE RAZORPAY ORDER
    // =====================================
    public Order createOrder(double amount) {

        try {
            RazorpayClient client =
                    new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (amount * 100)); // paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt",
                    "order_rcpt_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            return client.orders.create(orderRequest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    // =====================================
    // 2️⃣ VERIFY PAYMENT SIGNATURE (SECURE)
    // =====================================
    public boolean verifySignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {
            String payload =
                    razorpayOrderId + "|" + razorpayPaymentId;

            Utils.verifySignature(
                    payload,
                    razorpaySignature,
                    razorpaySecret   // ✅ FIXED HERE
            );
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =====================================
    // 3️⃣ REFUND PAYMENT
    // =====================================
    public void refundPayment(String paymentId, double amount) {

        try {
            RazorpayClient client =
                    new RazorpayClient(razorpayKey, razorpaySecret);

            Payment payment = client.payments.fetch(paymentId);
            String status = payment.get("status");

            // 🔐 Razorpay rule
            if (!"captured".equals(status)) {
                System.out.println("⚠️ Payment not captured. Refund skipped.");
                return;
            }

            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", (int) (amount * 100));

            client.payments.refund(paymentId, refundRequest);
            System.out.println("✅ Refund initiated for payment " + paymentId);

        } catch (Exception e) {
            // ❗ DO NOT FAIL ORDER
            System.out.println("⚠️ Refund failed: " + e.getMessage());
        }
    }
}