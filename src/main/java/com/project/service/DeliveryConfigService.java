package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.DeliveryConfig;
import com.project.repository.DeliveryConfigRepository;

@Service
public class DeliveryConfigService {

    @Autowired
    private DeliveryConfigRepository repository;

    public DeliveryConfig getConfig() {

        List<DeliveryConfig> list = repository.findAll();

        if (list.isEmpty()) {
            // 🔥 DEFAULT CONFIG (NO CRASH)
            DeliveryConfig config = new DeliveryConfig();
            config.setBaseFee(40);
            config.setFreeRadiusKm(3);
            config.setPricePerKm(10);
            config.setRestaurantLat(17.3850);  // example
            config.setRestaurantLng(78.4867);  // example
            return config;
        }

        return list.get(0);
    }
}