package com.example.firstapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.firstapi.mapper") //  必须有这行
public class FirstApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstApiApplication.class, args);
    }
}
