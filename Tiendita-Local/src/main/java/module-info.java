module com.example.tienditalocal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;

    exports com.example.tienditalocal to javafx.graphics;
    exports com.example.tienditalocal.controllers;
    exports com.example.tienditalocal.services;
    exports com.example.tienditalocal.models;
    exports com.example.tienditalocal.repositories;

    opens com.example.tienditalocal to
            javafx.fxml;
    opens com.example.tienditalocal.controllers to
            javafx.fxml;
    opens com.example.tienditalocal.models to
            javafx.base;
}