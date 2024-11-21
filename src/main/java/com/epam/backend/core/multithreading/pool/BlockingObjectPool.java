package com.epam.backend.core.multithreading.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Pool that block when it has not any items, or it is full
*/
public class BlockingObjectPool {

    private static final Logger LOGGER = Logger.getLogger(BlockingObjectPool.class.getName());
    private final int size;
    private final BlockingQueue<Object> pool;


    /**
     * Creates filled pool of passed size
     *
     * @param size  of pool
     */
    public BlockingObjectPool(int size) {
        this.size = size;
        pool = new ArrayBlockingQueue<>(size);
    }

    /**
    * Gets object from pool or blocks if pool is empty
    *
    * @return object from pool
    */
    public Object get() {
        Object pooledObject = null;
        try {
        while(pool.isEmpty()){
            LOGGER.info("Pool is empty. " + Thread.currentThread().getName() + " waiting for an object.");
            Thread.sleep(1000);
        }
            pooledObject = pool.take();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE ,"Thread interrupted while waiting for an object from a pool.");
        }
        return pooledObject;
    }

    /**
    * Puts object to pool or blocks if pool is full
    *
    * @param object to be taken back to pool
    */
    public void put(Object object) {
        try{
            while(pool.size()==size) {
                LOGGER.info("Pool is full. " + Thread.currentThread().getName() + " waiting for space to put an object in the pool.");
                Thread.sleep(1000);
            }
            pool.put(object);
        } catch (InterruptedException e) {
            LOGGER.info("Thread "+ Thread.currentThread().getName() +" interrupted while waiting for a space in the pool.");

        }
    }
}
