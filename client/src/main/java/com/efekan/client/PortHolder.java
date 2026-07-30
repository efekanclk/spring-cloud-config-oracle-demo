package com.efekan.client;

public class PortHolder {
    private static String serverPort;

    public static String getServerPort(){
        return serverPort;
    }
    public static void setServerPort(String port) {
        serverPort = port;

    }
}
