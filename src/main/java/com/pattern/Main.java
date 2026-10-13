package com.pattern;

import com.pattern.abstraction.*;
import com.pattern.implementor.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        SmsMessageSender sms = new SmsMessageSender();

        System.out.println("=== Scenario 1: UrgentNotification + Telegram ===");
        Notification urgent = new UrgentNotification(telegram);
        urgent.send("user1", "System overload detected!");

        System.out.println("\n=== Scenario 2: EncryptedNotification + Email ===");
        Notification encrypted = new EncryptedNotification(email);
        encrypted.send("user2@example.com", "Confidential Salary Report");

        System.out.println("\n=== Scenario 3: ScheduledBatchNotification + SMS ===");
        List<String> batchUsers = List.of("+1234567890", "+0987654321", "+1122334455");
        Notification batch = new ScheduledBatchNotification(sms, batchUsers);
        batch.send(null, "System maintenance scheduled tonight at 02:00 UTC.");

        System.out.println("\n=== Scenario 4: Runtime switching: Telegram -> Email ===");
        Notification runtimeNotif = new UrgentNotification(telegram);
        runtimeNotif.send("user3", "Database primary node failover.");
        runtimeNotif.setSender(email);
        runtimeNotif.send("user3@example.com", "Database primary node failover.");

        System.out.println("\n=== Scenario 5: Telegram unavailable -> Fallback to SMS ===");
        telegram.setAvailable(false);
        UrgentNotification fallbackNotif = new UrgentNotification(telegram, sms);
        fallbackNotif.send("+19998887766", "Critical Security Breach!");

        System.out.println("\n=== Scenario 6: MarketingNotification + PushNotificationSender ===");
        PushNotificationSender pushSender = new PushNotificationSender();
        Notification marketing = new MarketingNotification(pushSender);
        marketing.send("user4_device_token", "Get 50% discount on all subscriptions!");
    }
}