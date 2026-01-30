package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.CartItem;
import com.project.entity.Product;
import com.project.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);
    long countByUser(User user);
    int countByUserEmail(String email);
    void deleteAllByProduct_Id(Long productId);
}