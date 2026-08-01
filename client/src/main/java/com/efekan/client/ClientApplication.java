package com.efekan.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bootstrap.BootstrapRegistry;
import org.springframework.boot.bootstrap.BootstrapRegistryInitializer;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.config.client.ConfigClientRequestTemplateFactory;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        var springApplication = new SpringApplication(ClientApplication.class);

        springApplication.addBootstrapRegistryInitializer(new BootstrapRegistryInitializer() {
            @Override
            public void initialize(BootstrapRegistry registry) {
                try {
                    String serverPort = readPort();
                    registry.registerIfAbsent(RestTemplate.class, context -> {
                        RestTemplate restTemplate = context.get(ConfigClientRequestTemplateFactory.class).create();
                        restTemplate.getInterceptors().add(new CustomInterceptor(serverPort));
                        return restTemplate;
                    });
                } catch (IOException e) {
                    throw new RuntimeException("Port okunurken bir hata oluştu", e);
                }
            }

            public String readPort() throws IOException {
                List<PropertySource<?>> propertySources = new YamlPropertySourceLoader().load("applicationYaml", new ClassPathResource("application.yml"));
                StandardEnvironment environment = new StandardEnvironment();
                propertySources.forEach(source -> environment.getPropertySources().addLast(source));
                String port = environment.getProperty("server.port");

                if (port == null || port.isEmpty()) {
                    throw new RuntimeException("server.port değeri bulunamadı!");
                }
                return port;
            }
        });

        springApplication.run(args);
    }
}