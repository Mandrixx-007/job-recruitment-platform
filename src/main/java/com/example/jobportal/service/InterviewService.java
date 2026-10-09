package com.example.jobportal.service;

import com.example.jobportal.dto.InterviewDto;
import com.example.jobportal.model.*;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.EmployerProfileRepository;
import com.example.jobportal.repository.InterviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public InterviewService(InterviewRepository interviewRepository,
                            ApplicationRepository applicationRepository,
                            EmployerProfileRepository employerProfileRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    @Transactional
    public Interview schedule(Long applicationId, InterviewDto dto, String employerEmail) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer not found"));

        if (!app.getJob().getPostedBy().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized");
        }

        Interview interview = interviewRepository.findByApplicationId(applicationId)
                .orElse(new Interview());

        interview.setApplication(app);
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewTime(dto.getInterviewTime());
        interview.setInterviewType(dto.getInterviewType());
        interview.setMeetingLink(dto.getMeetingLink());
        interview.setLocation(dto.getLocation());
        interview.setNotes(dto.getNotes());

        app.setStatus(ApplicationStatus.INTERVIEW);
        applicationRepository.save(app);

        return interviewRepository.save(interview);
    }

    public Interview findByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId).orElse(null);
    }
}