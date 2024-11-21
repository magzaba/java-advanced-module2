package com.epam.backend.core.multithreading.bus;

public class MessageBusApp {
    public static void main(String[] args) {
        var messageBus = new MessageBus();
        var producer1 = new Thread(new Producer(messageBus, "topic1"));
        var producer2 = new Thread(new Producer(messageBus, "topic2"));
        var consumer1 = new Thread(new Consumer(messageBus, "topic1"));
        var consumer2 = new Thread(new Consumer(messageBus, "topic2"));

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}
