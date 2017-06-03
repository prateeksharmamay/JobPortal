package com.apply.aspect;

import com.apply.entities.Applicant;
import com.apply.entities.Application;
import com.apply.entities.Opening;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by Vivek Agarwal on 5/16/2017.
 */
@Aspect
@Component
public class OpeningServiceAspect {
    @Autowired
    JavaMailSender mailSender;

    @AfterReturning(value = "execution(* com.apply.services.CompanyService.updateOpening(com.apply.entities.Opening,com.apply.entities.Opening)) && args(openingReqObj,opening)",returning = "result")
    public void sendOpeningInfoUpdateToApplicant(Opening openingReqObj, Opening opening,boolean result){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            for(Application application:opening.getApplications()){
                if(!application.isTerminal()){
                    mailMessage.setTo(application.getApplicant().getEmail());
                    mailMessage.setSubject("Your applied opening has been recently updated.");

                    mailMessage.setText("The Opening with tile :"+opening.getTitle()+" has been modified and new details are" +
                            "\n\n\n Title :"+opening.getTitle()+"\n\n Company :"+opening.getCompany().getName()+"\n\n Job ID :"+opening.getOpening_id()+"\n\n Description :"+opening.getDescription()
                    +"\n\n Responsibilities :"+opening.getResponsibilities());

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
    }

    @AfterReturning(value = "execution(* com.apply.services.OpeningService.setStatus(com.apply.entities.Opening,com.apply.entities.Opening.Status)) && args(opening,status)",returning = "result")
    public void sendOpeningStatusUpdateToApplicant(Opening opening, Opening.Status status,boolean result){
        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            for(Application application:opening.getApplications()){
                if(!application.isTerminal()){
                    mailMessage.setTo(application.getApplicant().getEmail());
                    mailMessage.setSubject("Your applied opening status changed!");
                    mailMessage.setText("The status for the opening with tile :"+opening.getTitle()+" has changed as follows--" +
                            "\n\n Title :"+opening.getTitle()+"\n\n Company :"+opening.getCompany().getName()+"\n\n Job ID :"+opening.getOpening_id()+"\n\n Status :"+status);

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
    }


}
