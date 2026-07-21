package com.example.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Value("${config.key:default-value}")
    private String configValue;

    @GetMapping("/value")
    public String getValue() {
        return "This is my config value: " + configValue;
    }
}