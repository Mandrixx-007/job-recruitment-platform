package com.example.jobportal.controller;

import com.example.jobportal.model.Job;
import com.example.jobportal.model.Role;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;

    public AdminController(UserRepository userRepository,
                           JobRepository jobRepository,
                           ApplicationRepository applicationRepository,
                           CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        model.addAttribute("email", auth.getName());

        List<User> users = userRepository.findAll();
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("jobSeekers",
                users.stream().filter(u -> u.getRole() == Role.ROLE_JOB_SEEKER).count());
        model.addAttribute("employers",
                users.stream().filter(u -> u.getRole() == Role.ROLE_EMPLOYER).count());
        model.addAttribute("totalJobs", jobRepository.count());
        model.addAttribute("totalApplications", applicationRepository.count());
        model.addAttribute("totalCompanies", companyRepository.count());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        redirectAttrs.addFlashAttribute("successMessage",
                "User " + (user.isEnabled() ? "enabled" : "disabled") + ".");
        return "redirect:/admin/users";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "admin/jobs";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        jobRepository.deleteById(id);
        redirectAttrs.addFlashAttribute("successMessage", "Job deleted.");
        return "redirect:/admin/jobs";
    }
}