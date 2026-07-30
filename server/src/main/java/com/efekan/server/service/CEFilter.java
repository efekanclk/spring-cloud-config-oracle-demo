package com.efekan.server.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class CEFilter extends OncePerRequestFilter {
    private final CEStore ceStore;

    public CEFilter(CEStore ceStore){
        this.ceStore = ceStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI != null && !requestURI.contains("/actuator")) {
            String[] split = requestURI.split("/");

            if (split.length >= 3) {
                String application = split[1];
                String profile = split[2];
                String remoteAddr = request.getRemoteAddr();

                String clientPort = request.getHeader("X-Client-Port");

                boolean isConfigServerCall = split.length == 3 && !requestURI.contains("test");

                if (clientPort == null || clientPort.trim().isEmpty()) {
                    if (!isConfigServerCall) {
                        throw new IllegalStateException("İstekte X-Client-Port header'ı bulunamadı!");
                    }
                } else {
                    String ipPort = remoteAddr + ":" + clientPort;
                    ceStore.put(application, profile, ipPort);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}