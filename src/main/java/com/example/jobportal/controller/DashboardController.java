package com.example.jobportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_JOB_SEEKER")) {
            return "redirect:/seeker/dashboard";
        } else if (role.equals("ROLE_EMPLOYER")) {
            return "redirect:/employer/dashboard";
        } else if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }
}