package com.homework4.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatServer {
    private static final int SERVER_PORT = 8186;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner scanner = new Scanner(System.in);
    private static String serverMessage = null;


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Ожидание подключения...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Соединение установлено!");

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            Thread sendingThread = new Thread(() -> {
                while (true) {
                    serverMessage = scanner.nextLine().trim();
                    try {
                        if (!serverMessage.isBlank())
                            out.writeUTF(serverMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendingThread.setDaemon(true);
            sendingThread.start();
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.equals("/server-stop")) {
                        System.out.println("Сервер остановлен");
                        System.exit(0);
                    }
                    System.out.println("Клиент: " + message);
                }
            } catch (SocketException e) {
                clientSocket.close();
                System.out.println("Клиент отключился");
                scanner.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
