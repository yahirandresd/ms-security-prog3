package com.rml.ms_security.Models;

public class EmailSent {
    private String body_html;
    private String recipient;
    private String subject;

    public EmailSent(String recipient, String subject, String body_html) {
        this.recipient = recipient;
        this.subject = subject;
        this.body_html = body_html;
    }

    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String from) {
        this.body_html = body_html;
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
}
