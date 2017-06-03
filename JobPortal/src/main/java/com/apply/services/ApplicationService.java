package com.apply.services;

import com.apply.entities.Applicant;
import com.apply.entities.Application;
import com.apply.entities.Opening;
import com.apply.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Vivek Agarwal on 5/12/2017.
 */
@Service
public class ApplicationService {
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    OpeningService openingService;
    @Autowired
    ApplicantService applicantService;

    public void save(Application application) throws Exception {
        applicationRepository.save(application);
    }

    public Application getApplication(int id) throws Exception{
        return applicationRepository.findOne(id);
    }

    public boolean cancelApplication(Application application) throws Exception{
        if(application==null||application.isTerminal()) return false;
        application.setStatus("Cancelled");
        application.setTerminal(true);
        applicantService.reducePendingApplication(application.getApplicant().getEmail());

        return true;
    }

    public boolean rejectOffer(Application application){
        if(application==null||application.isTerminal() || !application.getStatus().equals("Offered")) return false;
        application.setStatus("OfferRejected");
        application.setTerminal(true);
        applicantService.reducePendingApplication(application.getApplicant().getEmail());

        return true;
    }

    public boolean acceptOffer(Application application){
        if(application==null||application.isTerminal() || !application.getStatus().equals("Offered")) return false;
        application.setStatus("OfferAccepted");
        application.setTerminal(true);
        applicantService.reducePendingApplication(application.getApplicant().getEmail());
        return true;
    }

    public boolean acceptCandidate(Application application){
        if(application==null||application.isTerminal()) return false;
        application.setStatus("Offered");
        return true;
    }

    public boolean rejectCandidate(Application application){
        if(application==null||application.isTerminal()) return false;
        System.out.println("Rejecting applicant :"+application.getApplicant().getEmail());
        application.setStatus("Rejected");
        application.setTerminal(true);
        applicantService.reducePendingApplication(application.getApplicant().getEmail());
        return true;
    }

}
