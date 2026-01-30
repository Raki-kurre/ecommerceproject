package com.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.entity.Address;
import com.project.entity.CartItem;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.service.AddressService;
import com.project.service.CartService;
import com.project.service.DeliveryFeeService;   // 🔥 ADD
import com.project.service.ProductService;
import com.project.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DeliveryFeeService deliveryFeeService; // 🔥 ADD

    /* ================= ADD TO CART ================= */
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productService.fetchbyId(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartService.addToCart(user, product);
        return "redirect:/cart";
    }

    /* ================= VIEW CART ================= */
    @GetMapping
    public String viewCart(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🛒 CART
        List<CartItem> cartItems = cartService.getCart(user);
        double cartTotal = cartService.getTotalPrice(user);

        // 📍 ADDRESS (FROM DB – PERSISTS AFTER RESTART)
        Address address = addressService.getLatestAddress(user);

        // 🚚 DELIVERY FEE (LIKE ZOMATO)
        double deliveryFee = 0;
        if (address != null) {
            deliveryFee = deliveryFeeService.calculate(address);
        }

        double grandTotal = cartTotal + deliveryFee;

        // ✅ SEND TO UI
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", cartTotal);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("address", address);
        model.addAttribute("user", user);   // 🔥 THIS WAS MISSING

        model.addAttribute("razorpayKeyId", "rzp_test_S88nKuBER0N9Gc");
        model.addAttribute("phone", user.getPhone());
        return "cart";
    }

    /* ================= INCREASE ================= */
    @GetMapping("/increase/{id}")
    public String increase(@PathVariable Long id, Principal principal) {

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productService.fetchbyId(id).orElseThrow();

        cartService.increase(user, product);
        return "redirect:/cart";
    }

    /* ================= DECREASE ================= */
    @GetMapping("/decrease/{id}")
    public String decrease(@PathVariable Long id, Principal principal) {

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productService.fetchbyId(id).orElseThrow();

        cartService.decrease(user, product);
        return "redirect:/cart";
    }

    /* ================= REMOVE ================= */
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, Principal principal) {

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productService.fetchbyId(id).orElseThrow();

        cartService.remove(user, product);
        return "redirect:/cart";
    }
    
}