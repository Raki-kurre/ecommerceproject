package com.project.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.project.entity.Address;
import com.project.entity.User;
import com.project.service.AddressService;
import com.project.service.UserService;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    // OPEN ADDRESS PAGE
    @GetMapping
    public String openAddressPage() {
        return "address";
    }

    // SAVE ADDRESS TO DB
    @PostMapping("/save")
    public String saveAddress(Address address, Principal principal) {

        User user = userService.findByEmail(principal.getName())
                               .orElseThrow();

        address.setUser(user);        // 🔥 CRITICAL
        addressService.save(address);

        return "redirect:/cart";
    }
}