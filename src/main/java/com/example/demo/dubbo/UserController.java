package com.example.demo.dubbo;


import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @DubboReference
    private UserService userService;

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam String name) {
        return userService.sayHello(name);
    }
}

