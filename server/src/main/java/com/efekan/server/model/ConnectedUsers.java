package com.efekan.server.model;

public record ConnectedUsers(
        String application,
        String profile,
        String ipAddress
) {}