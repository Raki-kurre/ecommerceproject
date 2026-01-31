package com.project.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
    name = "spring.kafka.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

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