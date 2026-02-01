package com.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ProductDt;
import com.project.entity.Category;
import com.project.entity.Product;
import com.project.service.CategoryService;
import com.project.service.ProductService;

@Controller
public class AdminController {

    @Autowired
    private CategoryService cservice;

    @Autowired
    private ProductService pservice;

    // ✅ Railway-safe upload directory (DO NOT use static/)
    public static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/product-images";

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
            @ModelAttribute("productDTO") ProductDt dto,
            @RequestParam("productImage") MultipartFile file,
            @RequestParam(value = "imgName", required = false) String imgName
    ) throws IOException {

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setWeight(dto.getWeight());
        product.setDescription(dto.getDescription());

        Category category = cservice.fetchbyId(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        // ✅ Ensure directory exists
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String imageName = imgName;

        if (file != null && !file.isEmpty()) {
            imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path imagePath = Paths.get(UPLOAD_DIR, imageName);
            Files.write(imagePath, file.getBytes());
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