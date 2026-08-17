package com.efekan.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClientPingService {
    private static final Logger log = LoggerFactory.getLogger(ClientPingService.class);
    private final RestClient restClient = RestClient.builder().build();

    @Value("${spring.application.name}")
    private String application;

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${spring.config.import}")
    private String url;


    @Scheduled(fixedDelay = 5000)
    public void sendPing() {
        try {
            String baseUrl = url.replace("configserver:", "");
            String targetUrl = baseUrl + "/" + application + "/" + profile;

            restClient.get()
                    .uri(targetUrl)
                    .retrieve()
                    .toBodilessEntity();
            log.info("ping atıldı: {} - {}", application, profile);
        } catch (Exception e) {
            String a = e.getLocalizedMessage();
            log.info(a);
            System.out.println("ping başarısız");
        }
    }
}

