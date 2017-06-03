package com.apply.services;

import com.apply.entities.Company;
import com.apply.entities.Opening;
import com.apply.repositories.ApplicantRepository;
import com.apply.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Set;

/**
 * Created by Vivek Agarwal on 5/10/2017.
 */
@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    OpeningService openingService;

    public boolean register(Company company) throws Exception {
        if(applicantRepository.findOne(company.getEmail())!=null || companyRepository.findOne(company.getEmail())!=null)
            return false;
        else {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String keyGen=company.getEmail()+":"+company.getPassword(); // When password is already encrypted, this could get buggy
            md.update(keyGen.getBytes());
            byte[] digest=md.digest();
            String hashValue= DatatypeConverter.printHexBinary(digest).toUpperCase();

            company.setHashValue(hashValue);
            companyRepository.save(company);
        }

        return true;
    }

    public void update(Company company) throws Exception{
        companyRepository.save(company);
    }

    public Company fetch(String email)throws Exception {
        return companyRepository.findOne(email);
    }

    public void postOpening(Opening opening) throws Exception{
        openingService.postOpening(opening);
    }

    public void deleteOpening(Opening opening) throws Exception{
        openingService.deleteOpening(opening.getOpening_id());
    }

    public void flushAllOpenings(String email) throws Exception{
        openingService.flushOpenings(email);
    }

    public Set<Opening> getAllOpenings(String email) throws Exception{
        return openingService.getOpenings(email);
    }

    public Company getCompanyWithHashValue(String hashValue) {
        return companyRepository.findByHashValue(hashValue);
    }

    public boolean updateOpening(Opening openingReqObj, Opening opening) {

        try {
            if (openingReqObj.getTitle() != null) {
                opening.setTitle(openingReqObj.getTitle());
            }
            if (openingReqObj.getDescription() != null) {
                opening.setDescription(openingReqObj.getDescription());
            }
            if (openingReqObj.getLocation() != null) {
                opening.setLocation(openingReqObj.getLocation());
            }
            if (openingReqObj.getMaxSalary() != null) {
                opening.setMaxSalary(openingReqObj.getMaxSalary());
            }
            if (openingReqObj.getMinSalary() != null) {
                opening.setMinSalary(openingReqObj.getMinSalary());
            }
            if (openingReqObj.getResponsibilities() != null) {
                opening.setResponsibilities(openingReqObj.getResponsibilities());
            }

//            if(openingReqObj.getStatus()!=null){
//                if(openingReqObj.getStatus()== Opening.Status.CANCELLED){
//                    if(!openingService.cancelOpening(opening)){
//                        return false;
//                    }
//                }
//            }

            openingService.update(opening);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
