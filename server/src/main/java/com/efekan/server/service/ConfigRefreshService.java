package com.efekan.server.service;

import com.efekan.server.model.ConnectedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class ConfigRefreshService {

    private static final Logger log = LoggerFactory.getLogger(ConfigRefreshService.class);
    private final CEStore ceStore;
    private final RestClient restClient = RestClient.builder().build();

    public ConfigRefreshService(CEStore ceStore) {
        this.ceStore = ceStore;
    }

    @Async("asyncExecutor")
    public void ensembleProperty(String application, String profile) {
        log.info("Çalışan Thread: " + Thread.currentThread().getName());
        Map<String, List<ConnectedUsers>> getConnectedUser = ceStore.getConnectedUsersMap();

        for (Map.Entry<String, List<ConnectedUsers>> entry : getConnectedUser.entrySet()) {
            if (entry.getKey().equals(application + "-" + profile)) {
                List<ConnectedUsers> value = entry.getValue();

                value.forEach(connectedUser -> {
                            try {
                                String body = restClient.post()
                                        .uri("http://" + connectedUser.ipAddress() + "/actuator/refresh")
                                        .retrieve()
                                        .body(String.class);
                                log.info(connectedUser.ipAddress() + ": " + body);
                            } catch (Exception e) {
                                log.info("refresh çalıştırılmadı");
                            }
                        });

                log.info("İlgili tüm client'lara asenkron refresh istekleri iletildi.");
            }
        }
    }
}
