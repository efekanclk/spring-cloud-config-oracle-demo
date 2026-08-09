package com.efekan.server.service;

import com.efekan.server.model.ConnectedUsers;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CEStore {

    private final Map<String, List<ConnectedUsers>> connectedUsersMap = new ConcurrentHashMap<>();

    public Map<String, List<ConnectedUsers>> getConnectedUsersMap() {
        return connectedUsersMap;
    }

    public void put(String application, String profile, String ipAddress) {
        String uniqueKey = application + "-" + profile;
        ConnectedUsers connectedUser = new ConnectedUsers(application, profile, ipAddress);

        List<ConnectedUsers> connectedUsers = connectedUsersMap.computeIfAbsent(uniqueKey, k -> new ArrayList<>());
        if (!connectedUsers.contains(connectedUser)) {
            connectedUsers.add(connectedUser);
        }
    }
}