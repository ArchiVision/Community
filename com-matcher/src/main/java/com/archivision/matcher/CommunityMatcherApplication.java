package com.archivision.matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunityMatcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityMatcherApplication.class, args);
    }
}
