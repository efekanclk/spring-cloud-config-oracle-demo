package com.efekan.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private final AppConfigProperties appConfigProperties;

    @Value("${config.key:default-value}")
    private String configValue;

    public Controller(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @GetMapping("/value")
    public String getValue() {
        return "This is my config value: " + configValue;
    }

    @GetMapping("/config-details")
    public Map<String, Object> getConfigDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", appConfigProperties.getName());
        return map;
    }
}