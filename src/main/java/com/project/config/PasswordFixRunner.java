package com.project.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.entity.User;
import com.project.repository.UserRepository;

@Component
public class PasswordFixRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void fixPasswords() {
        for (User user : userRepo.findAll()) {
            // Encode only if not already encoded
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepo.save(user);
            }
        }
        System.out.println("✅ Passwords encoded successfully");
    }
}