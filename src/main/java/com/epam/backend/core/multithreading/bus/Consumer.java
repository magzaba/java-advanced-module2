package com.epam.backend.core.multithreading.bus;

public class Consumer implements Runnable {
    private MessageBus messageBus;
    private String topic;

    public Consumer(MessageBus messageBus, String topic) {
        this.messageBus = messageBus;
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = messageBus.consumeMessage(topic);
                System.out.println("Consumed: " + message.getPayload());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
