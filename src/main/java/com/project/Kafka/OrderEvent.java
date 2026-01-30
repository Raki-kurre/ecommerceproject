package com.project.Kafka;

import java.io.Serializable;

public class OrderEvent implements Serializable {

    private Long orderId;
    private String status;
    private String phone;

    public OrderEvent() {}

    public OrderEvent(Long orderId, String status, String phone) {
        this.orderId = orderId;
        this.status = status;
        this.phone = phone;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}


