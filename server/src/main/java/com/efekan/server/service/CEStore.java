package com.efekan.server.service;

import com.efekan.server.model.ConnectedUsers;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CEStore {
    private final Map<String, ConnectedUsers> connectedUsersMap = new ConcurrentHashMap<>();

    public Map<String , ConnectedUsers> getConnectedUsersMap() {return connectedUsersMap;}

    public void put(String application, String profile, String ipAddress){
        String uniqueKey = application + "-" + profile;
        ConnectedUsers connectedUser = new ConnectedUsers(application, profile, ipAddress);
        connectedUsersMap.put(uniqueKey, connectedUser);
    }
}