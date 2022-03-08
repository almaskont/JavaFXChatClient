package com.homework4;

import com.homework4.controllers.Controller;
import com.homework4.models.Network;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();

        Network network = new Network();
        Controller controller = fxmlLoader.getController();

        controller.setNetwork(network);

        network.connect();
        network.waitMessage(controller);
    }

    public static void main(String[] args) {
        launch();
    }
}