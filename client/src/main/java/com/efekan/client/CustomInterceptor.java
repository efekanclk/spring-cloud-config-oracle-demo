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
    private final String serverPort;
    public CustomInterceptor(String serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte [] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("X-Client-Port", serverPort);
        System.out.println("URL " + request.getURI());
        return execution.execute(request, body);
    }
}