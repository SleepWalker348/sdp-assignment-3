package com.pattern.implementor;

public class SmsMessageSender implements MessageSender {
    private boolean available = true;
    private final int characterLimit;

    public SmsMessageSender() {
        this.characterLimit = 160;
    }

    public SmsMessageSender(int characterLimit) {
        this.characterLimit = characterLimit;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            return false;
        }
        String content = (title == null || title.isEmpty()) ? body : title + ": " + body;
        if (content.length() > characterLimit) {
            System.err.printf("[SMS Error -> %s]: Message exceeds limit of %d chars.%n", recipient, characterLimit);
            return false;
        }
        System.out.printf("[SMS -> %s]: %s%n", recipient, content);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}