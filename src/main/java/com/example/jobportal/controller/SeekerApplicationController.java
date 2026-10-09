package com.example.jobportal.controller;

import com.example.jobportal.dto.ApplicationDto;
import com.example.jobportal.model.Application;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.ApplicationService;
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
@RequestMapping("/seeker")
public class SeekerApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;

    public SeekerApplicationController(ApplicationService applicationService, JobService jobService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    @GetMapping("/apply/{jobId}")
    public String showApplyForm(@PathVariable Long jobId, Model model) {
        Job job = jobService.findById(jobId);
        model.addAttribute("job", job);
        model.addAttribute("applicationDto", new ApplicationDto());
        return "seeker/apply";
    }

    @PostMapping("/apply/{jobId}")
    public String submitApplication(@PathVariable Long jobId,
                                    @Valid @ModelAttribute("applicationDto") ApplicationDto dto,
                                    BindingResult result,
                                    Authentication auth,
                                    Model model,
                                    RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("job", jobService.findById(jobId));
            return "seeker/apply";
        }
        try {
            applicationService.apply(jobId, auth.getName(), dto);
            redirectAttrs.addFlashAttribute("successMessage", "Application submitted successfully!");
            return "redirect:/seeker/applications";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("job", jobService.findById(jobId));
            model.addAttribute("errorMessage", ex.getMessage());
            return "seeker/apply";
        }
    }

    @GetMapping("/applications")
    public String myApplications(Authentication auth, Model model) {
        List<Application> apps = applicationService.findBySeeker(auth.getName());
        model.addAttribute("applications", apps);
        return "seeker/applications";
    }
}