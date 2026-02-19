package com.example.springangular.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/api")
    public String home() {
        return "Spring Boot base application is running";
    }

    @GetMapping("/api/health")
    public String health() {
        return "UP";
    }
}
