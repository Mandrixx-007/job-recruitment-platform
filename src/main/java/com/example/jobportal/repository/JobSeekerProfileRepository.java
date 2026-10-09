package com.example.jobportal.repository;

import com.example.jobportal.model.JobSeekerProfile;
import com.example.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {
    Optional<JobSeekerProfile> findByUser(User user);
    Optional<JobSeekerProfile> findByUserId(Long userId);
    Optional<JobSeekerProfile> findByUser_Email(String email);
}