package com.chenxu.physical.theatre;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.chenxu.physical.theatre.database.mapper"})
public class ChenxuPhysicalTheatreServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChenxuPhysicalTheatreServerApplication.class, args);
  }
}
