package com.example.demo.base;

public class SynchronizedDemo       {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
    }

    public void incrementWithBlock() {
        synchronized (this) {
            counter++;
        }
    }

    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();
        Thread t1 = new Thread(demo::increment);
        Thread t2 = new Thread(demo::incrementWithBlock);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter: " + demo.counter);
    }
}
