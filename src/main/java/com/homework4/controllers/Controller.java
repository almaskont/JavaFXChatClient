package com.homework4.controllers;

import com.homework4.Application;
import com.homework4.models.Network;
import com.homework4.server.authentication.AuthenticationService;
import com.homework4.server.authentication.BaseAuthenticationService;
import com.homework4.server.handler.ClientHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        if (messageSend.trim().isEmpty()) return;

        if (selectedRecipient != null) {
            network.sendPrivateMessage(selectedRecipient, messageSend);
        } else {
            network.sendMessage(messageSend);
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

//        aboutButton.setOnAction((event) -> {
//            FXMLLoader root;
//            try {
//                root = FXMLLoader.load(Application.class.getResource("aboutPage.fxml"));
//                Stage stage = new Stage();
//                Scene about = new Scene(root.load());
//                stage.initModality(Modality.WINDOW_MODAL);
//                stage.setTitle("About");
//                stage.setScene(about);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        closeAboutButton.setOnAction( (event) -> {
//            stage = (Stage) closeAboutButton.getScene().getWindow();
//            if (event.getTarget() == closeAboutButton) {
//                stage.close();
//            }
//        });
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

}
