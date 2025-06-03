package com.example.demo.base;

import com.example.demo.design.singleton.Singleton;

public class Example {
    //    private static AtomicInteger staticVar = new AtomicInteger(0);
//
//    public void increment() {
//        staticVar.incrementAndGet();
//    }
    private final static SimpleLock lock = new SimpleLock();
    private static Integer staticVar = 0;

    public void increment() {
        lock.lock();
        try {
            staticVar++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

//    private AtomicReference<Thread> owner = new AtomicReference<>();
//
//    public void lock() {
//        Thread currentThread = Thread.currentThread();
//        // 自旋等待，直到成功获取锁
//        while (!owner.compareAndSet(null, currentThread)) {
//            // 自旋等待
//        }
//        System.out.println("Thread " + currentThread.getName() + " locked");
//    }
//
//    public void unlock() {
//        Thread currentThread = Thread.currentThread();
//        owner.compareAndSet(currentThread, null);
//    }

    public static void main(String[] args) {
        Example obj1 = new Example();
        Example obj2 = new Example();

        // 创建多个线程来调用 increment 方法
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                obj1.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                obj2.increment();
            }
        });

        // 启动线程
        thread1.start();
        thread2.start();

        // 等待线程执行完毕
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出最终的 staticVar 值
        System.out.println(Example.staticVar.toString()); // 输出 2000

//        obj1.lock();
//        obj1.unlock();


        Singleton instance = Singleton.INSTANCE;
        instance.someMethod();
    }


}


