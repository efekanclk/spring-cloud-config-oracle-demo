//bu sınıf dışarıdan gelen istekleri karşılayan ve yanıt döndüren bir kapıdır

package com.efekan.server.controller;

import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.service.CEStore;
import com.efekan.server.service.ConfigEnvironmentService;
//üstteki iki  satır ile servis ve dto sınıfını bu sınıfa tanıtıyoruz.

import com.efekan.server.model.ConnectedUsers;
//connectedusers sınıfını bu sınıfa tanıtıyoruz.
import org.springframework.http.ResponseEntity;
//dış dünyaya dönen http yanıtını kapsüllemek için kullanılan spring sınıfını proje dahil etmek için kullanılıyor.
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
//yukarıdakiler ise spring web anotasyonlarını projeye dahil etmek için kullanılıyor.
import jakarta.servlet.http.HttpServletRequest;
//spring'in istemciden gelen http isteğinin detaylarına erişmemizi sağlar.
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//java'nın harita ve çoklu iş parçacığı güvenli veri yapılarını projeye dahil eder

@RestController //bu sınıfın bir restcontroller olduğunu bildirir. verilerin json veya metin olarak body'ye yazılmasını sağlar.
@RequestMapping("/api/v1/config") //controler için ana endpoint yolunu yani base URL'i tanımlamamızı sağlar.

public class ConfigEnvironmentController {

    private final ConfigEnvironmentService configEnvironmentService;
    private final CEStore ceStore;

    public ConfigEnvironmentController(ConfigEnvironmentService configEnvironmentService, CEStore ceStore) {
        this.configEnvironmentService = configEnvironmentService;
        this.ceStore = ceStore;
    }

    // https://gelecegiyazanlar.turkcell.com.tr/egitimler/java

    // Db'dekı tüm property'lerı lısteleyen apı: Gerıye lıste dönderecek,dışarıdan applıcatıon veya profıle veya label veya key alabılır.
    // Bu gelenlere göre db'den verılerı fıltreleyerek ve pagınatıon yaparak çeker.
    // Eğer yoksa tüm property'lerı pagınatıon ıle çeker. Bunlar return edılıır. 201 durum kodu döner


    // Db'ye yenı bır porpery ekleyen apı: Gerıye eklenen property'ı dönderır.
    // dışarıdan applıcatıon ve profıle ve label ve key ve value alır. Bunların herhangı bırı null veya boş ıse hata fırlatır.
    // not:eklenen property ıçın ılgılı clıent refresh apsı çağırılıcak (şımdı yapılmayacak)

    @PutMapping("/property")
    public String updateProperty(@RequestBody ConfigPropertyRequest request) {
        // dışarıdan applıcatıon ve profıle ve label ve key ve value alır. Bunların herhangı bırı null veya boş ıse hata fırlatır.
        // update edıldıkten sonra yenı property return edılıır
        boolean isUpdated = configEnvironmentService.updateConfig(request);
        if (isUpdated) {
            //not: ceStore'dan application ve profile'e göre veri bulunacak. Bulunan verinin ipAddress'ine /actuator/refresh apisine post isteği at. Şimdi yapılmayacak.
            return "Ayar başarıyla güncellendi.";
        }
        throw new RuntimeException("bulunamadı");
    }

    // Db'dekı property'ı sılen apı; Bu apı dışarıdan applıcatıon ve profıle ve key alır.
    // Bulunan kayıt db'den sılınır ve gerıye 204 durum kodu döner ve hıçbışey return etmez

    @GetMapping("/connected-users/")
    public Map<String, ConnectedUsers> getConnectedUser() {
        return ceStore.getConnectedUsersMap();
    }


}
