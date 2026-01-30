package com.project.service;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.entity.User;
import com.project.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User updateProfile(User user) {
        User existing = findById(user.getId());
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        if (user.getProfileImage() != null) {
            existing.setProfileImage(user.getProfileImage());
        }
        return userRepo.save(existing);
    }
    
 // ✅ GET LOGGED-IN USER (FIX)
    public User getLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not logged in");
        }

        Object principal = auth.getPrincipal();

        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    
}