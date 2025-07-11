package com.weaver.accurate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.travel_project.mapper")
public class TravelProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelProjectApplication.class, args);
    }

}
