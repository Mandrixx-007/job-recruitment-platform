package com.example.jobportal.service;

import com.example.jobportal.dto.ApplicationDto;
import com.example.jobportal.model.*;
import com.example.jobportal.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              JobSeekerProfileRepository jobSeekerProfileRepository,
                              EmployerProfileRepository employerProfileRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    @Transactional
    public Application apply(Long jobId, String seekerEmail, ApplicationDto dto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new IllegalArgumentException("This job is closed");
        }

        JobSeekerProfile seeker = jobSeekerProfileRepository.findByUser_Email(seekerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Job seeker profile not found"));

        if (applicationRepository.existsByJobIdAndApplicantId(jobId, seeker.getId())) {
            throw new IllegalArgumentException("You already applied to this job");
        }

        Application app = new Application();
        app.setJob(job);
        app.setApplicant(seeker);
        app.setCoverLetter(dto.getCoverLetter());
        app.setCvUrl(dto.getCvUrl());
        app.setStatus(ApplicationStatus.PENDING);

        return applicationRepository.save(app);
    }

    public List<Application> findBySeeker(String email) {
        JobSeekerProfile seeker = jobSeekerProfileRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("Job seeker profile not found"));
        return applicationRepository.findByApplicantId(seeker.getId());
    }

    public List<Application> findByEmployerJob(Long jobId, String employerEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));

        if (!job.getPostedBy().getId().equals(employer.getId())) {
            throw new SecurityException("You can only view applicants for your own jobs");
        }

        return applicationRepository.findByJobId(jobId);
    }

    @Transactional
    public void updateStatus(Long applicationId, ApplicationStatus newStatus, String employerEmail) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));

        if (!app.getJob().getPostedBy().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized");
        }

        app.setStatus(newStatus);
        app.setUpdatedAt(java.time.LocalDateTime.now());
        applicationRepository.save(app);
    }

    public long countByJob(Long jobId) {
        return applicationRepository.countByJobId(jobId);
    }
}