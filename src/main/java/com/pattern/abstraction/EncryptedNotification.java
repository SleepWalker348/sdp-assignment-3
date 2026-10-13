package com.pattern.abstraction;

import com.pattern.implementor.MessageSender;
import java.util.Base64;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String encryptedPayload = Base64.getEncoder().encodeToString(message.getBytes());
        sender.sendMessage(recipient, "ENCRYPTED MESSAGE", encryptedPayload);
    }
}