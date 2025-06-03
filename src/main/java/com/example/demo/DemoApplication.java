package com.example.demo;

import com.example.demo.kafka.KafkaProducer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDubbo
@Configuration
@Service
@Controller
@RestController
@Repository
@Component
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    @Qualifier( "kafkaProducer")
    private KafkaProducer kafkaProducer;


    @Override
    @RequestMapping
    public void run(String... args) throws Exception {
        kafkaProducer.sendMessage("test-topic", "Hello, Kafka!");
    }

    @Bean
    public DemoApplication DemoApplication() {
        return new DemoApplication();
    }
}
