package com.project.service;

import org.springframework.stereotype.Service;

import com.project.entity.Address;

@Service
public class DeliveryFeeService {

    // 🔴 FIXED SHOP LOCATION (example: Hyderabad store)
    private static final double SHOP_LAT = 17.3850;
    private static final double SHOP_LNG = 78.4867;

    // ₹1 per KM
    private static final double PRICE_PER_KM = 1.0;

    public double calculate(Address address) {

        if (address == null) return 0;

        double userLat = address.getLatitude();
        double userLng = address.getLongitude();

        double distanceKm = calculateDistance(
                SHOP_LAT, SHOP_LNG,
                userLat, userLng
        );

        // 🔥 Swiggy-like logic
        return Math.ceil(distanceKm * PRICE_PER_KM);
    }

    // ✅ HAVERSINE FORMULA (REAL DISTANCE)
    private double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2) {

        final int R = 6371; // Earth radius in KM

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}