package com.efekan.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    @Primary
    public ConfigServicePropertySourceLocator configServicePropertySourceLocator(
            ConfigClientProperties configClientProperties
    ) {
        if (serverPort == null || serverPort.trim().isEmpty()) {
            throw new IllegalStateException("port tanımlı değil");
    }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new CustomInterceptor(serverPort));

        ConfigServicePropertySourceLocator locator = new ConfigServicePropertySourceLocator(configClientProperties);
        locator.setRestTemplate(restTemplate);
        return locator;
    }

    @Bean
    public RestTemplate restTemplate() {

        if (serverPort == null || serverPort.trim().isEmpty()) {
            throw new IllegalStateException("port tanımlı değil");
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new CustomInterceptor(serverPort));
        return restTemplate;

    }
}