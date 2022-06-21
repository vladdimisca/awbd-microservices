package com.awbd.eurekanamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
@RefreshScope
public class EurekaNamingserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaNamingserverApplication.class, args);
    }

}
