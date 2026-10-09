package com.example.jobportal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qualification;    // e.g. "BSc Computer Science"
    private String institution;      // e.g. "University of Lagos"
    private Integer startYear;
    private Integer endYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;

    public Education() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public Integer getStartYear() { return startYear; }
    public void setStartYear(Integer startYear) { this.startYear = startYear; }

    public Integer getEndYear() { return endYear; }
    public void setEndYear(Integer endYear) { this.endYear = endYear; }

    public JobSeekerProfile getJobSeekerProfile() { return jobSeekerProfile; }
    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) { this.jobSeekerProfile = jobSeekerProfile; }
}