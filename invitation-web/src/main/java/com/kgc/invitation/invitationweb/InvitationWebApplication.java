package com.kgc.invitation.invitationweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class InvitationWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvitationWebApplication.class, args);
    }

}
