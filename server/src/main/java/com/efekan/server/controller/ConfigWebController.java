package com.efekan.server.controller;

import ch.qos.logback.core.model.processor.PhaseIndicator;
import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.db.repository.ConfigPropertyPropertyRepository;
import com.efekan.server.model.ConnectedUsers;
import com.efekan.server.service.CEStore;
import com.efekan.server.service.ConfigRefreshService;
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
    private final ConfigRefreshService configRefreshService;

    public ConfigWebController(ConfigPropertyPropertyRepository repository, ConfigRefreshService configRefreshService) {
        this.repository = repository;
        this.configRefreshService = configRefreshService;
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
        configRefreshService.ensembleProperty(property.getApplication(), property.getProfile());
        return "redirect:/web/config";
    }

    @PostMapping("/delete/{id}")
    public String deleteProperty(@PathVariable String id) {
        repository.findById(id).ifPresent(repository::delete);
        return "redirect:/web/config";
    }

    @GetMapping("/insert")
    public String insertForm(Model model){
        model.addAttribute("property", new ConfigProperty());
        return "insert";
    }

    @PostMapping("/save")
    public String insertProperty(@ModelAttribute ConfigProperty property){
        repository.save(property);
        configRefreshService.ensembleProperty(property.getApplication(), property.getProfile());
        return "redirect:/web/config";
    }
}