package com.apply.aspect;

import com.apply.entities.Applicant;
import com.apply.entities.Application;
import com.apply.entities.Company;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.mail.MailSender;
/**
 * Created by Vivek Agarwal on 5/9/2017.
 */

@Aspect
@Component
public class ApplicantServiceAspect {

    @Autowired
    JavaMailSender mailSender;

    @AfterReturning(value = "execution(* com.apply.services.ApplicantService.register(com.apply.entities.Applicant)) && args(applicant)",returning = "result")
    public void sendEmailToApplicant(boolean result, Applicant applicant){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(applicant.getEmail());
            mailMessage.setSubject("Hurray! You made it! Please verify your account.");

           String verificationUrl="http://localhost:8080/applicant/verify?key="+applicant.getHashValue(); // Remember to change after shifting to cloud!
          //String verificationUrl="http://jobportal.ap-northeast-2.elasticbeanstalk.com/applicant/verify?key="+applicant.getHashValue(); // Remember to change after shifting to cloud!

            mailMessage.setText("Please follow the link to verify the account... \n\n\n"+verificationUrl);

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "execution(* com.apply.services.CompanyService.register(com.apply.entities.Company)) && args(company)",returning = "result")
    public void sendEmailToCompany(boolean result, Company company){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(company.getEmail());
            mailMessage.setSubject("Hurray! You made it! Please verify your account.");

            String verificationUrl="http://localhost:8080/company/verify?key="+company.getHashValue(); // Remember to change after shifting to cloud!
            //String verificationUrl="http://jobportal.ap-northeast-2.elasticbeanstalk.com/company/verify?key="+company.getHashValue(); // Remember to change after shifting to cloud!


            mailMessage.setText("Please follow the link to verify the account... \n\n\n"+verificationUrl);

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "execution(* com.apply.services.ApplicationService.cancelApplication(com.apply.entities.Application)) && args(application)",returning = "result")
    public void sendCancelledUpdate(boolean result, Application application){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(application.getApplicant().getEmail());
            mailMessage.setSubject("Application status update");
            String message="Your following application has been cancelled. Please note the following :" +
                    "\n\nApplication id: "+application.getApplication_id()+
                    "\n\nStatus :"+application.getStatus()+
                    "\n\nTitle:"+application.getOpening().getTitle();
            mailMessage.setText(message);

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @AfterReturning(value = "execution(* com.apply.services.ApplicationService.rejectCandidate(com.apply.entities.Application)) && args(application)",returning = "result")
    public void sendRejectCandidateUpdate(boolean result, Application application){
        System.out.println("Result for reject candidate :"+result);
        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(application.getApplicant().getEmail());
            mailMessage.setSubject("Application status update");

            mailMessage.setText("Your following application's status has been changed--" +
                    "\n\nBefore :PENDING" +
                    "\n\nNow :REJECTED" +
                    "\n\nApplication ID :"+application.getApplication_id()+
                    "\n\nStatus :"+application.getStatus()+
                    "\n\nTitle :"+application.getOpening().getTitle());

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "execution(* com.apply.services.ApplicationService.rejectOffer(com.apply.entities.Application)) && args(application)",returning = "result")
    public void sendRejectOfferUpdate(boolean result, Application application){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(application.getApplicant().getEmail());
            mailMessage.setSubject("Application status update");

            mailMessage.setText("You have successfully REJECTED the following application's offer "+
                    "\n\nApplication ID :"+application.getApplication_id()+
                    "\n\nStatus :"+application.getStatus()+
                    "\n\nTitle :"+application.getOpening().getTitle()+
                    "\n\nCompany :"+application.getOpening().getCompany().getName()+
                    "\n\nJob ID :"+application.getOpening().getOpening_id());

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "execution(* com.apply.services.ApplicationService.acceptOffer(com.apply.entities.Application)) && args(application)",returning = "result")
    public void sendAcceptOfferUpdate(boolean result, Application application){

        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(application.getApplicant().getEmail());
            mailMessage.setSubject("Application status update");

            mailMessage.setText("Congratulations! Your have accepted the following application's offer!"+
                    "\n\nApplication ID :"+application.getApplication_id()+
                    "\n\nStatus :"+application.getStatus()+
                    "\n\nTitle :"+application.getOpening().getTitle()+
                    "\n\nJob ID :"+application.getOpening().getOpening_id());

            try {
                mailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "execution(* com.apply.services.ApplicationService.acceptCandidate(com.apply.entities.Application)) && args(application)",returning = "result")
    public void sendAcceptCandidateUpdate(boolean result, Application application){
        System.out.println("Result for acccept candidate :"+result);
        if(result){
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setTo(application.getApplicant().getEmail());
            mailMessage.setSubject("Application status update");

            mailMessage.setText("Congratulations! Your following application's status has been changed from PENDING to OFFERED."+
                    "\n\nApplication ID :"+application.getApplication_id()+
                    "\n\nStatus :"+application.getStatus()+
                            "\n\n Title :"+application.getOpening().getTitle()+
                    "\n\n Job ID:"+application.getOpening().getOpening_id());

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
