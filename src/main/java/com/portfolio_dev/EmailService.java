package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

    @Autowired 
    private JavaMailSender mailSender;

    // We link the bean name from AsyncConfig here
    @Async("emailTaskExecutor") 
    public void sendEmailAsync(ContactMessage msg) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo("your-personal-email@gmail.com"); 
            email.setSubject("New Portfolio Message: " + msg.getSubject());
            email.setText("From: " + msg.getSenderName() + "\n\n" + msg.getMessage());
            
            mailSender.send(email);
            System.out.println(Thread.currentThread().getName() + " sent email successfully!");
        } catch (Exception e) {
            System.err.println("Error in " + Thread.currentThread().getName() + ": " + e.getMessage());
        }
    }
}