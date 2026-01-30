package com.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery_config")
public class DeliveryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double restaurantLat;
    private double restaurantLng;

    private double baseFee;        // eg: ₹30
    private double pricePerKm;     // eg: ₹10 per km
    private double freeRadiusKm;   // eg: 2 km
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getRestaurantLat() {
		return restaurantLat;
	}
	public void setRestaurantLat(double restaurantLat) {
		this.restaurantLat = restaurantLat;
	}
	public double getRestaurantLng() {
		return restaurantLng;
	}
	public void setRestaurantLng(double restaurantLng) {
		this.restaurantLng = restaurantLng;
	}
	public double getBaseFee() {
		return baseFee;
	}
	public void setBaseFee(double baseFee) {
		this.baseFee = baseFee;
	}
	public double getPricePerKm() {
		return pricePerKm;
	}
	public void setPricePerKm(double pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	public double getFreeRadiusKm() {
		return freeRadiusKm;
	}
	public void setFreeRadiusKm(double freeRadiusKm) {
		this.freeRadiusKm = freeRadiusKm;
	}

    // getters & setters
}
