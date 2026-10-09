package com.example.jobportal.repository;

import com.example.jobportal.model.EmployerProfile;
import com.example.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByUser(User user);
    Optional<EmployerProfile> findByUserId(Long userId);
    Optional<EmployerProfile> findByUser_Email(String email);
    List<EmployerProfile> findByCompanyId(Long companyId);
}