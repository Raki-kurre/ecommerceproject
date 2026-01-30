package com.project.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.entity.Order;
import com.project.enums.OrderStatus;
import com.project.repository.OrderRepository;

@Component
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    public OrderStatusScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Runs every 2 minutes
    @Scheduled(fixedRate = 120000)
    public void updateOrderStatusAutomatically() {

        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {

            if (order.getStatus() == OrderStatus.PAID &&
                order.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {

                order.setStatus(OrderStatus.PREPARING);
            }

            else if (order.getStatus() == OrderStatus.PREPARING &&
                order.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {

                order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            }

            else if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY &&
                order.getCreatedAt().plusMinutes(30).isBefore(LocalDateTime.now())) {

                order.setStatus(OrderStatus.DELIVERED);
            }
        }

        orderRepository.saveAll(orders);
    }
}
