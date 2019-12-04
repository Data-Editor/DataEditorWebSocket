package com.niek125.updateserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class UpdateServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdateServerApplication.class, args);
    }

}
