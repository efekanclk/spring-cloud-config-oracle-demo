package com.efekan.server.controller;

import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.db.repository.ConfigPropertyPropertyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/config")
public class ConfigWebController {

    private final ConfigPropertyPropertyRepository repository;

    public ConfigWebController(ConfigPropertyPropertyRepository repository) {
        this.repository = repository;
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
        return "redirect:/web/config";
    }

    @PostMapping("/delete/{id}")
    public String deleteProperty(@PathVariable String id) {
        repository.findById(id).ifPresent(repository::delete);
        return "redirect:/web/config";
    }
}