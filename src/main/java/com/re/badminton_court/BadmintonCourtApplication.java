package com.re.badminton_court;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BadmintonCourtApplication {

    public static void main(String[] args) {
        SpringApplication.run(BadmintonCourtApplication.class, args);
    }

}
