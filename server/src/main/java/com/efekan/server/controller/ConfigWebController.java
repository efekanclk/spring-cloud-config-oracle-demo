package com.efekan.server.controller;

import com.efekan.server.db.entity.ConfigProperty;
import com.efekan.server.db.repository.ConfigPropertyRepository;
import com.efekan.server.service.AuditService;
import com.efekan.server.service.ConfigRefreshService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/config")
public class ConfigWebController {

    private final ConfigPropertyRepository repository;
    private final ConfigRefreshService configRefreshService;
    private final AuditService auditService; // 1. Eklendi

    // Constructor güncellendi
    public ConfigWebController(ConfigPropertyRepository repository,
                               ConfigRefreshService configRefreshService,
                               AuditService auditService) {
        this.repository = repository;
        this.configRefreshService = configRefreshService;
        this.auditService = auditService;
    }

    @GetMapping
    public String listProperties(Model model) {
        model.addAttribute("properties", repository.findAll());
        return "index";
    }

    // 2. Audit sayfası için yeni endpoint eklendi
    @GetMapping("/audit")
    public String showAuditHistory(Model model) {
        model.addAttribute("audits", auditService.getAllRevisions());
        return "audit";
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