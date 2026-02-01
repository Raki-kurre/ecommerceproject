package com.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ProductDt;
import com.project.entity.Category;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.repository.UserRepository;
import com.project.service.CategoryService;
import com.project.service.ProductService;

@Controller
public class AdminController {

    @Autowired
    private CategoryService cservice;

    @Autowired
    private ProductService pservice;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ IMPORTANT: Railway-safe upload path
    public static final String uploadDir =
            System.getProperty("user.dir") + "/uploads/product-images";

    /* ================= LOGIN ================= */

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /* ================= ADMIN ROOT ================= */

    @GetMapping("/admin")
    public String adminHome() {
        return "redirect:/admin/products";
    }

    /* ================= PRODUCTS ================= */

    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", pservice.getAll());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addProduct(Model model) {
        model.addAttribute("productDTO", new ProductDt());
        model.addAttribute("categories", cservice.getAll());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String saveProduct(
            @ModelAttribute("productDTO") ProductDt p,
            @RequestParam("productImage") MultipartFile file,
            @RequestParam("imgName") String imgName) throws IOException {

        Product product = new Product();
        product.setId(p.getId());
        product.setName(p.getName());
        product.setPrice(p.getPrice());
        product.setWeight(p.getWeight());
        product.setDescription(p.getDescription());

        Category category = cservice.fetchbyId(p.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        // ✅ Create directory if not exists
        Files.createDirectories(Paths.get(uploadDir));

        String imageName;
        if (!file.isEmpty()) {
            imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, imageName);
            Files.write(imagePath, file.getBytes());
        } else {
            imageName = imgName;
        }

        product.setImageName(imageName);
        pservice.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        pservice.deletebyId(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model) {

        Product product = pservice.fetchbyId(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDt dto = new ProductDt();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setWeight(product.getWeight());
        dto.setDescription(product.getDescription());
        dto.setImageName(product.getImageName());
        dto.setCategoryId(product.getCategory().getId());

        model.addAttribute("productDTO", dto);
        model.addAttribute("categories", cservice.getAll());

        return "productsAdd";
    }
}