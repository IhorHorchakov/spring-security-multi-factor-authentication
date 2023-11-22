package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ApplicationStarter {
    public static void main(String[] args) {
        log.info("Starting the application with args: {}", args);
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
