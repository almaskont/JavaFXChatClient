package com.chatClientAndServer.models;

import com.chatClientAndServer.controllers.Controller;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Network {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + password
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg"; // + msg
    private static final String SERVER_MSG_CMD_PREFIX = "/sMsg"; // + msg
    private static final String PRIVATE_MSG_CMD_PREFIX = "/pMsg"; // + msg
    private static final String STOP_SERVER_CMD_PREFIX = "/stop";
    private static final String END_CLIENT_CMD_PREFIX = "/end";
    private static final String GET_CLIENTS_CMD_PREFIX = "/getCls";

    private static final String REG_CMD_PREFIX = "/reg"; //+ login + pass + username
    private static final String REGOK_CMD_PREFIX = "/regok"; //
    private static final String REGERR_CMD_PREFIX = "/regerr"; // + error message
    public static final String CHANGE_USERNAME_PREFIX = "/chngname"; // + login + username

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8186;
    private DataInputStream in;
    private DataOutputStream out;

    private final String host;
    private final int port;
    private String username;
    private final String chatHistoryFilePath = "src/main/resources/chatHistory";

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Network() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }


    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Соединение не установлено");
        }
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при отправке сообщения");
        }
    }

    public void waitMessage(Controller controller) {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();

                    if (message.startsWith(CLIENT_MSG_CMD_PREFIX)) {
                        String[] parts = message.split(":", 3);
                        String sender = parts[1];
                        String messageFromSender = parts[2];
                        String strDate = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
                        writeChatHistory(messageFromSender, "From: " + sender, strDate);
                        Platform.runLater(() -> controller.appendServerMessage(String.format("%s: %s", sender, messageFromSender), strDate));
                    } else if (message.startsWith(SERVER_MSG_CMD_PREFIX)) {
                        String[] parts = message.split(":", 2);
                        String serverMessage = parts[1];
                        String strDate = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
                        writeChatHistory(serverMessage, "", strDate);
                        Platform.runLater(() -> controller.appendMessage(serverMessage, strDate));
                    } else if (message.startsWith(GET_CLIENTS_CMD_PREFIX)) {
                        message = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
                        String[] users = message.split(", ");
                        Platform.runLater(() -> controller.updateUsersList(users));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();
    }

    public void writeChatHistory(String message, String user, String date) {
        String fileName = "chatHistoryOf" + username + ".txt";
        File file = new File(chatHistoryFilePath, fileName);
        String messageWithDate = ((user.isEmpty()) ? (message + "~" + date) : (message + "~" + date + "~" + user));
        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.exists()) {
                writer.write(messageWithDate + "\n");
            } else {
                file.createNewFile();
                writer.write(messageWithDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readChatHistory(Controller controller) {
        String fileName = "chatHistoryOf" + username + ".txt";
        File file = new File("src/main/resources/chatHistory", fileName);
        String line;
        if (!file.exists())
            return;
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("~");
                String message = parts[0];
                String date = parts[1];
                if (parts.length == 3) {
                    String user = parts[2];
                    if (user.startsWith("From: ")) {
                        user = user.replaceFirst("From: ", "");
                        controller.appendServerMessage(String.format("%s: %s", user, message), date);
                    } else {
                        controller.appendMessage(user + "\n" + message, date);
                    }
                    continue;
                }
                controller.appendMessage(message, date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendAuthMessage(String login, String password) {
        try {
            out.writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = in.readUTF();

            if (response.startsWith(AUTHOK_CMD_PREFIX)) {
                this.username = response.split("\\s+", 2)[1];
                return null;
            } else {
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public String getUsername() {
        return username;
    }

    public void sendPrivateMessage(String selectedRecipient, String message) {
        sendMessage(String.format("%s:%s:%s", PRIVATE_MSG_CMD_PREFIX, selectedRecipient, message));
    }

    public String sendSignUpMessage(String login, String password, String username) {
        try {
            out.writeUTF(String.format("%s %s %s %s", REG_CMD_PREFIX, login, password, username));
            String response = in.readUTF();
            if (response.startsWith(REGOK_CMD_PREFIX)) {
                return null;
            } else {
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public void sendChangeUsernameMessage(String newUsername) {
        this.username = newUsername;
        sendMessage(String.format("%s:%s:%s", CHANGE_USERNAME_PREFIX, username, newUsername));
    }
}
