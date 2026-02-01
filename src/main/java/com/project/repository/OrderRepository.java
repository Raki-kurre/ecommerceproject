package com.project.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.entity.Order;
import com.project.entity.User;
import com.project.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	 @Query("""
		        SELECT DISTINCT o
		        FROM Order o
		        JOIN FETCH o.user
		        LEFT JOIN FETCH o.items i
		        LEFT JOIN FETCH i.product
		        LEFT JOIN FETCH o.address
		        ORDER BY o.createdAt DESC
		    """)
		List<Order> findAllForAdminView();
//		Optional<Order> findOrderWithItems(Long orderId);
	
    // ✅ User latest order
    Optional<Order> findFirstByUserOrderByCreatedAtDesc(User user);

    Optional<Order> findTopByUserOrderByCreatedAtDesc(User user);

    // Optional admin filters
    List<Order> findByStatus(OrderStatus status);

    // User order history
    List<Order> findByUser(User user);
    
}