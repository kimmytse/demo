package com.example.demo.base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExcutorClass {

    private static ExecutorService realTimeExecutorService = new ThreadPoolExecutor(32, 64, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        realTimeExecutorService.execute(() -> {
            System.out.println("hello");
        });
    }
}
