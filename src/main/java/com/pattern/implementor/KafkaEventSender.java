package com.pattern.implementor;

public class KafkaEventSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            return false;
        }
        String jsonPayload = String.format("{\"recipient\": \"%s\", \"title\": \"%s\", \"payload\": \"%s\"}", recipient, title, body);
        System.out.printf("[Kafka Event Topic]: %s%n", jsonPayload);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }
}