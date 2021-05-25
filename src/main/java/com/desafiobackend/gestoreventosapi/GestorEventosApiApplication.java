package com.desafiobackend.gestoreventosapi;

import com.desafiobackend.gestoreventosapi.domain.user.UserSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class GestorEventosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestorEventosApiApplication.class, args);
    }

    @Autowired
    private UserSeed userSeed;

    @EventListener
    public void initialization(ContextRefreshedEvent event) {
        userSeed.create();
    }
}
