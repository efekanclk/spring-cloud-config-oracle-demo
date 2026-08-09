package com.efekan.server.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROPERTIES")
public class ConfigProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column
    private String application;

    @Column
    private String profile;

    @Column
    private String label;

    @Column(name = "PROP_KEY")
    private String key;

    @Column(name = "VALUE", columnDefinition = "CLOB", length = 1000)
    private String value;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }

    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}