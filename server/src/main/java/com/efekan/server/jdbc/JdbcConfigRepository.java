/*
bu sınıf properties tablosuna bağlanıp update sorgusunu çalıştıran katmandır.
 */
package com.efekan.server.jdbc;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository  //bu sınıfın bir erişim katmanı olduğunu spring'e bildiren etiket
public class JdbcConfigRepository {private final JdbcClient jdbcClient;

    public JdbcConfigRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //construstor metod, spring boot'un sunduğu jdbcdlient bileşenini sınıfa dahil etmek için kullanılıyor.

    public int updateConfig(String application, String profile, String propKey, String value) {
        //servis katmanından gelen 4 parametreyi sql sorgusuna iletmek için güncelleme metodu
        String updateSql = """
            UPDATE PROPERTIES 
            SET VALUE = :val 
            WHERE APPLICATION = :app 
              AND PROFILE = :prof 
              AND PROP_KEY = :pkey
            """;
        //properties tablosundaki value değerini güncellemek için tanımlanan SQL ifadesi.

        return jdbcClient.sql(updateSql) /*dışarıdan gelen verileri SQL'deki parametrelerle eşleştirir ve
             sorguyu db'ye gönderir. parametre bağlama kullanarak dışarıdan gelen metinlerin sql komutunun bir
             parçası gibi çalıştırılması engelleniyor. bunu kullanmasaydık SQL injection kullanılarak db'de yetkisiz
             işlemler yapılabilirdi*/
                .param("val", value)
                .param("app", application)
                .param("prof", profile)
                .param("pkey", propKey)
                .update();
    }
}
