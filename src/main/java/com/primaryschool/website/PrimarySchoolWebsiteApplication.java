package com.primaryschool.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PrimarySchoolWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimarySchoolWebsiteApplication.class, args);
    }
}
