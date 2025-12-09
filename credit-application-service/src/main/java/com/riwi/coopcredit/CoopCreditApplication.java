package com.riwi.coopcredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CoopCreditApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoopCreditApplication.class, args);
    }
}
