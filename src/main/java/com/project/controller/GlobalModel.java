package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


import com.project.service.CartService;
import com.project.service.UserService;

@ControllerAdvice
public class GlobalModel {

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {

        // ✅ ONLY run for HTTP requests
        if (!(org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes()
                instanceof org.springframework.web.context.request.ServletRequestAttributes)) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            model.addAttribute("cartCount", 0);
            return;
        }

        String email = auth.getName();

        // ✅ SINGLE lightweight query
        model.addAttribute("cartCount",
                cartService.getCartCountByEmail(email));
    }
}