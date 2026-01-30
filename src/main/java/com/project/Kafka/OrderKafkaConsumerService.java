package com.project.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class OrderKafkaConsumerService {

    private final smsService smsService;

    // ✅ Constructor Injection
    public OrderKafkaConsumerService(smsService smsService) {
        this.smsService = smsService;
    }

    @KafkaListener(topics = "order-topic", groupId = "ecommerce-group")
    public void consumeOrder(OrderEvent event) {

        System.out.println("📦 Order Event → "
                + event.getOrderId() + " : " + event.getStatus());

        String message = "";

        switch (event.getStatus()) {

            case "PAID":
                message = "✅ Order Confirmed!\n"
                        + "Order ID: " + event.getOrderId();
                break;

            case "PREPARING":
                message = "👨‍🍳 Your order is being prepared\n"
                        + "Order ID: " + event.getOrderId();
                break;

            case "OUT_FOR_DELIVERY":
                message = "🛵 Your order is on the way\n"
                        + "Order ID: " + event.getOrderId();
                break;

            case "DELIVERED":
                message = "🎉 Order Delivered\n"
                        + "Order ID: " + event.getOrderId();
                break;

            case "CANCELLED":
                message = "❌ Order Cancelled\n"
                        + "Order ID: " + event.getOrderId();
                break;

            default:
                message = "📦 Order Update\n"
                        + "Order ID: " + event.getOrderId();
        }

        // ✅ SEND SMS
        smsService.sendSms(event.getPhone(), message);
    }
}