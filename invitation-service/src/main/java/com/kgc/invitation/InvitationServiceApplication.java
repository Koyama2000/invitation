package com.kgc.invitation;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubbo
@SpringBootApplication
public class InvitationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvitationServiceApplication.class, args);
    }

}
