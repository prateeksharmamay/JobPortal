package com.apply.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="applicant")
public class Applicant {
    @Id
    @Column(name = "email")
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String introduction;

    @Column
    private String experience;

    @Column
    private String education;

    @Column
    private String skills;

    @Column
    private boolean verified;

    @Column
    private String hashValue;

    @OneToMany(mappedBy = "applicant")
    private Set<Application> applications;

    @ManyToMany
    @JoinTable(
            joinColumns = {@JoinColumn(name="email")},
            inverseJoinColumns = {@JoinColumn(name = "opening_id")}
    )
    private Set<Opening> interestedOpenings;

    public Set<Opening> getInterestedOpenings() {
        return interestedOpenings;
    }

    public void addToInterestedJobs(Opening opening){
        interestedOpenings.add(opening);
    }

    private int pendingApplications;

    private String imageUrl;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void addApplications(Application application) {
        this.applications.add(application);
    }

    public String getEducation() {
        return education;
    }

    public String getSkills() {
        return skills;
    }

    public boolean isVerified() {
        return verified;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getExperience() {
        return experience;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPendingApplications() {
        return pendingApplications;
    }

    public void setPendingApplications(int pendingApplications)
    {
        if(pendingApplications>=0)
            this.pendingApplications = pendingApplications;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

   /* @Override
    public String toString() {
        return "Applicant{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", experience=" + experience +
                ", education='" + education + '\'' +
                ", skills='" + skills + '\'' +
                ", verified=" + verified +
                ", applications=" + applications +
                '}';
    }*/

}