package com.fema.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RightDoseApplication {

  public static void main(String[] args) {
    SpringApplication.run(RightDoseApplication.class, args);
  }
}
