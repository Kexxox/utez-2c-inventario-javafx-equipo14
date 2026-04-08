module com.example.tienditalocal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tienditalocal to javafx.fxml;
    exports com.example.tienditalocal;
}