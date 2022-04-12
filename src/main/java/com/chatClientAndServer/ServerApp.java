package com.chatClientAndServer;

import com.chatClientAndServer.server.ChatServer;

import java.io.IOException;

public class ServerApp {
    private static final int DEFAULT_PORT = 8186;

    public static void main(String[] args) {

        try {
            new ChatServer(DEFAULT_PORT).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
