package com.example.jobportal.service;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.model.*;
import com.example.jobportal.repository.EmployerProfileRepository;
import com.example.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public JobService(JobRepository jobRepository, EmployerProfileRepository employerProfileRepository) {
        this.jobRepository = jobRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    public List<Job> findOpenJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN);
    }

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
    }

    public List<Job> findByEmployer(String email) {
        EmployerProfile employer = employerProfileRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));
        return jobRepository.findByPostedById(employer.getId());
    }

    @Transactional
    public Job postJob(String employerEmail, JobDto dto) {
        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Please create your company profile first"));

        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setCategory(dto.getCategory());
        job.setLocation(dto.getLocation());
        job.setEmploymentType(dto.getEmploymentType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setDeadline(dto.getDeadline());
        job.setStatus(JobStatus.OPEN);
        job.setCompany(employer.getCompany());
        job.setPostedBy(employer);

        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId, String employerEmail) {
        Job job = findById(jobId);
        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));

        if (!job.getPostedBy().getId().equals(employer.getId())) {
            throw new SecurityException("You can only delete your own jobs");
        }
        jobRepository.delete(job);
    }

    @Transactional
    public void closeJob(Long jobId, String employerEmail) {
        Job job = findById(jobId);
        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));

        if (!job.getPostedBy().getId().equals(employer.getId())) {
            throw new SecurityException("You can only close your own jobs");
        }
        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);
    }
}