package com.efekan.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String serverPort = PortHolder.getServerPort();

        if (serverPort != null && !serverPort.trim().isEmpty()) {
            request.getHeaders().add("X-Client-Port", serverPort);
            log.info("URL {} için X-Client-Port eklendi: {}", request.getURI(), serverPort);
        } else {
            log.warn("URL {} için port bilgisi bulunamadı!", request.getURI());
        }

        return execution.execute(request, body);
    }
}