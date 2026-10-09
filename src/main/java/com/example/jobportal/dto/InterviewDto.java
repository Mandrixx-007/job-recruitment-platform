package com.example.jobportal.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterviewDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate interviewDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime interviewTime;

    private String interviewType;   // ONLINE, IN_PERSON, PHONE
    private String meetingLink;
    private String location;
    private String notes;

    public LocalDate getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDate interviewDate) { this.interviewDate = interviewDate; }

    public LocalTime getInterviewTime() { return interviewTime; }
    public void setInterviewTime(LocalTime interviewTime) { this.interviewTime = interviewTime; }

    public String getInterviewType() { return interviewType; }
    public void setInterviewType(String interviewType) { this.interviewType = interviewType; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}