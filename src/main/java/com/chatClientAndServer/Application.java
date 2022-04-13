package com.chatClientAndServer;

import com.chatClientAndServer.controllers.*;
import com.chatClientAndServer.models.Network;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private Network network;
    private Stage primaryStage;
    private Stage authStage;
    private Stage aboutStage;
    private Controller chatController;

    @Override
    public void start(Stage stage) throws IOException {

        this.primaryStage = stage;

        network = new Network();
        network.connect();

        openAuthDialog();
        createChatDialog();
    }

    private void openAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader(Application.class.getResource("loginPage.fxml"));
        authStage = new Stage();
        Scene scene = new Scene(authLoader.load());

        authStage.setScene(scene);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        authStage.setTitle("Authentication");
        authStage.setAlwaysOnTop(true);
        authStage.show();

        SignController signController = authLoader.getController();

        signController.setNetwork(network);
        signController.setApplication(this);
    }

    private void createChatDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);

        chatController = fxmlLoader.getController();

        chatController.setNetwork(network);
        chatController.setApplication(this);
    }

    public static void main(String[] args) {
        launch();
    }

    public void openChatDialog(Stage stage) {
        stage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        network.waitMessage(chatController);
        chatController.setUsernameTitle(network.getUsername());
    }

    public void openChangeUsernameDialog() throws IOException {
        FXMLLoader changeUsernamePage = new FXMLLoader(Application.class.getResource("changeUsernamePage.fxml"));
        Stage changeUsernameStage = new Stage();
        Scene scene = new Scene(changeUsernamePage.load());
        changeUsernameStage.setScene(scene);
        changeUsernameStage.initModality(Modality.WINDOW_MODAL);
        changeUsernameStage.initOwner(primaryStage);
        changeUsernameStage.setTitle("Change Username");
        changeUsernameStage.setAlwaysOnTop(true);
        changeUsernameStage.show();

        ChangeUsernameController changeUsernameController = changeUsernamePage.getController();

        changeUsernameController.setNetwork(network);
        changeUsernameController.setStartClient(this);
    }

    public void openAboutDialog() throws IOException {
        FXMLLoader aboutPageLoader = new FXMLLoader(Application.class.getResource("aboutPage.fxml"));
        aboutStage = new Stage();
        aboutStage.setScene(new Scene(aboutPageLoader.load()));
        aboutStage.initModality(Modality.WINDOW_MODAL);
        aboutStage.initOwner(primaryStage);
        aboutStage.setTitle("About");
        aboutStage.setAlwaysOnTop(true);
        aboutStage.show();

        AboutController aboutController = aboutPageLoader.getController();
        aboutController.setStartClient(this);
    }

    public void showErrorAlert(String title, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(errorMessage);
        alert.show();
    }

    public void showInformationAlert(String title, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(errorMessage);
        alert.show();
    }
}