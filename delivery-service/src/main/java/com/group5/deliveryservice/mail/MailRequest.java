package com.group5.deliveryservice.mail;

public interface MailRequest {

    String getMailSubject();

    String getMailBody();

    String getBodyPrefix();

    String getBodySuffix();
}
