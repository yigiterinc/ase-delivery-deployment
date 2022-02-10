package com.group5.deliveryservice.mail;

public interface MailRequest {

    public String getMailSubject();

    public String getMailBody();

    public String getBodyPrefix();

    public String getBodySuffix();
}
