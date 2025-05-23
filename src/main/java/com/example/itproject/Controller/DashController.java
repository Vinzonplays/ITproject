package com.example.itproject.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashController {

    @FXML
    private BorderPane hootPane;

    @FXML
    private Button si_Home;

    @FXML
    private Button si_Menu;

    @FXML
    private Button si_Special;

    @FXML
    private Button si_BtnLogin;

    @FXML
    private TextField si_Search;

    @FXML
    public void initialize() {
        loadUI("Home.fxml");
    }

    @FXML
    private void onHome() {
        loadUI("Home.fxml");
    }

    @FXML
    private void onMenu() {
        loadUI("hello-view.fxml");
    }

    @FXML
    private void onLogin() {
        loadUI("AdminLogin.fxml");
    }

    @FXML
    private void onContact() {
        loadUI("Contact.fxml");
    }

    @FXML
    private void Login() {
        System.out.println("Login button clicked. Implement login logic here.");
    }

    public void loadUI(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/" + fxmlFile));
            Node node = loader.load();

            // Inject this DashController into the HomeController
            Object controller = loader.getController();
            if (controller instanceof HomeController) {
                ((HomeController) controller).setDashController(this);
            }

            hootPane.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load: " + fxmlFile);
        }
    }
}
