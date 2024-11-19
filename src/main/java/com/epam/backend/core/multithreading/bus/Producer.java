package com.epam.backend.core.multithreading.bus;

public class Producer implements Runnable {
    private MessageBus messageBus;
    private String topic;

    public Producer(MessageBus messageBus, String topic) {
        this.messageBus = messageBus;
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String payload = "Message " + Math.random();
                Message message = new Message(topic, payload);
                messageBus.postMessage(message);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
