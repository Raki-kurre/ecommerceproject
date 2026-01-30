package com.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.CartItem;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.repository.CartItemRepository;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    /* ================= GET CART ================= */
    public List<CartItem> getCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        return items == null ? new ArrayList<>() : items;
    }
    public int getCartCountByEmail(String email) {
        return cartItemRepository.countByUserEmail(email);
    }

    /* ================= CART COUNT (🔥 IMPORTANT) ================= */
    public long getCartCount(User user) {
        return cartItemRepository.countByUser(user);
    }

    /* ================= TOTAL PRICE ================= */
    public double getTotalPrice(User user) {
        double total = 0;
        for (CartItem item : getCart(user)) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    /* ================= ADD TO CART ================= */
    public void addToCart(User user, Product product) {

        Optional<CartItem> optionalItem =
                cartItemRepository.findByUserAndProduct(user, product);

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(1);
            cartItemRepository.save(item);
        }
    }

    /* ================= INCREASE ================= */
    public void increase(User user, Product product) {
        cartItemRepository.findByUserAndProduct(user, product)
                .ifPresent(item -> {
                    item.setQuantity(item.getQuantity() + 1);
                    cartItemRepository.save(item);
                });
    }

    /* ================= DECREASE ================= */
    public void decrease(User user, Product product) {
        cartItemRepository.findByUserAndProduct(user, product)
                .ifPresent(item -> {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        cartItemRepository.save(item);
                    } else {
                        cartItemRepository.delete(item);
                    }
                });
    }

    /* ================= REMOVE ================= */
    public void remove(User user, Product product) {
        cartItemRepository.findByUserAndProduct(user, product)
                .ifPresent(cartItemRepository::delete);
    }
}