package com.example.jobportal.repository;

import com.example.jobportal.model.Application;
import com.example.jobportal.model.ApplicationStatus;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJob(Job job);
    List<Application> findByJobId(Long jobId);
    List<Application> findByApplicant(JobSeekerProfile applicant);
    List<Application> findByApplicantId(Long applicantId);
    List<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status);
    Optional<Application> findByJobIdAndApplicantId(Long jobId, Long applicantId);
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
    long countByJobId(Long jobId);
}