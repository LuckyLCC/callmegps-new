package com.huwo.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CallmegpsNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallmegpsNewApplication.class, args);

    }

}
