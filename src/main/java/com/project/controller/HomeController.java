package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.project.entity.User;
import com.project.repository.UserRepository;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* ================= HOME ================= */
    @GetMapping("/")
    public String index() {
        return "index";   // templates/index.html
    }

    /* ================= LOGIN ================= */
    @GetMapping("/login")
    public String login() {
        return "login";   // templates/login.html
    }

    /* ================= REGISTER PAGE ================= */
    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html
    }

    /* ================= REGISTER SUBMIT ================= */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String email,
            @RequestParam String password) {

        // prevent duplicate user
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/register?error";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return "redirect:/login";
    }
}