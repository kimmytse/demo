package com.example.demo.spi;

import java.util.ServiceLoader;

public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<Search> loader = ServiceLoader.load(Search.class);
        for (Search search : loader) {
            search.search("hello");
        }
    }
}
