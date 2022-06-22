package com.example.BookingAppTeam05.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class HTMLMailService {
    private JavaMailSender mailSender;


    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String from, String to, String subject, String msg) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(from);
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            System.out.println("l");
        }
    }


}
