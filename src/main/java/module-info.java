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

    opens com.homework4 to javafx.fxml;
    exports com.homework4;
    exports com.homework4.controllers;
    opens com.homework4.controllers to javafx.fxml;
}