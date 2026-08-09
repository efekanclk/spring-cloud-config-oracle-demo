package com.efekan.server.controller;

import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.service.CEStore;
import com.efekan.server.service.ConfigEnvironmentService;

import com.efekan.server.model.ConnectedUsers;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")

public class ConfigEnvironmentController {

    private final ConfigEnvironmentService configEnvironmentService;
    private final CEStore ceStore;

    public ConfigEnvironmentController(ConfigEnvironmentService configEnvironmentService, CEStore ceStore) {
        this.configEnvironmentService = configEnvironmentService;
        this.ceStore = ceStore;
    }

    // https://gelecegiyazanlar.turkcell.com.tr/egitimler/java

    // Db'deki tüm property'leri lısteleyen API: Geriye liste dönderecek,dışarıdan application veya profile veya label veya key alabilir.
    // Bu gelenlere göre DB'den verileri filtreleyerek ve pagination yaparak çeker.
    // Eğer yoksa tüm property'leri pagination ile çeker. Bunlar return edilir. 201 durum kodu döner


    // Db'ye yeni bir property ekleyen apı: Geriye eklenen property'i dönderir.
    // dışarıdan application ve profile ve label ve key ve value alır. Bunların herhangı biri null veya boş ise hata fırlatır.
    // not:eklenen property için ilgili client refresh API'si çağrılacak. (Şimdi yapılmayacak.)

    @PutMapping("/property")
    public String updateProperty(@RequestBody ConfigPropertyRequest request) {
        // Db'deki tüm property'leri lısteleyen API: Geriye liste dönderecek,dışarıdan application veya profile veya label veya key alabilir.
        // Update edildikten sonra yeni property return edilir.
        boolean isUpdated = configEnvironmentService.updateConfig(request);
        if (isUpdated) {
            //not: ceStore'dan application ve profile'e göre veri bulunacak. Bulunan verinin ipAddress'ine /actuator/refresh apisine post isteği at. Şimdi yapılmayacak.
            return "Ayar başarıyla güncellendi.";
        }
        throw new RuntimeException("bulunamadı");
    }

    // Db'deki property'i silen API; Bu API dışarıdan application ve profile ve key alır.
    // Bulunan kayıt db'den silinir ve gerıye 204 durum kodu döner ve hiçbir şey return etmez

    @GetMapping("/connected-users/")
    public Map<String, ConnectedUsers> getConnectedUser() {
        return ceStore.getConnectedUsersMap();
    }

}
