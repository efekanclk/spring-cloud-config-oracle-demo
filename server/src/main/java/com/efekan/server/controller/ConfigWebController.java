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

    // Tabloyu listeleme sayfası
    @GetMapping
    public String listProperties(Model model) {
        System.out.println("DEBUG: ConfigWebController.listProperties method entered!");
        // Tüm verileri DB'den çekip "properties" adıyla HTML'e yolluyoruz
        model.addAttribute("properties", repository.findAll());
        return "index";
    }

    // Düzenleme (Edit) sayfasını açma
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) { // ID artık String!
        ConfigProperty property = repository.findById(id).orElse(null);
        if (property == null) {
            return "redirect:/web/config"; // Bulunamazsa ana sayfaya at
        }
        model.addAttribute("property", property);
        return "edit";
    }

    // Düzenlenen veriyi DB'ye kaydetme
    @PostMapping("/update/{id}")
    public String updateProperty(@PathVariable String id, @ModelAttribute ConfigProperty property) {
        property.setId(id); // Güncellerken ID'nin değişmemesini garanti ediyoruz
        repository.save(property);
        return "redirect:/web/config";
    }

    // Veriyi silme
    @PostMapping("/delete/{id}")
    public String deleteProperty(@PathVariable String id) {
        repository.findById(id).ifPresent(repository::delete);
        return "redirect:/web/config";
    }
}