package com.efekan.server.controller;
import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.model.ConnectedUsers;
import com.efekan.server.db.repository.ConfigPropertyPropertyRepository;
import com.efekan.server.service.CEStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigEnvironmentController {

    private final ConfigPropertyPropertyRepository configPropertyPropertyRepository;
    private final CEStore ceStore;
    private final RestClient restClient = RestClient.create();

    public ConfigEnvironmentController(ConfigPropertyPropertyRepository configPropertyPropertyRepository,
                                       CEStore ceStore) {
        this.configPropertyPropertyRepository = configPropertyPropertyRepository;
        this.ceStore = ceStore;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/property")
    public ConfigProperty createProperty(@RequestBody ConfigProperty configProperty) {
        ConfigProperty save = configPropertyPropertyRepository.save(configProperty);
        ensembleProperty(configProperty.getApplication(), configProperty.getProfile());
        return save;
    }

    @PutMapping(path = "/property")
    public ConfigProperty updateProperty(@RequestBody ConfigPropertyRequest request) {
        ConfigProperty configProperty = configPropertyPropertyRepository.findByApplicationAndProfileAndLabelAndKey(
                request.application(), request.profile(), request.label(), request.propKey());

        if (configProperty == null) {
            throw new RuntimeException("bulunamadı");
        }

        configProperty.setValue(request.value());
        ConfigProperty save = configPropertyPropertyRepository.save(configProperty);
        ensembleProperty(request.application(), request.profile());
        return save;
    }

    void ensembleProperty(String application, String profile){
        Map<String, List<ConnectedUsers>> getConnectedUser = getConnectedUser();
        for (Map.Entry<String, List<ConnectedUsers>> entry : getConnectedUser.entrySet()) {
            if (entry.getKey().equals(application + "-" + profile)) {
                List<ConnectedUsers> value = entry.getValue();
                value.forEach(connectedUser -> {
                    String body = restClient.post()
                            .uri("http://" + connectedUser.ipAddress() + "/actuator/refresh")
                            .retrieve()
                            .body(String.class);
                    System.out.println("body: " + body);
                });
            }
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/property/{name}/{profiles}/{label}/{key}")
    public void updateProperty(@PathVariable String name, @PathVariable String profiles,
                               @PathVariable String label, @PathVariable String key) {

        ConfigProperty configProperty = configPropertyPropertyRepository.findByApplicationAndProfileAndLabelAndKey(name, profiles, label, key);

        if (configProperty == null) {
            throw new RuntimeException("bulunamadı");
        }

        configPropertyPropertyRepository.delete(configProperty);
    }

    @GetMapping("/connected-users/")
    public Map<String, List<ConnectedUsers>> getConnectedUser() {
        return ceStore.getConnectedUsersMap();
    }
}