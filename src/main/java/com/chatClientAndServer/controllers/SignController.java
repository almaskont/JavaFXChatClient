package com.chatClientAndServer.controllers;

import com.chatClientAndServer.Application;
import com.chatClientAndServer.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    private TextField loginReg;
    @FXML
    private TextField passReg;
    @FXML
    private TextField usernameReg;

    private Network network;
    private Application application;
    Stage stage;

    public void checkAuth(String login, String password) {

        if (login == null || password == null) {
            application.showErrorAlert("Ошибка ввода при аутентификации", "Поля не заданы");

        }

        if (login.length() == 0 || password.length() == 0) {
            application.showErrorAlert("Ошибка ввода при аутентификации", "Поля не должны быть пустыми");

            return;
        }

        String authErrorMessage = network.sendAuthMessage(login, password);

        if (authErrorMessage == null) {
            application.openChatDialog(stage);
        } else {
            application.showErrorAlert("Ошибка аутентификации", authErrorMessage);
        }
    }

    @FXML
    public void checkAuth(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        checkAuth(login, password);
    }

    @FXML
    void signUp() {
        String login = loginReg.getText().trim();
        String password = passReg.getText().trim();
        String username = usernameReg.getText().trim();

        if (login.length() == 0 || password.length() == 0 || username.length() == 0) {
            application.showErrorAlert("Ошибка регистрации", "Поля не должны быть пустыми");
            return;
        }

        String signUpErrorMessage = network.sendSignUpMessage(login, password, username);

        if (signUpErrorMessage == null) {
//            startClient.showInformationAlert("Регистрация успешно пройдена", "Можете перейти к аутентификации");
            application.showInformationAlert("Поздравляем с регистрацией!", "Добро пожаловать");
            checkAuth(login, password);
        } else {
            application.showErrorAlert("Ошибка регистрации", signUpErrorMessage);
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }


    public void setApplication(Application application) {
        this.application = application;
    }
}
