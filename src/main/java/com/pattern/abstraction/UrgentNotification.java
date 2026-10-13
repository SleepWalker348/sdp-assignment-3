package com.pattern.abstraction;

import com.pattern.implementor.MessageSender;

public class UrgentNotification extends Notification {
    private MessageSender fallbackSender;

    public UrgentNotification(MessageSender sender) {
        super(sender);
    }

    public UrgentNotification(MessageSender sender, MessageSender fallbackSender) {
        super(sender);
        this.fallbackSender = fallbackSender;
    }

    public void setFallbackSender(MessageSender fallbackSender) {
        this.fallbackSender = fallbackSender;
    }

    @Override
    public void send(String recipient, String message) {
        String urgentMessage = "[URGENT] " + message;
        String title = "URGENT ALERT";

        if (sender != null && sender.isAvailable()) {
            boolean success = sender.sendMessage(recipient, title, urgentMessage);
            if (success) {
                return;
            }
        }

        if (fallbackSender != null && fallbackSender.isAvailable()) {
            System.out.printf("[Fallback Triggered]: Primary sender (%s) unavailable. Switch -> %s%n",
                    sender != null ? sender.getChannelName() : "None", fallbackSender.getChannelName());
            fallbackSender.sendMessage(recipient, title, urgentMessage);
        } else {
            System.err.printf("[Error]: All senders failed for recipient %s.%n", recipient);
        }
    }
}