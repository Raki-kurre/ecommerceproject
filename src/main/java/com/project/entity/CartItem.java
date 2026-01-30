package com.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "product_id"})
       })
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 CART OWNER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔹 PRODUCT
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    /* ================= GETTERS / SETTERS ================= */

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* ================= CALCULATED ================= */

    @Transient
    public double getTotalPrice() {
        return product != null ? product.getPrice() * quantity : 0;
    }
}