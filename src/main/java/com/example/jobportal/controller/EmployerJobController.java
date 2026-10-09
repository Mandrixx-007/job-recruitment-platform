package com.example.jobportal.controller;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer/jobs")
public class EmployerJobController {

    private final JobService jobService;

    public EmployerJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public String listJobs(Authentication auth, Model model) {
        List<Job> jobs = jobService.findByEmployer(auth.getName());
        model.addAttribute("jobs", jobs);
        return "employer/jobs/list";
    }

    @GetMapping("/new")
    public String showPostForm(Model model) {
        model.addAttribute("jobDto", new JobDto());
        return "employer/jobs/form";
    }

    @PostMapping("/new")
    public String postJob(@Valid @ModelAttribute("jobDto") JobDto dto,
                          BindingResult result,
                          Authentication auth,
                          RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "employer/jobs/form";
        }
        try {
            jobService.postJob(auth.getName(), dto);
            redirectAttrs.addFlashAttribute("successMessage", "Job posted successfully!");
            return "redirect:/employer/jobs";
        } catch (IllegalArgumentException ex) {
            modelIfNeeded(result, ex);
            return "employer/jobs/form";
        }
    }

    @PostMapping("/{id}/close")
    public String closeJob(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttrs) {
        try {
            jobService.closeJob(id, auth.getName());
            redirectAttrs.addFlashAttribute("successMessage", "Job closed.");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employer/jobs";
    }

    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttrs) {
        try {
            jobService.deleteJob(id, auth.getName());
            redirectAttrs.addFlashAttribute("successMessage", "Job deleted.");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employer/jobs";
    }

    private void modelIfNeeded(BindingResult result, Exception ex) {
        result.reject("global", ex.getMessage());
    }
}