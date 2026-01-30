//package com.project.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public User getLoggedInUser() {
//
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//    if (auth == null || !auth.isAuthenticated()) {
//        throw new RuntimeException("User not logged in");
//    }
//
//    Object principal = auth.getPrincipal();
//
//    String email;
//
//    if (principal instanceof UserDetails) {
//        email = ((UserDetails) principal).getUsername();
//    } else {
//        email = principal.toString();
//    }
//
//    return userRepo.findByEmail(email)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//}
