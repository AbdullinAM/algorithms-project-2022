module com.example.mazesolver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires javafx.swing;

    opens com.nikolai.mazesolver to javafx.fxml;
    exports com.nikolai.mazesolver;
    exports com.nikolai.mazesolver.controller;
    opens com.nikolai.mazesolver.controller to javafx.fxml;
    exports com.nikolai.mazesolver.view;
    opens com.nikolai.mazesolver.view to javafx.fxml;
}