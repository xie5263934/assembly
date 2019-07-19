package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author jinrun.xie
 * @date 2019/6/20
 **/
@SpringBootApplication
@MapperScan(value = "com.example.mapper")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HelloMain {
    public static void main(String[] args) {
        SpringApplication.run(HelloMain.class, args);
    }
}
