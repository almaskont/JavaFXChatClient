module gb.demo.lesson4.lesson4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires lombok;
    requires java.sql;

    opens com.chatClientAndServer to javafx.fxml;
    exports com.chatClientAndServer;
    exports com.chatClientAndServer.controllers;
    opens com.chatClientAndServer.controllers to javafx.fxml;
}