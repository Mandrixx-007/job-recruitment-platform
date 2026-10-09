package com.example.jobportal.controller;

import com.example.jobportal.dto.JobSeekerProfileDto;
import com.example.jobportal.model.Application;
import com.example.jobportal.model.ApplicationStatus;
import com.example.jobportal.model.JobSeekerProfile;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.service.JobSeekerProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    private final ApplicationRepository applicationRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobSeekerProfileService profileService;

    public SeekerController(ApplicationRepository applicationRepository,
                            JobSeekerProfileRepository jobSeekerProfileRepository,
                            JobSeekerProfileService profileService) {
        this.applicationRepository = applicationRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.profileService = profileService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("email", email);

        JobSeekerProfile seeker = jobSeekerProfileRepository.findByUser_Email(email).orElse(null);
        if (seeker != null) {
            List<Application> apps = applicationRepository.findByApplicantId(seeker.getId());
            model.addAttribute("appliedCount", apps.size());
            model.addAttribute("shortlistedCount",
                    apps.stream().filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED).count());
            model.addAttribute("interviewsCount",
                    apps.stream().filter(a -> a.getStatus() == ApplicationStatus.INTERVIEW).count());
        } else {
            model.addAttribute("appliedCount", 0);
            model.addAttribute("shortlistedCount", 0);
            model.addAttribute("interviewsCount", 0);
        }

        return "seeker/dashboard";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication auth, Model model) {
        String email = auth.getName();
        JobSeekerProfile profile = profileService.findByEmail(email);

        JobSeekerProfileDto dto = new JobSeekerProfileDto();
        if (profile != null) {
            dto.setProfessionalTitle(profile.getProfessionalTitle());
            dto.setBiography(profile.getBiography());
            dto.setYearsOfExperience(profile.getYearsOfExperience());
            dto.setLocation(profile.getLocation());
            dto.setProfilePictureUrl(profile.getProfilePictureUrl());
            dto.setCvUrl(profile.getCvUrl());
        }

        model.addAttribute("profileDto", dto);
        model.addAttribute("email", email);
        return "seeker/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("profileDto") JobSeekerProfileDto dto,
                                Authentication auth,
                                RedirectAttributes redirectAttrs) {
        try {
            profileService.save(auth.getName(), dto);
            redirectAttrs.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/seeker/profile";
    }
}