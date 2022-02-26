package com.howework4;

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

    @FXML
    void onEnter() {
        textArea.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    @FXML
    void sendMessage() {
        String messageSend = textArea.getText();
        String strDate = new SimpleDateFormat("dd.MM hh:mm").format(new Date());
        if (!messageSend.isEmpty()) {
            HBox messageBox = new HBox();
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageBox.setPadding(new Insets(5, 10, 5, 10));
            Text text = new Text(messageSend + "\n\t" + strDate);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(238, 238, 238); " +
                    "-fx-background-color: rgb(57, 138, 185); " +
                    "-fx-background-radius: 15px;");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934, 0.945, 0.996));
            messageBox.getChildren().add(textFlow);
            messageBoard.getChildren().add(messageBox);
            textArea.clear();
        }
        messageScroll.vvalueProperty().bind(messageBoard.heightProperty());
    }
    @FXML
    void onAbout(ActionEvent event) throws IOException {
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
        Collections.addAll(users, "Almas", "Bot", "Admin", "Bob");

        users.forEach((u) -> {
            HBox userBox = new HBox();
            userBox.setAlignment(Pos.CENTER_LEFT);
            userBox.setPadding(new Insets(5, 10, 5, 10));
            Text userName = new Text(u);
            TextFlow textFlow = new TextFlow(userName);
            textFlow.setStyle("-fx-color: rgb(238, 238, 238); " +
                    "-fx-background-color: rgb(28, 101, 140); ");
            textFlow.setPadding(new Insets(5,10,5,10));
            userName.setFill(Color.color(0.934, 0.945, 0.996));
            HBox.setHgrow(textFlow, Priority.ALWAYS);
            userBox.getChildren().add(textFlow);
            usersNames.getChildren().add(userBox);
        });
    }
}
