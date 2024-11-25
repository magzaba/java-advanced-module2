package com.epam.backend.core.multithreading.experiment;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedThreadSafeMap<K, V> implements ThreadSafeMap<K, V> {
    private final List<Entry<K, V>> entries;

    public SynchronizedThreadSafeMap() {
        this.entries = new ArrayList<>();
    }

    @Override
    public synchronized void put(K key, V value) {
        var existingEntry = entries.stream()
                .filter(entry -> key.equals(entry.key))
                .findFirst();

        if (existingEntry.isPresent()) {
            existingEntry.get().value = value;
        } else {
            entries.add(new Entry<>(key, value));
        }
    }

    @Override
    public synchronized V get(K key) {
        return entries.stream()
                .filter(entry -> key.equals(entry.key))
                .map(entry -> entry.value)
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized int size() {
        return entries.size();
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}