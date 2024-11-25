package com.epam.backend.core.multithreading.experiment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class Experiment {

    private final Map<Integer, Integer> map;
    private final AtomicBoolean addingCompleted = new AtomicBoolean(false);

    public Experiment(Map<Integer, Integer> map) {
        this.map = map;
    }

    public void startExperiment() {
        var adderThread = new Thread(() -> {
            IntStream.iterate(0, i -> i + 1).limit(1000).forEachOrdered(i -> {
                map.put(i, i);
                try {
                    Thread.sleep(1); // Sleep to increase the chance of timing overlap
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            addingCompleted.set(true);
        });


        var sumThread = new Thread(() -> {
            try {
                while(!addingCompleted.get() ){
                    var sum = map.values().stream()
                            .reduce(Integer::sum);
                    sum.ifPresent(s -> System.out.println("Sum: " + s));
                    // Calculate sum every second
                    Thread.sleep(1000);
                }
                // Final sum after adding is completed
                var finalSum = map.values().stream()
                        .reduce(Integer::sum);
                finalSum.ifPresent(s -> System.out.println("Final Sum: " + s));
            } catch (InterruptedException e) {
                System.out.println("Exception caught: " + e.getClass().getSimpleName());
                Thread.currentThread().interrupt();
            }
        });

        adderThread.start();
        sumThread.start();

        try {
            adderThread.join();
            sumThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        // Using HashMap
        System.out.println("Using HashMap:");
        Map<Integer, Integer> hashMap = new HashMap<>();
        new Experiment(hashMap).startExperiment();

        // Using ConcurrentHashMap
        System.out.println("Using ConcurrentHashMap:");
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        new Experiment(concurrentHashMap).startExperiment();

        // Using synchronized HashMap
        System.out.println("Using Collections.synchronizedMap:");
        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        new Experiment(synchronizedMap).startExperiment();
    }


}
