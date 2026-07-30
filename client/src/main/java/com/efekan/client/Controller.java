package com.efekan.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private final AppConfigProperties appConfigProperties;
    private final RestTemplate restTemplate;

    @Value("${config.key:default-value}")
    private String configValue;

    public Controller(AppConfigProperties appConfigProperties, RestTemplate restTemplate) {
        this.appConfigProperties = appConfigProperties;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/value")
    public String getValue() {
        return "This is my config value: " + configValue;
    }

    @GetMapping("/config-details")
    public Map<String, Object> getConfigDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("dbPassword", appConfigProperties.getDbPassword());
        return map;
    }

    @GetMapping("/trigger")
    public String trigger() {

        return restTemplate.getForObject("http://localhost:8888/myapp/default", String.class);
    }
}