package com.example.demo.spi;

import java.util.List;

public class FileSearch implements Search {
    @Override
    public List<String> search(String keyword) {
        System.out.println("文件搜索：" + keyword);
        return null;
    }
}
