package com.china.demo.example.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthcheckController {

    @GetMapping("/health")
    public String  helloworld() {
        // TODO: do the magic!
        return "ok";
    }

}
