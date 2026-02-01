package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.entity.Category;
import com.project.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    // ✅ LIST CATEGORIES
    @GetMapping
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "categories"; // categories.html
    }

    // ✅ ADD CATEGORY PAGE
    @GetMapping("/add")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd"; // categoriesAdd.html
    }

    // ✅ SAVE CATEGORY
    @PostMapping("/add")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    // ✅ DELETE CATEGORY
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}