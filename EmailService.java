package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactEmail(String fromEmail, String name, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo("agaledeva4@gmail.com"); // Your receiving address
        message.setSubject("Portfolio Contact: " + subject);
        message.setText("Sender: " + name + " (" + fromEmail + ")\n\n" + messageBody);
        
        mailSender.send(message);
    }
    
 // Method 2: Auto-reply to the USER
    public void sendThankYouEmail(String userEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail); // Sending to the visitor
        message.setSubject("Thank you for reaching out!");
        message.setText("Hi " + userName + ",\n\n" +
                        "Thank you for contacting me through my portfolio. " +
                        "I have received your message and will get back to you as soon as possible.\n\n" +
                        "Best regards,\n" +
                        "Devesh Agale");
        mailSender.send(message);
    }
}