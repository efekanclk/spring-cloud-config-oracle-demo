package com.efekan.server.environment;

public record ConfigPropertyRequest(
        String application,
        String profile,
        String label,
        String propKey,
        String value
) {}