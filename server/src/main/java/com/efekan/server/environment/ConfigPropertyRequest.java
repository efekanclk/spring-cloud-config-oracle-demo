/*bu sınıf dışarıdan gelecek olan veriyi temsil edecek DTO (Data Transfer Object) nesnesi.
db'deki kolon karşılıklarını tutar.
 */
package com.efekan.server.environment;


public class ConfigPropertyRequest {

private String application;
    private String profile;
    private String propKey;
    private String value;
    /*
    Yukarıda oluşturduğumuz değişkenler REST API üzerinden gelecek verideki alanları temsil ederler.
     */

    public ConfigPropertyRequest() {
    }

    public ConfigPropertyRequest(String application, String profile, String propKey, String value) {
        this.application = application;
        this.profile = profile;
        this.propKey = propKey;
        this.value = value;
    }
    /*
    Parametre alan constructor metodlar.Nesnenin oluşturulduğu an initialize etmek içi kullanıyoruz.
     */

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}