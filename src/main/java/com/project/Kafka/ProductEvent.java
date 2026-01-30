package com.project.Kafka;

public class ProductEvent {

    private long productId;
    private String productName;
    private double price;
    private String action;

    public ProductEvent() {}

    public ProductEvent(long productId, String productName, double price, String action) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.action = action;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getAction() {
        return action;
    }
}
