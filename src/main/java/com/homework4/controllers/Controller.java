package com.homework4.controllers;

import com.homework4.Application;
import com.homework4.models.Network;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Controller {

    @FXML
    private Button sendMessageButton;

    @FXML
    private Menu editMenu;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private TextArea textArea;

    @FXML
    private VBox messageBoard;

    @FXML
    private ScrollPane messageScroll;

    @FXML
    private ScrollPane userScroll;

    @FXML
    private VBox usersNames = new VBox();

    @FXML
    private Button closeAboutButton;

    private Stage stage = new Stage();

    private Network network;

    public void setNetwork(Network network) {
        this.network = network;
    }

    @FXML
    void onEnter() {
        textArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    @FXML
    void sendMessage() {
        String messageSend = textArea.getText().trim();
        textArea.clear();
        //dynamically created HBox that represents individual message
        if (!messageSend.isEmpty()) {
            network.sendMessage(messageSend);
            appendMessage(messageSend);
        }
    }

    public void appendMessage(String messageSend) {
        String strDate = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        Text text = new Text(messageSend + "\n\t" + strDate);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(238, 238, 238); " +
                "-fx-background-color: rgb(45, 49, 250); " +
                "-fx-background-radius: 15px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));
        messageBox.getChildren().add(textFlow);
        Platform.runLater(() -> messageBoard.getChildren().add(messageBox));
        messageScroll.vvalueProperty().bind(messageBoard.heightProperty());
    }

    public void appendServerMessage(String messageSend) {
        String strDate = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        Text text = new Text(messageSend + "\n\t" + strDate);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(238, 238, 238); " +
                "-fx-background-color: rgb(93, 139, 244); " +
                "-fx-background-radius: 15px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));
        messageBox.getChildren().add(textFlow);
        Platform.runLater(() -> messageBoard.getChildren().add(messageBox));
        messageScroll.vvalueProperty().bind(messageBoard.heightProperty());
    }

    @FXML
    void onAbout() throws IOException {
        FXMLLoader aboutPageLoader = new FXMLLoader(Application.class.getResource("aboutPage.fxml"));
        stage.setScene(new Scene(aboutPageLoader.load()));
        stage.showAndWait();
    }

    @FXML
    void closeAboutPage(ActionEvent event) {
        stage = (Stage) closeAboutButton.getScene().getWindow();
        if (event.getTarget() == closeAboutButton) {
            stage.close();
        }
    }

    @FXML
    void initialize() {
        userInitialization();
    }

    private void userInitialization() {
        ArrayList<String> users = new ArrayList<>();
        Collections.addAll(users, "Almas", "Bot", "Admin", "Bob", "Someone");
        /*
        For users the procedure is the same as for messages the difference is that we have arraylist
        of the users that we add at the same time to the GUI
        */
        users.forEach((u) -> {
            HBox userBox = new HBox();
            userBox.setAlignment(Pos.CENTER_LEFT);
            userBox.setPadding(new Insets(5, 10, 5, 10));
            Text userName = new Text(u);
            TextFlow textFlow = new TextFlow(userName);
            textFlow.setStyle("-fx-color: rgb(238, 238, 238); " +
                    "-fx-background-color: rgb(45, 49, 250); ");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            userName.setFill(Color.color(0.934, 0.945, 0.996));
            HBox.setHgrow(textFlow, Priority.ALWAYS);
            userBox.getChildren().add(textFlow);
            usersNames.getChildren().add(userBox);
        });
    }
}
