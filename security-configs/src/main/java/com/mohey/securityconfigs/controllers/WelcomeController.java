package com.mohey.securityconfigs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping
    public Mono<String> greetingMessage(){
        return Mono.just("Welcome Spring Boot Without Security");
    }
}
