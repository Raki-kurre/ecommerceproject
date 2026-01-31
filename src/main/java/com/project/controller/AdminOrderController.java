package com.project.controller;

import org.springframework.beans.factory.ObjectProvider;
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
    private final KafkaProducerService kafkaProducerService; // optional

    public AdminOrderController(
            OrderRepository orderRepository,
            ObjectProvider<KafkaProducerService> kafkaProducerServiceProvider) {

        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerServiceProvider.getIfAvailable();
    }

    // 📋 LIST ORDERS (ADMIN)
    @GetMapping
    @Transactional(readOnly = true)
    public String list(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin";
    }

    // 🔄 UPDATE ORDER STATUS
    @PostMapping("/{id}/status")
    @Transactional
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        // Kafka optional (won’t crash if disabled)
        if (kafkaProducerService != null) {
            kafkaProducerService.sendOrderEvent(
                new OrderEvent(
                    order.getId(),
                    status.name(),
                    order.getUser().getPhone()
                )
            );
        }

        return "redirect:/admin/orders";
    }
}