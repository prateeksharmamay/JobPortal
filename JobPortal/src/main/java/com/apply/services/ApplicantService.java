package com.apply.services;

import com.apply.entities.Applicant;
import com.apply.entities.Application;
import com.apply.entities.Company;
import com.apply.entities.Opening;
import com.apply.repositories.ApplicantRepository;
import com.apply.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Set;

/**
 * Created by Vivek Agarwal on 5/9/2017.
 */
@Service
public class ApplicantService {
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    OpeningService openingService;
    @Autowired
    ApplicationService applicationService;


    public boolean register(Applicant applicant) throws Exception {
        if(companyRepository.findOne(applicant.getEmail())==null && applicantRepository.findOne(applicant.getEmail())==null)
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String keyGen=applicant.getEmail()+":"+applicant.getPassword(); // When password is already encrypted, this could get buggy
            md.update(keyGen.getBytes());
            byte[] digest=md.digest();
            String hashValue= DatatypeConverter.printHexBinary(digest).toUpperCase();
            applicant.setHashValue(hashValue);
            applicantRepository.save(applicant);
            return true;
        }
        else return false;
    }


    public Applicant fetch(String email) throws Exception{
        return applicantRepository.findOne(email);
    }

    public Page<Opening> getOpenings(Pageable pageable) throws Exception{
        Page<Opening> openings=openingService.getAllOpenings(pageable);
        return openings;
    }

    public int applyWithResume(String resumeUrl, int opening_id, String applicant_id) {
        Application application=new Application();
        Opening opening=null;

        try{
            opening=openingService.getOpening(opening_id);
            if(opening==null)
                return 404;
        }
        catch (Exception e){
            e.printStackTrace();
            return 404;
        }

        Applicant applicant=applicantRepository.findOne(applicant_id);
        Set<Application> applications=applicant.getApplications();

        for(Application submittedApplication:applications){
            if(submittedApplication.getOpening().getOpening_id()==opening_id){
                if(!submittedApplication.isTerminal())
                    return 401;
            }
        }

        if(applicant.getPendingApplications()>4)
            return 402;

        application.setApplicant(applicant);
        application.setOpening(opening);
        application.setStatus("Pending");
        application.setResumeUrl(resumeUrl);

        try {
            applicationService.save(application);
            applicant.setPendingApplications(applicant.getPendingApplications()+1);
        }
        catch (Exception e){
            e.printStackTrace();
            return 404;
        }
        return 200;
    }

    public int apply(int jobId,String applicantId) throws Exception {
        Application application=new Application();
        Opening opening=null;
        try {
            opening = openingService.getOpening(jobId);
        }
        catch (Exception e){
            e.printStackTrace();
            return 404;
        }
        Applicant applicant=applicantRepository.findOne(applicantId);
        Set<Application> applications=applicant.getApplications();

        for(Application submittedApplication:applications){
            if(submittedApplication.getOpening().getOpening_id()==jobId){
                if(!submittedApplication.isTerminal())
                    return 401;
            }
        }

        if(applicant.getPendingApplications()>4)
            return 402;
        application.setApplicant(applicant);
        application.setOpening(opening);
        application.setStatus("Pending");
        application.setDate(new Date());

        try {
            applicationService.save(application);
            applicant.setPendingApplications(applicant.getPendingApplications()+1);
        }
        catch (Exception e){
            e.printStackTrace();
            return 404;
        }
        return 200;
    }

    public boolean reducePendingApplication(String email) {
        try {
            Applicant applicant = applicantRepository.findOne(email);
            applicant.setPendingApplications(applicant.getPendingApplications()-1);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Applicant getApplicantWithHashValue(String hashValue) throws Exception{
        return applicantRepository.findByHashValue(hashValue);
    }

    public void save(Applicant applicant) throws Exception {
        applicantRepository.save(applicant);
    }


}
