package com.group5.deliveryservice.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;


@Component
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmailTo(@Email final String receiver, final MailRequest mailRequest) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(receiver);
        mail.setSubject(mailRequest.getMailSubject());
        mail.setText(mailRequest.getMailBody());

        javaMailSender.send(mail);
    }
}

