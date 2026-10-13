package com.pattern.implementor;

public class TelegramMessageSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            return false;
        }
        String formattedMessage = String.format("<b>%s</b>\n%s", title, body);
        System.out.printf("[Telegram -> %s]: %s%n", recipient, formattedMessage);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }
}