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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public static String uploadDir =
            System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    /* ================= LOGIN & REGISTER ================= */

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String password) {

        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/login";
    }

    /* ================= ADMIN ROOT ================= */

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/orders";
    }

    /* ================= CATEGORIES ================= */

    @GetMapping("/admin/categories")
    public String categoryPage(Model model) {
        model.addAttribute("categories", cservice.getAll());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String saveCategory(@ModelAttribute Category c) {
        cservice.saveCategory(c);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id,
                                 RedirectAttributes redirectAttributes) {

        cservice.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "success",
                "Category and all related products deleted successfully"
        );
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = cservice.fetchbyId(id);
        category.ifPresent(value -> model.addAttribute("category", value));
        return "categoriesAdd";
    }

    /* ================= PRODUCTS ================= */

    @GetMapping("/admin/products")
    public String productPage(Model model) {
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
    public String saveProduct(@ModelAttribute("productDTO") ProductDt p,
                              @RequestParam("productImage") MultipartFile file,
                              @RequestParam("imgName") String imgName)
            throws IOException {

        Product pro = new Product();
        pro.setId(p.getId());
        pro.setName(p.getName());
        pro.setPrice(p.getPrice());
        pro.setWeight(p.getWeight());
        pro.setDescription(p.getDescription());
        pro.setCategory(cservice.fetchbyId(p.getCategoryId()).get());

        String imageUUID = file.isEmpty() ? imgName : file.getOriginalFilename();
        if (!file.isEmpty()) {
            Path path = Paths.get(uploadDir, imageUUID);
            Files.write(path, file.getBytes());
        }

        pro.setImageName(imageUUID);
        pservice.saveProduct(pro);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        pservice.deletebyId(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model) {

        Product pro = pservice.fetchbyId(id).get();

        ProductDt pdt = new ProductDt();
        pdt.setId(pro.getId());
        pdt.setName(pro.getName());
        pdt.setPrice(pro.getPrice());
        pdt.setWeight(pro.getWeight());
        pdt.setDescription(pro.getDescription());
        pdt.setImageName(pro.getImageName());
        pdt.setCategoryId(pro.getCategory().getId());

        model.addAttribute("productDTO", pdt);
        model.addAttribute("categories", cservice.getAll());

        return "productsAdd";
    }
}