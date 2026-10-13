package com.pattern;

import com.pattern.abstraction.*;
import com.pattern.implementor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationGatewayTest {
    private TelegramMessageSender telegram;
    private EmailMessageSender email;
    private SmsMessageSender sms;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        telegram = new TelegramMessageSender();
        email = new EmailMessageSender();
        sms = new SmsMessageSender();
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        System.setErr(printStream);
    }

    @Test
    @DisplayName("1. Successful Delivery via Telegram")
    void testSuccessfulDelivery() {
        Notification notification = new UrgentNotification(telegram);
        notification.send("alice", "Hello!");
        assertTrue(outputStream.toString().contains("[Telegram -> alice]: <b>URGENT ALERT</b>\n[URGENT] Hello!"));
    }

    @Test
    @DisplayName("2. Primary Provider Unavailable without Fallback")
    void testUnavailableProvider() {
        telegram.setAvailable(false);
        Notification notification = new UrgentNotification(telegram);
        notification.send("bob", "Hello!");
        assertFalse(outputStream.toString().contains("[Telegram -> bob]"));
    }

    @Test
    @DisplayName("3. Primary Unavailable with Fallback to SMS")
    void testFallbackMechanism() {
        telegram.setAvailable(false);
        UrgentNotification notification = new UrgentNotification(telegram, sms);
        notification.send("+123456", "Emergency");

        String log = outputStream.toString();
        assertTrue(log.contains("[Fallback Triggered]: Primary sender (Telegram) unavailable. Switch -> SMS"));
        assertTrue(log.contains("[SMS -> +123456]: URGENT ALERT: [URGENT] Emergency"));
    }

    @Test
    @DisplayName("4. SMS Character Limit Enforcement (>160 characters)")
    void testSmsLengthRestriction() {
        String longMessage = "A".repeat(165);
        boolean result = sms.sendMessage("+1000", "Title", longMessage);
        assertFalse(result);
    }

    @Test
    @DisplayName("5. Message Encryption Verification (Base64 encoding)")
    void testEncryptedMessage() {
        Notification notification = new EncryptedNotification(email);
        notification.send("secret@agent.com", "MySecret123");
        assertTrue(outputStream.toString().contains("TXlTZWNyZXQxMjM="));
    }

    @Test
    @DisplayName("6. Scheduled Batch Delivery Summary")
    void testBatchNotification() {
        ScheduledBatchNotification batch = new ScheduledBatchNotification(email, List.of("user1@a.com", "user2@a.com"));
        String summary = batch.sendBatch("Batch Alert");
        assertTrue(summary.contains("Total: 2"));
        assertTrue(summary.contains("Successful: 2"));
        assertTrue(summary.contains("Failed: 0"));
    }

    @Test
    @DisplayName("7. Runtime Sender Switching")
    void testRuntimeSenderSwitching() {
        Notification notification = new UrgentNotification(telegram);
        assertEquals("Telegram", notification.getSender().getChannelName());

        notification.setSender(email);
        assertEquals("Email", notification.getSender().getChannelName());
    }

    @Test
    @DisplayName("8. Extension Component: PushNotificationSender Works")
    void testPushNotificationSender() {
        PushNotificationSender push = new PushNotificationSender();
        Notification notification = new UrgentNotification(push);
        notification.send("device_token_99", "Ping!");
        assertTrue(outputStream.toString().contains("[Push Notification -> device_token_99]"));
    }

    @Test
    @DisplayName("9. Extension Component: MarketingNotification Works")
    void testMarketingNotification() {
        Notification notification = new MarketingNotification(email);
        notification.send("buyer@shop.com", "Sale is live!");
        assertTrue(outputStream.toString().contains("Unsubscribe: Send STOP to 1234"));
    }

    @Test
    @DisplayName("10. Failure Scenario: SMS limit breached during notification dispatch")
    void testSmsLimitFailureScenario() {
        Notification notification = new UrgentNotification(sms);
        notification.send("+123", "X".repeat(200));
        assertTrue(System.err.toString().contains("Message exceeds limit of 160 chars")
                || outputStream.toString().contains("Message exceeds limit"));
    }
}