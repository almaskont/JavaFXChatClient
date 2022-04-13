package com.chatClientAndServer.controllers;

import com.chatClientAndServer.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AboutController {
    private Application application;

    @FXML
    void closeAboutPage(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        application.openChatDialog(stage);
    }

    public void setStartClient(Application startClient) {
        this.application = startClient;
    }
}
