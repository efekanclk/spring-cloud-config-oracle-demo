package com.efekan.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bootstrap.BootstrapRegistry;
import org.springframework.boot.bootstrap.BootstrapRegistryInitializer;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.config.client.ConfigClientRequestTemplateFactory;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        var efe = new SpringApplication(ClientApplication.class);
        efe.addBootstrapRegistryInitializer(new BootstrapRegistryInitializer() {
            @Override
            public void initialize(BootstrapRegistry registry) {
                try {
                    String port = readPort();
                    registry.registerIfAbsent(RestTemplate.class, context -> {
                        RestTemplate degisken = context.get(ConfigClientRequestTemplateFactory.class).create();
                        degisken.getInterceptors().add(new CustomInterceptor(port));
                        return degisken;
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String readPort() throws IOException {
                List<PropertySource<?>> abc = new YamlPropertySourceLoader().load("İsim", new ClassPathResource("application.yml"));
                StandardEnvironment anil = new StandardEnvironment();
                abc.forEach(source -> anil.getPropertySources().addLast(source));
                String port = anil.getProperty("server.port");

                if (port == null || port.isEmpty()) {
                    throw new RuntimeException();
                }
                return port;
            }
        });
        efe.run(args);
    }

}