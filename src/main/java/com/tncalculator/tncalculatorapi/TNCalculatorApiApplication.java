package com.tncalculator.tncalculatorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TNCalculatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TNCalculatorApiApplication.class, args);
    }

}
