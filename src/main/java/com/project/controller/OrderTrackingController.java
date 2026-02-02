package com.project.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.entity.Order;
import com.project.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderTrackingController {

    private final OrderService orderService;

    public OrderTrackingController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/track/{id}")
    public String trackOrder(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);

        if (order == null) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        return "order-track";
    }
}