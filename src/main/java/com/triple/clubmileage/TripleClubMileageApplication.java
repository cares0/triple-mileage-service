package com.triple.clubmileage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TripleClubMileageApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripleClubMileageApplication.class, args);
    }

}
