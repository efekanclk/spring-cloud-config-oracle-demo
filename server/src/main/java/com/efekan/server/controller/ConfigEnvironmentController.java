//bu sınıf dışarıdan gelen istekleri karşılayan ve yanıt döndüren bir kapıdır

package com.efekan.server.controller;

import com.efekan.server.environment.ConfigPropertyRequest;
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
@CrossOrigin(origins = "*") //farklı kaynaklardan gelen isteklere izin verir (CORS kısıtlaması için)

public class ConfigEnvironmentController {

    private final ConfigEnvironmentService configEnvironmentService;

    private final Map<String, ConnectedUsers> connectedUsersMap = new ConcurrentHashMap<>();
    //sisteme bağlanan kullanıcıları applicaton-profile anahtarıyla hafızada tutar.
    public ConfigEnvironmentController(ConfigEnvironmentService configEnvironmentService) {
        this.configEnvironmentService = configEnvironmentService;
    //controller'ın iş kurallarını çalıştırabilmek için servis katmanına bağlanmasını sağlar.
    }

    @PutMapping("/property") //Endpoint metodu,HTTP PUT isteklerini dinlememizi sağlar.
    public ResponseEntity<String> updateProperty(@RequestBody ConfigPropertyRequest request) {
        /*
        gelen HTTP isteğinin body'sindeki json verisinin otomatik olarak java nesnesine çevrilmesini sağlar.
        */
        boolean isUpdated = configEnvironmentService.updateConfig(request);
        if (isUpdated) {
            return ResponseEntity.ok("Ayar başarıyla güncellendi.");
        } else {
            return ResponseEntity.notFound().build();
        }
        //servis katmanını çağırır ve boolean sonucuna göre yanıt döndürür.
        /*true ise 200 durum kodunu (OK) döndürür. false ise applicaton, profile ve prop_key kombinasyonuna uygun bir
        kayıt kayıt bulunamadı demektir, bunun sonucunda 404 döndürür */
    }

    @GetMapping("/connected-users") //HTTP GET isteklerini dinlememizi sağlayan metod.
    public ResponseEntity<Map<String, ConnectedUsers>> getConnectedUser(
            @RequestParam String application,
            @RequestParam String profile,
            HttpServletRequest request
            //URL üzerinden gelen application ve profile parametreleri ile istemcinin IP adresini yakalamak için kullanılan metod.
    ) {

        // İsteği atan kullanıcının IP adresini alıyoruz
        String ipAddress = request.getRemoteAddr();

        // Araya tire (-) koyarak unique key oluşturuyoruz
        String uniqueKey = application + "-" + profile;

        // Model nesnesini oluşturuyoruz
        ConnectedUsers connectedUser = new ConnectedUsers(application, profile, ipAddress);

        // Map'e ekliyoruz. Aynı key varsa üzerine yazar (günceller), yoksa yeni ekler
        connectedUsersMap.put(uniqueKey, connectedUser);

        // Güncel haritanın tamamını dönüyoruz
        return ResponseEntity.ok(connectedUsersMap);
    }
}
