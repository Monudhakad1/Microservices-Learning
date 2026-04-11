package com.usermicro.userservicemicroservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceMicroservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceMicroservicesApplication.class, args);
    }


}
