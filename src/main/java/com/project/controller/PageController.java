package com.project.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.entity.Address;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.service.AddressService;
import com.project.service.CategoryService;
import com.project.service.DeliveryFeeService;
import com.project.service.ProductService;
import com.project.service.UserService;

@Controller
public class PageController {

    @Autowired
    private CategoryService cservice;

    @Autowired
    private ProductService pservice;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DeliveryFeeService deliveryFeeService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    // ================= SHOP =================
    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("categories", cservice.getAll());
        model.addAttribute("products", pservice.getAll());
        return "shop";
    }

    // ================= SHOP BY CATEGORY =================
    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id) {
        model.addAttribute("categories", cservice.getAll());
        model.addAttribute("products", pservice.getProByCatId(id));
        return "shop";
    }

    // ================= VIEW PRODUCT =================
    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Product product = pservice.getProductById(id);
        if (product == null) {
            return "redirect:/shop";
        }

        model.addAttribute("product", product);

        Address address = null;
        double deliveryFee = 0;

        if (principal != null) {
            User user = userService.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                address = addressService.getLatestAddress(user);
                if (address != null) {
                    deliveryFee = deliveryFeeService.calculate(address);
                }
            }
        }

        model.addAttribute("deliveryFee", Math.round(deliveryFee));
        model.addAttribute("grandTotal",
                Math.round(product.getPrice() + deliveryFee));
        model.addAttribute("razorpayKeyId", razorpayKey);

        return "viewProduct";
    }

   
}