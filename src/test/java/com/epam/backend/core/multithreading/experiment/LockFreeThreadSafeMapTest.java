package com.epam.backend.core.multithreading.experiment;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class LockFreeThreadSafeMapTest {
    private LockFreeThreadSafeMap<Integer, String> map;

    @BeforeMethod
    public void setUp() {
        map = new LockFreeThreadSafeMap<>();
    }

    @Test
    public void testPutAndGet() {
        map.put(1, "one");
        assertEquals(map.get(1), "one", "The retrieved value should match the inserted value.");
    }

    @Test
    public void testUpdateValue() {
        map.put(1, "one");
        map.put(1, "ONE");
        assertEquals(map.get(1), "ONE", "The value should be updated to 'ONE'.");
    }

    @Test
    public void testSize() {
        map.put(1, "one");
        map.put(2, "two");
        assertEquals(map.size(), 2, "The size of the map should be 2.");
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(map.get(99), "Getting a non-existent key should return null.");
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        var thread1 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> map.put(i, "Value " + i)));

        var thread2 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> map.get(i)));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(map.size(), 1000, "The map should contain 1000 entries.");
    }
}