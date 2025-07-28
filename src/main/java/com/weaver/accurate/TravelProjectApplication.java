package com.weaver.accurate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.weaver.accurate.mapper")
@EnableAsync
@ComponentScan(value = "com.weaver.accurate.**")
public class TravelProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelProjectApplication.class, args);
    }

}
