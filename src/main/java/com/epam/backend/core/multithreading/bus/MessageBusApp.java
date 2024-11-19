package com.epam.backend.core.multithreading.bus;

public class MessageBusApp {
    public static void main(String[] args) {
        MessageBus messageBus = new MessageBus();
        Thread producer1 = new Thread(new Producer(messageBus, "topic1"));
        Thread producer2 = new Thread(new Producer(messageBus, "topic2"));
        Thread consumer1 = new Thread(new Consumer(messageBus, "topic1"));
        Thread consumer2 = new Thread(new Consumer(messageBus, "topic2"));

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}
