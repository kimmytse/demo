package com.example.demo.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Person {

    private int age;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public static void main(String[] args) {
        try {
            // 1. 加载类
            Class<?> clazz = Class.forName("com.example.demo.base.Person");

            // 2. 创建对象
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
            Object person = constructor.newInstance("John Doe", 30);

            // 3. 访问属性
            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            String name = (String) nameField.get(person);
            System.out.println("Name: " + name);

            Field ageField = clazz.getDeclaredField("age");
            ageField.setAccessible(true);
            int age = (int) ageField.get(person);
            System.out.println("Age: " + age);

            // 4. 调用方法
//            Method greetMethod = clazz.getDeclaredMethod("greet");
//            greetMethod.setAccessible(true);
//            greetMethod.invoke(person);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
