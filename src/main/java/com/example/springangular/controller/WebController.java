package com.example.springangular.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/app", "/app/**"})
    public String index() {
        return "forward:/index.html";
    }
}
