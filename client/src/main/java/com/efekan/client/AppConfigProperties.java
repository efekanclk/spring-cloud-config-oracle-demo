package com.efekan.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {

    private String name;
    private int maxConnections;
    private boolean featureToggle;
    private List<String> allowedRoles;
    private String dbPassword;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }

    public boolean isFeatureToggle() { return featureToggle; }
    public void setFeatureToggle(boolean featureToggle) { this.featureToggle = featureToggle; }

    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }

    public String getDbPassword() { return dbPassword; }
    public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }
}