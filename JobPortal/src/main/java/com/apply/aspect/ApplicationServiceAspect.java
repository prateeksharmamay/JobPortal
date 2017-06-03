package com.apply.aspect;

import com.amazonaws.services.apigateway.model.Op;
import com.amazonaws.services.opsworks.model.App;
import com.apply.entities.Applicant;
import com.apply.entities.Opening;
import com.apply.services.ApplicantService;
import com.apply.services.OpeningService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by Vivek Agarwal on 5/17/2017.
 */

@Aspect
@Component
public class ApplicationServiceAspect {

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    ApplicantService applicantService;
    @Autowired
    OpeningService openingService;

    @AfterReturning(value = "execution(* com.apply.services.ApplicantService.apply(int,java.lang.String)) && args(jobId,applicantId)",returning = "statusCode")
    public void sendEmailToApplicant(int jobId,String applicantId,int statusCode){
        if(statusCode==200){
            try {
                Applicant applicant=applicantService.fetch(applicantId);
                Opening opening=openingService.getOpening(jobId);
                sendAppliedEmailUpdate(applicant,opening);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @AfterReturning(value = "execution(* com.apply.services.ApplicantService.applyWithResume(java.lang.String,int,java.lang.String)) && args(resumeUrl,opening_id,applicant_id)",returning = "statusCode")
    public void sendEmailToApplicant(String resumeUrl, int opening_id, String applicant_id,int statusCode){
        if(statusCode==200) {
            try {
                Applicant applicant = applicantService.fetch(applicant_id);
                Opening opening = openingService.getOpening(opening_id);
                sendAppliedEmailUpdate(applicant, opening);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void sendAppliedEmailUpdate(Applicant applicant, Opening opening){

        if(applicant!=null && opening!=null){

            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(applicant.getEmail());
            mailMessage.setSubject("We have received your application!");

            mailMessage.setText("Congratulations! You have successfully applied to the following Position : \n\n-"+"Job ID: "+opening.getOpening_id()+" \n\nTitle :"+opening.getTitle()+" \n\nCompany :"+opening.getCompany().getName());
            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
