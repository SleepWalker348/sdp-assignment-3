package com.pattern.implementor;

public class EmailMessageSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            return false;
        }
        String emailBody = String.format("Subject: %s\nBody: %s\n-- \nSent via Notification Gateway", title, body);
        System.out.printf("[Email -> %s]:%n%s%n", recipient, emailBody);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }
}