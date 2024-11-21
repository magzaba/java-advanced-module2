package com.epam.backend.core.multithreading.pool;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

public class BlockingObjectPoolTest {

    @Test
    public void shouldBePossibleToPutObjectInThePoolAndTakeFromThere(){
        //given
        var pool = new BlockingObjectPool(10);
        var object1 = new Object();
        var object2 = new Object();

        //when
        pool.put(object1);
        pool.put(object2);
        var takenFirst = pool.get();
        var takenSecond = pool.get();

        //then
        var softAssert = new SoftAssert();
        softAssert.assertEquals(takenFirst, object1);
        softAssert.assertEquals(takenSecond, object2);
        softAssert.assertAll();
    }

    @Test
    public void shouldBlockWhenPoolIsFull(){
        //given
        var pool = new BlockingObjectPool(10);
        IntStream.range(0,10)
                .mapToObj(i -> new Thread(()-> pool.put(new Object()), "Thread-"+i))
                .forEach(Thread::start);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println("Testing method interrupted");
        }
        var blockingObject = new Object();

        //when
        new Thread(() -> pool.put(blockingObject), "Thread-blocking").start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("Testing method interrupted");
        }
        new Thread(pool::get, "Thread-unblocking").start();

        //then
        var softAssert = new SoftAssert();
        assertEquals(Thread.currentThread().getName(), "main");
        assertEquals(Thread.currentThread().getState().toString(), "RUNNABLE");
        softAssert.assertAll();
    }

    @Test
    public void shouldBlockWhenPoolIsEmpty(){
        //given
        var pool = new BlockingObjectPool(10);
        var newObject = new Object();
        var unblockingThread = new Thread(()-> pool.put(newObject));

        //when
        new Thread(pool::get).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("Testing method interrupted");
        }
        unblockingThread.start();

        //then
        var softAssert = new SoftAssert();
        assertEquals(Thread.currentThread().getName(), "main");
        assertEquals(Thread.currentThread().getState().toString(), "RUNNABLE");
        softAssert.assertAll();
    }


}