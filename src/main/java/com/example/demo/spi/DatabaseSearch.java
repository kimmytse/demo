package com.example.demo.spi;

import java.util.List;

public class DatabaseSearch implements Search {
    @Override
    public List<String> search(String keyword) {
        System.out.println("数据库搜索：" + keyword);
        return null;
    }
}
