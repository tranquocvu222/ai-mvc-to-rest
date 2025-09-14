package com.example.ai_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.ai_mvc")
@EnableJpaRepositories("com.example.ai_mvc")
public class AiMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMvcApplication.class, args);
    }
}
