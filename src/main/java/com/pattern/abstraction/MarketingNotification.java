package com.pattern.abstraction;

import com.pattern.implementor.MessageSender;

public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String promoMessage = message + "\n\nUnsubscribe: Send STOP to 1234";
        sender.sendMessage(recipient, "PROMO OFFER", promoMessage);
    }
}