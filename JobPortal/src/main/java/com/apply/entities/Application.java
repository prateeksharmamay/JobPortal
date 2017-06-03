package com.apply.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * Created by Vivek Agarwal on 5/9/2017.
 */

@Entity
@Table(name="application")
public class Application {
    @Id
    @GeneratedValue
    @Column(name="application_id")
    private int application_id;

    @ManyToOne
    @JoinColumn(name = "opening_id")
    private Opening opening;

    @ManyToOne
    @JoinColumn(name = "email")
    private Applicant applicant;

    @Column
    private boolean terminal;

    @Column
    private String resumeUrl;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column
    private String status;

    @Embedded
    private InterviewSchedule interviewSchedule;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd-HH")
    private Date date;


    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }


    public Opening getOpening() {
        return opening;
    }

    public void setOpening(Opening opening) {
        this.opening = opening;
    }

    @JsonIgnore
    public Applicant getApplicant() {
        return applicant;
    }

    @JsonIgnore
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }


    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public InterviewSchedule getInterviewSchedule() {
        return interviewSchedule;
    }

    public void setInterviewSchedule(InterviewSchedule interviewSchedule) {
        this.interviewSchedule = interviewSchedule;
    }
}
