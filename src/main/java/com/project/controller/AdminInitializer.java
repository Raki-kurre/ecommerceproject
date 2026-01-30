package com.project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.entity.User;
import com.project.repository.UserRepository;

@Configuration
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "rakikurre@gmail.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);

            System.out.println("✅ ADMIN USER CREATED");
        } else {
            System.out.println("ℹ️ ADMIN USER ALREADY EXISTS");
        }
    }
}