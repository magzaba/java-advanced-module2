package com.epam.backend.core.multithreading.deadlocks;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class CollectionAnalyzer {

    private final ArrayList<Integer> collection;
    private static final Random RANDOM = new Random();
    private volatile boolean stopSignal;


    public CollectionAnalyzer(ArrayList<Integer> collection) {
        this.collection = collection;
    }

    private void write(){
        while (!stopSignal) {
                var nextNumber = generateValue();
                synchronized (collection) {
                collection.add(nextNumber);
                System.out.println("Number: " + nextNumber + " added to collection.");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("Exception caught: " + e.getClass().getSimpleName());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void calculateSum(){
        while (!stopSignal) {
            synchronized (collection) {
                var sum = BigInteger.valueOf(collection.stream().mapToLong(value -> value).sum());
                System.out.println("Sum of the numbers in the collection: " + sum);
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                System.err.println("Exception caught: " + e.getClass().getSimpleName());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void calculateSumOfSquares(){
        BigInteger sumOfSquares;
        while (!stopSignal) {
            synchronized (collection) {
                sumOfSquares = BigInteger.valueOf(collection.stream().mapToLong(value -> value * value).sum());
            }
            var squareRoot = Math.sqrt(sumOfSquares.doubleValue());
            System.out.printf("Square root of the sum of squares of the numbers in the collection: %.2f%n", squareRoot);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.err.println("Exception caught: " + e.getClass().getSimpleName());
                Thread.currentThread().interrupt();
            }
        }
    }

    private int generateValue() {
        return RANDOM.nextInt(1000);
    }


    private void stop() {
        stopSignal = true;
    }

    public static void main(String[] args) throws InterruptedException {
        var analyzer = new CollectionAnalyzer(new ArrayList<>());
        var writer = new Thread(analyzer::write);
        writer.start();

        Thread.sleep(10);

        var sumCalculator = new Thread(analyzer::calculateSum);
        sumCalculator.start();
        var squareCalculator = new Thread(analyzer::calculateSumOfSquares);
        squareCalculator.start();
        Thread.sleep(4000);
        analyzer.stop();
    }

}
