package com.project.service;

import org.springframework.stereotype.Service;
import com.project.entity.Order;
import com.project.entity.User;

@Service
public class NotificationService {

    public void notifyUserOrderSuccess(User user, Order order) {
        System.out.println(
            "✅ User notified: Order placed successfully. Order ID = " + order.getId()
        );
    }

    public void notifyAdminNewOrder(Order order) {
        System.out.println(
            "📦 Admin notified: New order received. Order ID = " + order.getId()
        );
    }

    public void notifyUserOrderCancelled(Order order) {
        System.out.println(
            "❌ User notified: Order cancelled. Order ID = " + order.getId()
        );
    }

    public void notifyAdminOrderCancelled(Order order) {
        System.out.println(
            "🚨 Admin notified: Order cancelled. Order ID = " + order.getId()
        );
    }
}