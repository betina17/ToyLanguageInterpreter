module org.example.lastlab {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.lastlab to javafx.fxml;
    exports org.example.lastlab;
}