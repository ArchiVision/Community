package com.archivision.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunityPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityPaymentApplication.class, args);
    }
}
