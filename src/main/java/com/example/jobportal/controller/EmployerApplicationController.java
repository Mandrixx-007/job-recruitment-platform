package com.example.jobportal.controller;

import com.example.jobportal.dto.InterviewDto;
import com.example.jobportal.model.Application;
import com.example.jobportal.model.ApplicationStatus;
import com.example.jobportal.model.Interview;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.ApplicationService;
import com.example.jobportal.service.InterviewService;
import com.example.jobportal.service.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerApplicationController {

    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final JobService jobService;

    public EmployerApplicationController(ApplicationService applicationService,
                                         InterviewService interviewService,
                                         JobService jobService) {
        this.applicationService = applicationService;
        this.interviewService = interviewService;
        this.jobService = jobService;
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public String viewApplicants(@PathVariable Long jobId, Authentication auth, Model model) {
        Job job = jobService.findById(jobId);
        List<Application> apps = applicationService.findByEmployerJob(jobId, auth.getName());
        model.addAttribute("job", job);
        model.addAttribute("applications", apps);
        model.addAttribute("statuses", ApplicationStatus.values());
        return "employer/applicants";
    }

    @PostMapping("/applications/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam ApplicationStatus status,
                               @RequestParam Long jobId,
                               Authentication auth,
                               RedirectAttributes redirectAttrs) {
        try {
            applicationService.updateStatus(id, status, auth.getName());
            redirectAttrs.addFlashAttribute("successMessage", "Status updated to " + status);
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employer/jobs/" + jobId + "/applicants";
    }

    @GetMapping("/applications/{id}/interview")
    public String showInterviewForm(@PathVariable Long id,
                                    @RequestParam Long jobId,
                                    Model model) {
        Interview existing = interviewService.findByApplication(id);

        InterviewDto dto = new InterviewDto();
        if (existing != null) {
            dto.setInterviewDate(existing.getInterviewDate());
            dto.setInterviewTime(existing.getInterviewTime());
            dto.setInterviewType(existing.getInterviewType());
            dto.setMeetingLink(existing.getMeetingLink());
            dto.setLocation(existing.getLocation());
            dto.setNotes(existing.getNotes());
        }

        model.addAttribute("applicationId", id);
        model.addAttribute("jobId", jobId);
        model.addAttribute("interviewDto", dto);
        return "employer/interview";
    }

    @PostMapping("/applications/{id}/interview")
    public String scheduleInterview(@PathVariable Long id,
                                    @ModelAttribute("interviewDto") InterviewDto dto,
                                    @RequestParam Long jobId,
                                    Authentication auth,
                                    RedirectAttributes redirectAttrs) {
        try {
            interviewService.schedule(id, dto, auth.getName());
            redirectAttrs.addFlashAttribute("successMessage", "Interview scheduled.");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employer/jobs/" + jobId + "/applicants";
    }
}