
package com.project.entity;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    // ✅ FOR NAVBAR COUNT
    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // ✅ ADD PRODUCT
    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem());
    }

    // ✅ REMOVE PRODUCT (FIXED)
    public void removeProduct(Long productId) {
        items.removeIf(item ->
                item.getProduct() != null &&
                item.getProduct().getId().equals(productId)
        );
    }

    // ✅ INCREASE
    public void increaseQuantity(Long productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
    }

    // ✅ DECREASE (REMOVE IF 0)
    public void decreaseQuantity(Long productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() <= 0) {
                    removeProduct(productId);
                }
                return;
            }
        }
    }

    public void clear() {
        items.clear();
    }
}