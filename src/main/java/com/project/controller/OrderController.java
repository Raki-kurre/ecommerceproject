package com.project.controller;

import com.project.entity.Order;
import com.project.enums.OrderStatus;
import com.project.service.OrderService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // ✅ Constructor Injection (BEST PRACTICE)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ================= TRACK LATEST ORDER =================
    @GetMapping("/track")
    public String trackLatestOrder(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Order order = orderService.getLatestOrderForUser();

        if (order == null) {
            model.addAttribute("message", "No order found");
            return "orderTracking";
        }

        model.addAttribute("order", order);
        model.addAttribute("orderTime", order.getCreatedAt());
        model.addAttribute("deliveryTime",
                order.getCreatedAt().plusMinutes(30));

        if (order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.REFUNDED) {

            model.addAttribute(
                "cancelledMessage",
                "❌ Your order has been cancelled. Refund will be processed shortly."
            );
        }

        return "orderTracking";
    }

    // ================= TRACK ORDER BY ID =================
    @GetMapping("/track/{id}")
    public String trackOrderById(@PathVariable Long id,
                                 Model model,
                                 Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);

        if (order == null) {
            model.addAttribute("message", "No order found");
            return "orderTracking";
        }

        model.addAttribute("order", order);
        model.addAttribute("orderTime", order.getCreatedAt());
        model.addAttribute("deliveryTime",
                order.getCreatedAt().plusMinutes(30));

        if (order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.REFUNDED) {

            model.addAttribute(
                "cancelledMessage",
                "❌ Your order has been cancelled. Refund will be processed shortly."
            );
        }

        return "orderTracking";
    }

    // ================= CANCEL ORDER =================
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            // ✅ Matches your OrderService exactly
            orderService.cancelOrder(id);

            redirectAttributes.addFlashAttribute(
                "cancelledMessage",
                "❌ Your order has been cancelled successfully."
            );

        } catch (RuntimeException ex) {

            redirectAttributes.addFlashAttribute(
                "errorMessage",
                ex.getMessage()
            );
        }

        return "redirect:/orders/track/" + id;
    }
}