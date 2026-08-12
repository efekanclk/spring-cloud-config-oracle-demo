package com.efekan.server.controller;
import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.model.ConnectedUsers;
import com.efekan.server.db.repository.ConfigPropertyPropertyRepository;
import com.efekan.server.service.CEStore;
import com.efekan.server.service.ConfigRefreshService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigEnvironmentController {

    private final ConfigPropertyPropertyRepository configPropertyPropertyRepository;
    private final CEStore ceStore;
    private final ConfigRefreshService configRefreshService;

    public ConfigEnvironmentController(ConfigPropertyPropertyRepository configPropertyPropertyRepository,
                                       CEStore ceStore,
                                       ConfigRefreshService configRefreshService) {
        this.configPropertyPropertyRepository = configPropertyPropertyRepository;
        this.ceStore = ceStore;
        this.configRefreshService = configRefreshService;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/property")
    public ConfigProperty createProperty(@RequestBody ConfigProperty configProperty) {
        ConfigProperty save = configPropertyPropertyRepository.save(configProperty);
        configRefreshService.ensembleProperty(configProperty.getApplication(), configProperty.getProfile());
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
        configRefreshService.ensembleProperty(request.application(), request.profile());
        return save;
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