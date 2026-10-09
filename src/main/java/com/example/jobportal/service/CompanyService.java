package com.example.jobportal.service;

import com.example.jobportal.dto.CompanyDto;
import com.example.jobportal.model.Company;
import com.example.jobportal.model.EmployerProfile;
import com.example.jobportal.repository.CompanyRepository;
import com.example.jobportal.repository.EmployerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public CompanyService(CompanyRepository companyRepository,
                          EmployerProfileRepository employerProfileRepository) {
        this.companyRepository = companyRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    @Transactional
    public Company saveOrUpdateCompany(String employerEmail, CompanyDto dto) {
        EmployerProfile employer = employerProfileRepository.findByUser_Email(employerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Employer profile not found"));

        Company company = employer.getCompany();
        if (company == null) {
            company = new Company();
        }

        company.setName(dto.getName());
        company.setIndustry(dto.getIndustry());
        company.setLocation(dto.getLocation());
        company.setWebsite(dto.getWebsite());
        company.setDescription(dto.getDescription());
        company.setLogoUrl(dto.getLogoUrl());

        Company saved = companyRepository.save(company);

        employer.setCompany(saved);
        employerProfileRepository.save(employer);

        return saved;
    }

    public Company getCompanyForEmployer(String employerEmail) {
        return employerProfileRepository.findByUser_Email(employerEmail)
                .map(EmployerProfile::getCompany)
                .orElse(null);
    }
}