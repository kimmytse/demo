package com.example.demo.proxy.jdk;

import java.lang.reflect.Proxy;

public class JDKProxyDemo {
    public static void main(String[] args) {
        JDKService service = new JDKServiceImpl();
        JDKService proxy = (JDKService) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new ServiceInvocationHandler(service)
        );
        proxy.perform();
    }
}

