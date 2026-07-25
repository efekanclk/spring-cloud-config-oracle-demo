/*iş katmanı. bu sınıf controller ve veri tabanı arasında köprü görevi görür.
controller http trafiğini yönetir. iş kurallarını servis sınıfına yazıyoruz ki
iş kurallarını  daha temiz bir şekilde yönetebilelim.
*/

package com.efekan.server.service;
import com.efekan.server.environment.ConfigPropertyRequest;
import com.efekan.server.jdbc.JdbcConfigRepository;
import org.springframework.stereotype.Service;

@Service //bu anotasyon bu sınıfın bussines mantığının yönetildiği sınıf olduğunu spring boot'a bildiriyor.
public class ConfigEnvironmentService {

    private final JdbcConfigRepository jdbcConfigRepository;
    //db sorgularını çalıştıran JdbcConfigRepository sınıfını bu servisin içine dahil ediyoruz.

    /*servis katmanının db'ye erişmesini sağlıyor ama SQL sorgularının detayını bilmiyor.sadece metodu çağırıyor.
    db sorgularını repository'de izole ediyoruz ki db'ye erişim mantığını ve iş katmanını birbirinden ayıralım
    yani ilerde tablo yapısı değişirse veya direkt db değişirse -örneğin redise geçecek olalım- bu service katmanına
    dokunmadan değişiklik yapabiliriz.
    */
    public ConfigEnvironmentService(JdbcConfigRepository jdbcConfigRepository) {
        this.jdbcConfigRepository = jdbcConfigRepository;
    }

    public boolean updateConfig(ConfigPropertyRequest request) {
        /*
        Controllerden gelen ConfigPropertyRequest sınıfını açar, içindeki alanları ayırıp repository'e gönderir.
         */
        int updatedRows = jdbcConfigRepository.updateConfig(
                request.getApplication(),
                request.getProfile(),
                request.getPropKey(),
                request.getValue()
        );

        return updatedRows > 0;
    }
        /*
        çalışma mantığı ise şu şekilde, yukarıda güncellenen satırların sayısal bir değer olarak dönüyor,
        altta ise güncellenen satırların sayısı 0dan büyük mü (herhangi bir satır güncellenmiş mi) kontrolü yapılıyor.
        eğer 0'dan büyükse true dönüyor. bu şekilde güncelleme yapılmış mı bunu kontrol etmiş oluyoruz.
        */
}
