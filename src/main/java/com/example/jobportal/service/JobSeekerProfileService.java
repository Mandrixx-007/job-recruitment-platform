package com.example.jobportal.service;

import com.example.jobportal.dto.JobSeekerProfileDto;
import com.example.jobportal.model.JobSeekerProfile;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository repository;

    public JobSeekerProfileService(JobSeekerProfileRepository repository) {
        this.repository = repository;
    }

    public JobSeekerProfile findByEmail(String email) {
        return repository.findByUser_Email(email).orElse(null);
    }

    @Transactional
    public JobSeekerProfile save(String email, JobSeekerProfileDto dto) {
        JobSeekerProfile profile = repository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setProfessionalTitle(dto.getProfessionalTitle());
        profile.setBiography(dto.getBiography());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setLocation(dto.getLocation());
        profile.setProfilePictureUrl(dto.getProfilePictureUrl());
        profile.setCvUrl(dto.getCvUrl());

        return repository.save(profile);
    }
}