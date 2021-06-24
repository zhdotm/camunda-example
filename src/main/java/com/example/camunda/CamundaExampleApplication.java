package com.example.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaExampleApplication.class, args);
    }

}
