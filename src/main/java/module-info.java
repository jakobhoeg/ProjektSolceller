module com.example.projektsolceller {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projektsolceller to javafx.fxml;
    exports com.example.projektsolceller;
}