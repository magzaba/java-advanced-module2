package com.epam.backend.core.multithreading.bus;

import java.util.Objects;

public class Message {
    private String topic;
    private String payload;

    public Message(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return topic.equals(message.topic) && payload.equals(message.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, payload);
    }

    @Override
    public String toString() {
        return "Message{" +
                ", topic='" + topic + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
