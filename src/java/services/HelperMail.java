package services;

import java.util.ArrayList;
import java.util.Date;

public class HelperMail {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String date;
    
    public HelperMail(){}
    
    public HelperMail(String sender, String recipient, String subject, String body, String date){
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
