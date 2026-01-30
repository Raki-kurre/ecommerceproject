package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.Kafka.KafkaProducerService;
import com.project.Kafka.OrderEvent;
import com.project.entity.Order;
import com.project.enums.OrderStatus;
import com.project.repository.OrderRepository;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    public AdminOrderController(OrderRepository orderRepository,
                                KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    // 📋 LIST ORDERS
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderRepository.findAllOrdersForAdmin());
        return "admin";
    }

    // 🔄 UPDATE STATUS ✅ FIXED
    @PostMapping("/{id}/status")
    @Transactional   // ⭐⭐⭐ THIS LINE FIXES EVERYTHING
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        // ✅ SAFE NOW
        String phone = order.getUser().getPhone();

        OrderEvent event = new OrderEvent(
                order.getId(),
                status.name(),
                phone
        );

        kafkaProducerService.sendOrderEvent(event);

        return "redirect:/admin/orders";
    }

    // 🗑 DELETE ORDER
    @PostMapping("/delete/{id}")
    @Transactional
    public String deleteOrder(@PathVariable Long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/admin/orders";
        }

        order.setAddress(null);
        orderRepository.delete(order);

        return "redirect:/admin/orders";
    }
}