package com.apply.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by kunal on 5/25/17.
 */

@Service
public class SendMailCalendar {

    @Autowired
    JavaMailSender mailSender;

    public void sendMail(String calFile,String email,String location)
    {
        System.out.println("Reached in Send Mail");
        SimpleMailMessage mailMessage=new SimpleMailMessage();


        mailMessage.setSubject("Interview Schdule");

        System.out.println("CalFile:"+calFile);

        mailMessage.setText("We have scheduled your interview. The location would be"+location+". Please accept or Reject by going to your interview page.");

        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);


            helper.setTo(email);
            helper.setSubject(mailMessage.getSubject());
            helper.setText(String.format(mailMessage.getText()));


            FileSystemResource file = new FileSystemResource("interviewSchedule.ics");
            helper.addAttachment(file.getFilename(), file);

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }



}
