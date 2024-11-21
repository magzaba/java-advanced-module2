package com.epam.backend.core.multithreading.experiment;

public interface ThreadSafeMap<K, V> {
    void put(K key, V value);

    V get(K key);

    int size();
}
