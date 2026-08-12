package com.efekan.server.service;

import com.efekan.server.model.ConnectedUsers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ConfigRefreshService {

    private final CEStore ceStore;
    private final RestClient restClient = RestClient.builder().build();

    public ConfigRefreshService(CEStore ceStore) {
        this.ceStore = ceStore;
    }

    @Async("asyncExecutor")
    public void ensembleProperty(String application, String profile) {
        System.out.println("Çalışan Thread: " + Thread.currentThread().getName());
        Map<String, List<ConnectedUsers>> getConnectedUser = ceStore.getConnectedUsersMap();

        for (Map.Entry<String, List<ConnectedUsers>> entry : getConnectedUser.entrySet()) {
            if (entry.getKey().equals(application + "-" + profile)) {
                List<ConnectedUsers> value = entry.getValue();

                List<CompletableFuture<Void>> tasks = value.stream().map(connectedUser -> CompletableFuture.runAsync(() -> {
                            try {
                                String body = restClient.post()
                                        .uri("http://" + connectedUser.ipAddress() + "/actuator/refresh")
                                        .retrieve()
                                        .body(String.class);
                                System.out.println(connectedUser.ipAddress() + ": " + body);
                            } catch (Exception e) {
                                System.out.println("Refresh başarısız.");
                            }
                        })
                ).toList();

                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
                System.out.println("İlgili tüm client'lara asenkron refresh istekleri iletildi.");
            }
        }
    }
}
