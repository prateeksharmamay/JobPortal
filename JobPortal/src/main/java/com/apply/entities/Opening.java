package com.apply.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Vivek Agarwal on 5/9/2017.
 */

@Entity
@Table(name="opening")
public class Opening {
    @Id
    @GeneratedValue
    @Column(name = "opening_id")
    private int opening_id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String responsibilities;

    @Column
    private String location;

    @Column
    private Integer minSalary;

    @Column
    private Integer maxSalary;

    public enum Status{OPEN,CANCELLED,FILLED}

    @Column
    public Status status;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd-HH")
    private Date date;

    @ManyToOne
    @JoinColumn
    private Company company;

    @OneToMany(mappedBy = "opening")
    private List<Application> applications;

    @ManyToMany(mappedBy = "interestedOpenings")
    @JsonBackReference
    private Set<Applicant> interestedApplicants;


    public Set<Applicant> getInterestedApplicants() {
        return interestedApplicants;
    }

    public void addToInterestedApplicants(Applicant applicant){
        interestedApplicants.add(applicant);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public int getOpening_id() {
        return opening_id;
    }

    public void setOpening_id(int opening_id) {
        this.opening_id = opening_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    @JsonIgnore
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @JsonIgnore
    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
/* @Override
    public String toString() {
        return "Opening{" +
                "opening_id=" + opening_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", responsibilities='" + responsibilities + '\'' +
                ", location='" + location + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", active=" + active +
                '}';
    }*/
}
