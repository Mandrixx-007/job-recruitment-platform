package com.example.jobportal.controller;

import com.example.jobportal.dto.RegisterDto;
import com.example.jobportal.model.Role;
import com.example.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ===== Login =====
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ===== Register =====
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("roles", new Role[]{Role.ROLE_JOB_SEEKER, Role.ROLE_EMPLOYER});
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        // Validation errors from @Valid
        if (result.hasErrors()) {
            model.addAttribute("roles", new Role[]{Role.ROLE_JOB_SEEKER, Role.ROLE_EMPLOYER});
            return "auth/register";
        }

        try {
            userService.registerUser(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("roles", new Role[]{Role.ROLE_JOB_SEEKER, Role.ROLE_EMPLOYER});
            model.addAttribute("emailError", ex.getMessage());
            return "auth/register";
        }

        redirectAttrs.addFlashAttribute("successMessage",
                "Registration successful! Please log in.");
        return "redirect:/login";
    }
}