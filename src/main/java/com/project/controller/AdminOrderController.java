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
    private final KafkaProducerService kafkaProducerService; // can be null

    public AdminOrderController(
            OrderRepository orderRepository,
            ObjectProvider<KafkaProducerService> kafkaProducerServiceProvider) {

        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerServiceProvider.getIfAvailable();
    }

    // 📋 LIST ORDERS
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderRepository.findAllOrdersForAdmin());
        return "admin";
    }

    // 🔄 UPDATE STATUS
    @PostMapping("/{id}/status")
    @Transactional
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        if (kafkaProducerService != null) {
            OrderEvent event = new OrderEvent(
                    order.getId(),
                    status.name(),
                    order.getUser().getPhone()
            );
            kafkaProducerService.sendOrderEvent(event);
        }

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