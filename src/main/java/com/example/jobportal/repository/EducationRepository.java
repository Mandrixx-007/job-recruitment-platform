package com.example.jobportal.repository;

import com.example.jobportal.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByJobSeekerProfileId(Long jobSeekerProfileId);
}