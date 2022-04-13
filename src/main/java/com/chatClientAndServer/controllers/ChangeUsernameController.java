package com.chatClientAndServer.controllers;

import com.chatClientAndServer.Application;
import com.chatClientAndServer.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangeUsernameController {

    private Network network;
    private Application application;

    @FXML
    private TextField newUsernameField;

    @FXML
    void changeUsername(ActionEvent event) {
        String newUsername = newUsernameField.getText().trim();

        if (newUsername.length() == 0) {
            application.showErrorAlert("Ошибка ввода при изменение username", "Поля не должны быть пустыми");
            return;
        }

        network.sendChangeUsernameMessage(newUsername);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        application.openChatDialog(stage);
    }

    public void setNetwork(Network network) {
        this.network = network;
    }


    public void setStartClient(Application startClient) {
        this.application = startClient;
    }

}