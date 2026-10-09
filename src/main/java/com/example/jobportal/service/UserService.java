package com.example.jobportal.service;

import com.example.jobportal.dto.RegisterDto;
import com.example.jobportal.model.EmployerProfile;
import com.example.jobportal.model.JobSeekerProfile;
import com.example.jobportal.model.Role;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.EmployerProfileRepository;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       EmployerProfileRepository employerProfileRepository,
                       JobSeekerProfileRepository jobSeekerProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        Role role = dto.getRole();
        if (role != Role.ROLE_JOB_SEEKER && role != Role.ROLE_EMPLOYER) {
            role = Role.ROLE_JOB_SEEKER;
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole(role);
        user.setEnabled(true);

        User saved = userRepository.save(user);

        // Auto-create the appropriate profile
        if (role == Role.ROLE_EMPLOYER) {
            EmployerProfile ep = new EmployerProfile();
            ep.setUser(saved);
            employerProfileRepository.save(ep);
        } else if (role == Role.ROLE_JOB_SEEKER) {
            JobSeekerProfile jsp = new JobSeekerProfile();
            jsp.setUser(saved);
            jobSeekerProfileRepository.save(jsp);
        }

        return saved;
    }
}