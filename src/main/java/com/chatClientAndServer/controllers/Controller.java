package com.chatClientAndServer.controllers;

import com.chatClientAndServer.Application;
import com.chatClientAndServer.models.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class Controller {

    @FXML
    private ListView<String> usersList;

    private String selectedRecipient;

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
    private MenuItem changeUsernameButton;

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
    private Label userName;

    @FXML
    private Button closeAboutButton;

    private Network network;

    private Application application;

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

        if (messageSend.trim().isEmpty()) return;

        if (selectedRecipient != null) {
            network.sendPrivateMessage(selectedRecipient, messageSend);
            appendMessage("To: " + selectedRecipient + "\n" + messageSend);
        } else {
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
        String[] messageArray = messageSend.split(":", 2);
        String sender = messageArray[0];
        String message = messageArray[1];
        String strDate = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        Text text = new Text("From: " + sender + "\n" + message + "\n\t" + strDate);
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
        application.openAboutDialog();
    }

    @FXML
    void initialize() {
        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });
    }

    public void updateUsersList(String[] users) {

        Arrays.sort(users);

        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(network.getUsername())) {
                users[i] = ">>> " + users[i];
            }
        }

        usersList.getItems().clear();
        Collections.addAll(usersList.getItems(), users);
    }

    public void setUsernameTitle(String username) {
        this.userName.setText(username);
    }

    @FXML
    void changeUsername() throws IOException {
        application.openChangeUsernameDialog();
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
