package com.efekan.server.controller;

import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.db.repository.ConfigPropertyPropertyRepository;
import com.efekan.server.model.ConnectedUsers;
import com.efekan.server.service.CEStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/config")
public class ConfigWebController {

    private final ConfigPropertyPropertyRepository repository;
    private final CEStore ceStore;
    private final RestClient restClient = RestClient.builder().build();

    public ConfigWebController(ConfigPropertyPropertyRepository repository, CEStore ceStore) {
        this.repository = repository;
        this.ceStore = ceStore;
    }

    @GetMapping
    public String listProperties(Model model) {
        model.addAttribute("properties", repository.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        ConfigProperty property = repository.findById(id).orElse(null);
        if (property == null) {
            return "redirect:/web/config";
        }
        model.addAttribute("property", property);
        return "edit";
    }

    @PostMapping("/update/{id}")
    public String updateProperty(@PathVariable String id, @ModelAttribute ConfigProperty property) {
        property.setId(id);
        repository.save(property);
        ensembleProperty(property.getApplication(), property.getProfile());
        return "redirect:/web/config";
    }

    @PostMapping("/delete/{id}")
    public String deleteProperty(@PathVariable String id) {
        repository.findById(id).ifPresent(repository::delete);
        return "redirect:/web/config";
    }

    void ensembleProperty(String application, String profile){
        Map<String, List<ConnectedUsers>> getConnectedUser = ceStore.getConnectedUsersMap();
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
}