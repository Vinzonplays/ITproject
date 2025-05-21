module com.example.itproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.itproject to javafx.fxml;
    exports com.example.itproject;
    exports com.example.itproject.Controller;
    opens com.example.itproject.Controller to javafx.fxml;
    exports com.example.itproject.Repositories;
    opens com.example.itproject.Repositories to javafx.fxml;
    exports com.example.itproject.database;
    opens com.example.itproject.database to javafx.fxml;

}