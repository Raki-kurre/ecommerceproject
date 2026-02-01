package com.project.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.entity.Order;
import com.project.enums.OrderStatus;
import com.project.repository.OrderRepository;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderRepository orderRepository;

    public AdminOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /* ================= LIST ORDERS ================= */

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAllForAdminView());
        return "admin";
    }

    /* ================= DELETE ORDER ================= */

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/admin/orders";
    }

    /* ================= UPDATE STATUS ================= */

    @PostMapping("/status/{id}")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/admin/orders";
    }
}