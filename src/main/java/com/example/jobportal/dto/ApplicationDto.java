package com.example.jobportal.dto;

import jakarta.validation.constraints.NotBlank;

public class ApplicationDto {

    @NotBlank(message = "Cover letter is required")
    private String coverLetter;

    @NotBlank(message = "CV URL is required")
    private String cvUrl;

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getCvUrl() { return cvUrl; }
    public void setCvUrl(String cvUrl) { this.cvUrl = cvUrl; }
}