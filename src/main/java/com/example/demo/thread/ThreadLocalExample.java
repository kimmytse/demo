package com.example.demo.thread;

public class ThreadLocalExample {
    // 创建一个 ThreadLocal 变量
    private static final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        // 创建两个线程，模拟多线程环境
        Thread threadOne = new Thread(new Worker(), "Thread-1");
        Thread threadTwo = new Thread(new Worker(), "Thread-2");

        // 启动线程
        threadOne.start();
        threadTwo.start();
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            // 获取当前线程的名称
            String threadName = Thread.currentThread().getName();

            // 设置线程局部变量
            threadLocal.set((int) (Math.random() * 100));

            // 获取并打印线程局部变量的值
            System.out.println(threadName + " initial value: " + threadLocal.get());

            // 模拟一些工作
            for (int i = 0; i < 5; i++) {
                threadLocal.set(threadLocal.get() + 1);
                System.out.println(threadName + " updated value: " + threadLocal.get());
            }

            // 清理线程局部变量
            threadLocal.remove();
            System.out.println(threadName + " after remove: " + threadLocal.get());
        }
    }
}

