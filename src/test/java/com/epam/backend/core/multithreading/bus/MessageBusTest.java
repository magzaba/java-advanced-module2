package com.epam.backend.core.multithreading.bus;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class MessageBusTest {
    private MessageBus messageBus;

    @BeforeMethod
    public void setUp() {
        messageBus = new MessageBus();
    }

    @Test
    public void testPostAndConsumeMessage() throws InterruptedException {
        String topic = "testTopic";
        String payload = "testPayload";
        Message message = new Message(topic, payload);

        Thread producer = new Thread(() -> messageBus.postMessage(message));
        Thread consumer = new Thread(() -> {
            Message consumedMessage = messageBus.consumeMessage(topic);
            Assert.assertEquals(consumedMessage.getPayload(), payload, "Payloads should match");
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    @Test
    public void testMultipleProducersAndConsumers() throws InterruptedException {
        String topic1 = "topic1";
        String topic2 = "topic2";

        Thread producer1 = new Thread(new Producer(messageBus, topic1));
        Thread producer2 = new Thread(new Producer(messageBus, topic2));
        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Message message = messageBus.consumeMessage(topic1);
                Assert.assertNotNull(message, "Message should not be null");
            }
        });
        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Message message = messageBus.consumeMessage(topic2);
                Assert.assertNotNull(message, "Message should not be null");
            }
        });

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        // Join for 1 second to allow some messages to be produced
        producer1.join(1000);
        producer2.join(1000);
        consumer1.join();
        consumer2.join();

        producer1.interrupt();
        producer2.interrupt();
    }
}