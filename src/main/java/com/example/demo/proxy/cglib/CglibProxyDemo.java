package com.example.demo.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

public class CglibProxyDemo {
    private final static StringBuilder aa = new StringBuilder("111");

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CGLIBService.class);
        enhancer.setCallback(new ServiceInterceptor());
        CGLIBService proxy = (CGLIBService) enhancer.create();
        proxy.perform();
        aa.append("222");
        System.out.println(aa);
    }
}
