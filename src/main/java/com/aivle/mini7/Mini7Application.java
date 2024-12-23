package com.aivle.mini7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Mini7Application {

    public static void main(String[] args) {
        SpringApplication.run(Mini7Application.class, args);
    }

}
