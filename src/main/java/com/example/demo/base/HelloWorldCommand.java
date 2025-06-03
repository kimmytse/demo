package com.example.demo.base;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class HelloWorldCommand extends HystrixCommand<String> {

    private final String name;

    public HelloWorldCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }

    @Override
    protected String getFallback() {
        return "Hello Fallback!";
    }

    public static void main(String[] args) {
        HelloWorldCommand command = new HelloWorldCommand("World");
        String result = command.execute();
        System.out.println(result);
    }
}

