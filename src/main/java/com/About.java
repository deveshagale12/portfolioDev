package com.portfolio_dev;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String professionalTitle;
    
    @Column(columnDefinition = "TEXT") // Allows for longer text
    private String shortBio;
    
    @Column(columnDefinition = "TEXT")
    private String skillsAndExpertise; // You can store these as a comma-separated string
    
    @Column(columnDefinition = "TEXT")
    private String experienceSummary;
    
    private String missionVision;
    private String education;
    private String location; // Optional
    private String contactInfo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProfessionalTitle() {
		return professionalTitle;
	}
	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}
	public String getShortBio() {
		return shortBio;
	}
	public void setShortBio(String shortBio) {
		this.shortBio = shortBio;
	}
	public String getSkillsAndExpertise() {
		return skillsAndExpertise;
	}
	public void setSkillsAndExpertise(String skillsAndExpertise) {
		this.skillsAndExpertise = skillsAndExpertise;
	}
	public String getExperienceSummary() {
		return experienceSummary;
	}
	public void setExperienceSummary(String experienceSummary) {
		this.experienceSummary = experienceSummary;
	}
	public String getMissionVision() {
		return missionVision;
	}
	public void setMissionVision(String missionVision) {
		this.missionVision = missionVision;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

    // Getters and Setters
    
    
}