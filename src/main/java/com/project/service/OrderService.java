package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.entity.Address;
import com.project.entity.CartItem;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.entity.User;
import com.project.enums.OrderStatus;
import com.project.repository.CartItemRepository;
import com.project.repository.OrderRepository;







@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final AddressService addressService;

    public OrderService(OrderRepository orderRepository,
                        CartItemRepository cartItemRepository,
                        NotificationService notificationService,
                        PaymentService paymentService,
                        UserService userService,
                        AddressService addressService) {

        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.notificationService = notificationService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.addressService = addressService;
    }

    // ================= PAYMENT SUCCESS =================
    public Order handlePaymentSuccess(
            String paymentId,
            String razorpayOrderId,
            String signature) {

        if (!paymentService.verifySignature(
                razorpayOrderId, paymentId, signature)) {
            throw new RuntimeException("Invalid payment");
        }

        User user = userService.getLoggedInUser();
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        // 🔥 GET SELECTED ADDRESS
        Address address = addressService.getLatestAddress(user);

        if (address == null) {
            throw new RuntimeException("No delivery address found");
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart empty");
        }

        double total = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // 🔥 CREATE ORDER
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
      
        order.setTotalAmount(total);
        order.setDeliveryFee(30);
        order.setPaymentMethod("ONLINE");
        order.setPaymentId(paymentId);
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepository.save(order);

        // 🔥 SAVE ORDER ITEMS
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(item);
        }

        order.setItems(orderItems);
        orderRepository.save(order);

        // 🔥 CLEAR CART
        cartItemRepository.deleteAll(cartItems);

        // 🔔 NOTIFICATIONS
        notificationService.notifyUserOrderSuccess(user, order);
        notificationService.notifyAdminNewOrder(order);

        return order;
    }

    // ================= TRACK ORDER =================
    public Order getLatestOrderForUser() {
        User user = userService.getLoggedInUser();
        if (user == null) return null;

        return orderRepository
                .findFirstByUserOrderByCreatedAtDesc(user)
                .orElse(null);
    }

    // ================= CANCEL ORDER =================
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order already delivered");
        }

        if (order.getStatus() == OrderStatus.CANCELLED ||
            order.getStatus() == OrderStatus.REFUNDED) {
            return;
        }

        // ✅ 1️⃣ CANCEL ORDER FIRST (ALWAYS)
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledBy("USER");
        orderRepository.save(order);

        // ✅ 2️⃣ TRY REFUND (OPTIONAL)
        if ("ONLINE".equals(order.getPaymentMethod())) {
            paymentService.refundPayment(
                    order.getPaymentId(),
                    order.getTotalAmount()
            );
        }

        // 🔔 3️⃣ NOTIFICATIONS
        notificationService.notifyUserOrderCancelled(order);
        notificationService.notifyAdminOrderCancelled(order);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}