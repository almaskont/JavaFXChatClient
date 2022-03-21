package com.homework4.server;

import com.homework4.server.authentication.AuthenticationService;
import com.homework4.server.authentication.BaseAuthenticationService;
import com.homework4.server.handler.ClientHandler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final List<ClientHandler> clients;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new BaseAuthenticationService();
        clients = new ArrayList<>();
    }

    public void start() {
        System.out.println("СЕРВЕР ЗАПУЩЕН!");
        System.out.println("----------------");

        try {
            while(true) {
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitAndProcessNewClientConnection() throws IOException {
        System.out.println("Ожидание клиента...");
        Socket socket = serverSocket.accept();
        System.out.println("Клиент подключился!");

        processClientConnection(socket);
    }

    private void processClientConnection(Socket socket) throws IOException {
        ClientHandler handler = new ClientHandler(this, socket);
        handler.handle();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public synchronized boolean isUsernameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(sender.getUsername(), message);
        }
    }

    public synchronized void privateMessage(String message, ClientHandler sender) throws IOException {
        String[] messageArray = message.split("\\s");
        if (messageArray.length < 3) {
            sender.sendMessage(sender.getUsername(), "вы отправили пустое сообщение");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < messageArray.length; i++) {
            sb.append(messageArray[i]);
            sb.append(" ");
        }
        if(!isUsernameBusy(messageArray[1]))
            sender.noUsername(messageArray[1]);
        for (ClientHandler client : clients) {
            if(client.getUsername().equals(messageArray[1])) {
                client.sendMessage(sender.getUsername(), sb.toString());
                return;
            }
        }
    }
}
