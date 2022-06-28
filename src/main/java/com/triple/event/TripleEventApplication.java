package com.triple.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TripleEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripleEventApplication.class, args);
    }

}
