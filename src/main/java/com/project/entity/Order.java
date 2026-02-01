package com.project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.project.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= USER =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;
    
    public String getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	@Column(name = "cancelled_by")
    private String cancelledBy;

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

	public void setId(Long id) {
		this.id = id;
	}
  
	// ================= ORDER ITEMS (ONLY THIS) =================
	@OneToMany(mappedBy = "order",
	           cascade = CascadeType.ALL,
	           orphanRemoval = true)
	private List<OrderItem> items;

    // ================= PAYMENT =================
    @Column(nullable = false)
    private double totalAmount;

    private double deliveryFee;

    @Column(nullable = false)
    private String paymentMethod;

    private String paymentId;

    // ================= STATUS =================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // ================= TIME =================
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // ================= GETTERS / SETTERS =================
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getEstimatedDeliveryTime() {
        return createdAt.plusMinutes(30);
    }
}