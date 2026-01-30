package com.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.User;
import com.project.repository.OrderRepository;
import com.project.service.FileStorageService;
import com.project.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= VIEW PROFILE =================
    @GetMapping
    public String profile(Model model) {

        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();

        String email = auth.getName();

        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // ✅ ADD THIS
        model.addAttribute(
                "orders",
                orderRepository.findByUser(user)
        );

        return "profile";
    }

    // ================= EDIT PROFILE PAGE =================
    @GetMapping("/edit")
    public String editProfile(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "edit-profile"; // edit-profile.html
    }
    

    // ================= UPDATE PROFILE =================
    @PostMapping("/update")
    public String updateProfile(@RequestParam Long userId,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String location) {

        User user = userService.findById(userId);

        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLocation(location);

        userService.updateProfile(user);

        return "redirect:/profile?updated";
    }

    // ================= UPDATE PROFILE PHOTO =================
    @PostMapping("/photo")
    public String updatePhoto(@RequestParam("image") MultipartFile file)
            throws IOException {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();   // logged-in user

        User user = userService.findByEmail(email)
                               .orElseThrow(() -> new RuntimeException("User not found"));

        if (!file.isEmpty()) {
            String fileName = fileService.saveProfileImage(file);
            user.setProfileImage(fileName);
            userService.updateProfile(user);
        }

        return "redirect:/profile";
    }

    // ================= CHANGE PASSWORD PAGE =================
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "account/change-password"; 
        // templates/account/change-password.html
    }

    // ================= CHANGE PASSWORD ACTION =================
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        // ❌ old password incorrect
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect");
            return "account/change-password";
        }

        // ❌ new passwords do not match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "account/change-password";
        }

        // ✅ update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateProfile(user);

        return "redirect:/profile?passwordChanged";
    }
    
}