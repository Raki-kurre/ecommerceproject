package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.project.service.OrderService;
import com.project.service.PaymentService;
import com.razorpay.Order;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService,
                             OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    // ===============================
    // CREATE RAZORPAY ORDER
    // ===============================
    @PostMapping("/create")
    @ResponseBody
    public String createPayment(@RequestParam int amount) {

        Order razorpayOrder = paymentService.createOrder(amount);
        return razorpayOrder.toString();
    }

    // ===============================
    // PAYMENT SUCCESS CALLBACK
    // ===============================
    @PostMapping("/success")
    public String handlePaymentSuccess(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_order_id") String orderId,
            @RequestParam("razorpay_signature") String signature) {

        com.project.entity.Order order = orderService.handlePaymentSuccess(
                paymentId,
                orderId,
                signature
        );

        // ✅ REDIRECT WITH ORDER ID
        return "redirect:/orders/track/" + order.getId();
    }
}