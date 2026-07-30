package com.efekan.client;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ServerPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext){
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String port = environment.getProperty("server.port");

        if (port == null || port.trim().isEmpty()){
            throw new IllegalStateException("server port tanımlı değildir");
        }

        PortHolder.setServerPort(port);
    }
}
