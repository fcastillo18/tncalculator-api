package com.tncalculator.tncalculatorapi;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Log4j2
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TNCalculatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TNCalculatorApiApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args -> log.info("Message from application.properties --> " + environment.getProperty("message-from-application-properties"));
    }

}
