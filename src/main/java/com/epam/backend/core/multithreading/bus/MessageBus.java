package com.epam.backend.core.multithreading.bus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MessageBus {
    private final Map<String, Queue<Message>> queues = new HashMap<>();

    public synchronized void postMessage(Message message) {
        var topic = message.getTopic();
        queues.putIfAbsent(topic, new LinkedList<>());
        queues.get(topic).offer(message);
        notifyAll();
    }

    public synchronized Message consumeMessage(String topic) {
        while (!queues.containsKey(topic) || queues.get(topic).isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queues.get(topic).poll();
    }
}
