package com.example.engineer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.example.engineer.Repository")
@EntityScan(basePackages = {"com.example.engineer.Model"})
public class EngineerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngineerApplication.class, args);
    }

}
