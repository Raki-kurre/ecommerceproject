package com.project.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.User;
import com.project.service.AddressService;
import com.project.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    private static final String UPLOAD_DIR =
        System.getProperty("user.dir") + "/src/main/resources/static/profile-images/";

    // VIEW PROFILE
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("addresses", addressService.getLatestAddress(user));
        return "account/profile";
    }

    // EDIT PROFILE PAGE
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "account/edit-profile";
    }

    // UPDATE PROFILE
    @PostMapping("/update")
    public String update(@ModelAttribute User user) {
        userService.updateProfile(user);
        return "redirect:/account/profile/" + user.getId();
    }

    // UPLOAD PHOTO
    @PostMapping("/upload-photo")
    public String uploadPhoto(@RequestParam MultipartFile image,
                              @RequestParam Long userId) throws Exception {

        if (!image.isEmpty()) {
            String fileName = userId + "_" + image.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), image.getBytes());

            User user = userService.findById(userId);
            user.setProfileImage(fileName);
            userService.updateProfile(user);
        }
        return "redirect:/account/profile/" + userId;
    }
}