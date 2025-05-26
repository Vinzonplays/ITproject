package com.example.itproject.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "1234".equals(password)) {
            errorLabel.setText("Login successful!");
            loadOrderHistory();
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    private void loadOrderHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/OrderHistory.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);  // Use 'root' instead of reloading
            Stage stage = (Stage) usernameField.getScene().getWindow();  // get current stage
            stage.setMaximized(false);
            stage.setScene(scene);
            stage.setTitle("Order History - Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load order history.");
        }
    }

}
