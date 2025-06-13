package com.chenxu.physical.theatre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ChenxuPhysicalTheatreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChenxuPhysicalTheatreServerApplication.class, args);
    }
}
