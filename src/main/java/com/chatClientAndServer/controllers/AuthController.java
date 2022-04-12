package com.chatClientAndServer.controllers;

import com.chatClientAndServer.Application;
import com.chatClientAndServer.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    private Network network;
    private Application application;

    @FXML
    public void checkAuth(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.length() == 0 || password.length() == 0) {
            application.showErrorAlert("Ошибка ввода при аутентификации", "Поля не должны быть пустыми");
            return;
        }

        String authErrorMessage = network.sendAuthMessage(login, password);

        if (authErrorMessage == null) {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            application.openChatDialog(stage);
        } else {
            application.showErrorAlert("Ошибка аутентификации", authErrorMessage);
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }


    public void setStartClient(Application startClient) {
        this.application = startClient;
    }

}
