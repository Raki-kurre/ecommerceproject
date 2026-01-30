package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.entity.User;
import com.project.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}