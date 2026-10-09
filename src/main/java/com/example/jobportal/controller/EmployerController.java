package com.example.jobportal.controller;

import com.example.jobportal.dto.CompanyDto;
import com.example.jobportal.model.*;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.EmployerProfileRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final CompanyService companyService;
    private final JobRepository jobRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final ApplicationRepository applicationRepository;

    public EmployerController(CompanyService companyService,
                              JobRepository jobRepository,
                              EmployerProfileRepository employerProfileRepository,
                              ApplicationRepository applicationRepository) {
        this.companyService = companyService;
        this.jobRepository = jobRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("email", email);

        EmployerProfile employer = employerProfileRepository.findByUser_Email(email).orElse(null);
        if (employer != null) {
            List<Job> myJobs = jobRepository.findByPostedById(employer.getId());
            long activeJobs = myJobs.stream().filter(j -> j.getStatus() == JobStatus.OPEN).count();

            long totalApplications = 0;
            long shortlisted = 0;
            long interviews = 0;

            for (Job job : myJobs) {
                var apps = applicationRepository.findByJobId(job.getId());
                totalApplications += apps.size();
                shortlisted += apps.stream()
                        .filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED).count();
                interviews += apps.stream()
                        .filter(a -> a.getStatus() == ApplicationStatus.INTERVIEW).count();
            }

            model.addAttribute("activeJobsCount", activeJobs);
            model.addAttribute("applicationsCount", totalApplications);
            model.addAttribute("shortlistedCount", shortlisted);
            model.addAttribute("interviewsCount", interviews);
        } else {
            model.addAttribute("activeJobsCount", 0);
            model.addAttribute("applicationsCount", 0);
            model.addAttribute("shortlistedCount", 0);
            model.addAttribute("interviewsCount", 0);
        }

        return "employer/dashboard";
    }

    // ===== Company Profile =====
    @GetMapping("/company")
    public String companyForm(Authentication auth, Model model) {
        Company existing = companyService.getCompanyForEmployer(auth.getName());

        CompanyDto dto = new CompanyDto();
        if (existing != null) {
            dto.setName(existing.getName());
            dto.setIndustry(existing.getIndustry());
            dto.setLocation(existing.getLocation());
            dto.setWebsite(existing.getWebsite());
            dto.setDescription(existing.getDescription());
            dto.setLogoUrl(existing.getLogoUrl());
        }

        model.addAttribute("companyDto", dto);
        model.addAttribute("hasCompany", existing != null);
        return "employer/company";
    }

    @PostMapping("/company")
    public String saveCompany(@Valid @ModelAttribute("companyDto") CompanyDto dto,
                              BindingResult result,
                              Authentication auth,
                              Model model,
                              RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("hasCompany", false);
            return "employer/company";
        }
        try {
            companyService.saveOrUpdateCompany(auth.getName(), dto);
            redirectAttrs.addFlashAttribute("successMessage", "Company profile saved!");
            return "redirect:/employer/jobs/new";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("hasCompany", false);
            return "employer/company";
        }
    }
}