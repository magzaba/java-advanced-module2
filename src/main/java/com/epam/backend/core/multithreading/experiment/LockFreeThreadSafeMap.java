package com.epam.backend.core.multithreading.experiment;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LockFreeThreadSafeMap <K, V> implements ThreadSafeMap <K, V>{
    private final ConcurrentLinkedQueue<Entry<K, V>> entries = new ConcurrentLinkedQueue<>();

    public void put(K key, V value) {
        var existingEntry = entries.stream()
                .filter(entry -> key.equals(entry.key))
                .findFirst();

        if (existingEntry.isPresent()) {
            existingEntry.get().value = value;
        } else {
            entries.add(new Entry<>(key, value));
        }
    }

    public V get(K key) {
        return entries.stream()
                .filter(entry -> key.equals(entry.key))
                .map(entry -> entry.value)
                .findFirst()
                .orElse(null);
    }

    public int size() {
        return entries.size();
    }

    private static class Entry<K, V> {
        final K key;
        volatile V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
