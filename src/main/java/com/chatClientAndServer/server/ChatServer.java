package com.chatClientAndServer.server;

import com.chatClientAndServer.server.authentication.AuthenticationService;
import com.chatClientAndServer.server.authentication.BaseAuthenticationService;
import com.chatClientAndServer.server.authentication.DBAuthenticationService;
import com.chatClientAndServer.server.handler.ClientHandler;


import java.io.*;
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
        authenticationService = new DBAuthenticationService();
        clients = new ArrayList<>();
    }

    public void start() {
        authenticationService.startAuthentication();
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

    public synchronized void broadcastMessage(String message, ClientHandler sender, boolean isServerMessage) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(isServerMessage ? null : sender.getUsername(), message);
        }
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        broadcastMessage(message, sender, false);
    }

    public synchronized void privateMessage(String messageText, String recipient, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(recipient)) {
                client.sendMessage(sender.getUsername(), messageText);
            }
        }
    }

    public synchronized void broadcastClients(ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {

            client.sendServerMessage(String.format("%s присоеденился к чату", sender.getUsername()));

            client.sendClientsList(clients);
        }
    }

    public synchronized void broadcastClientDisconnected(ClientHandler sender) throws IOException {

        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendServerMessage(String.format("%s отключился", sender.getUsername()));
            client.sendClientsList(clients);
        }
    }

    public synchronized void updateClients() throws IOException {
        for (ClientHandler client : clients) {
            client.sendClientsList(clients);
            System.out.println(client.getUsername());
        }
    }

    public void updateUsername(String newUsername, ClientHandler client) throws IOException {
        authenticationService.updateUsername(client.getUsername(), newUsername);
        client.setUsername(newUsername);
        updateClients();
    }

}
