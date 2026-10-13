package com.pattern.abstraction;

import com.pattern.implementor.MessageSender;
import java.util.List;

public class ScheduledBatchNotification extends Notification {
    private List<String> recipients;

    public ScheduledBatchNotification(MessageSender sender, List<String> recipients) {
        super(sender);
        this.recipients = recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    @Override
    public void send(String unusedRecipient, String message) {
        sendBatch(message);
    }

    public String sendBatch(String message) {
        int successful = 0;
        int failed = 0;

        for (String recipient : recipients) {
            boolean result = sender.sendMessage(recipient, "Scheduled Batch Message", message);
            if (result) {
                successful++;
            } else {
                failed++;
            }
        }

        String summary = String.format("Total: %d%nSuccessful: %d%nFailed: %d", recipients.size(), successful, failed);
        System.out.println("--- Batch Delivery Summary ---");
        System.out.println(summary);
        System.out.println("------------------------------");
        return summary;
    }
}