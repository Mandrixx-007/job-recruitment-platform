package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.EmployerProfile;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByCompany(Company company);
    List<Job> findByCompanyId(Long companyId);
    List<Job> findByPostedBy(EmployerProfile postedBy);
    List<Job> findByPostedById(Long postedById);
    List<Job> findByStatusAndCategory(JobStatus status, String category);
    List<Job> findByStatusAndLocationContainingIgnoreCase(JobStatus status, String location);
    List<Job> findByTitleContainingIgnoreCase(String keyword);
}