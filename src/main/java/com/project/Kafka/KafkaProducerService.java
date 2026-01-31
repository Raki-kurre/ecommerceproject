package com.project.Kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
    name = "spring.kafka.enabled",
    havingValue = "true"
)
@ConditionalOnBean(KafkaTemplate.class)   // ⭐ EXTRA SAFETY
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // ✅ Constructor injection (BEST PRACTICE)
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCategoryEvent(CategoryEvent event) {
        kafkaTemplate.send("category-topic", event);
    }

    public void sendProductEvent(ProductEvent event) {
        kafkaTemplate.send("product-topic", event);
    }

    public void sendOrderEvent(OrderEvent event) {
        kafkaTemplate.send("order-topic", event);
    }
}