package com.project.jstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JstagramApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:secret.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(JstagramApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
//        SpringApplication.run(JstagramApplication.class, args);
    }

}
