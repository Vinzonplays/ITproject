package com.example.itproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ContactController {

    @FXML
    private ImageView phoneImageView;

    @FXML
    private ImageView pinImageView;

    @FXML
    private ImageView emailImageView;

    @FXML
    public void initialize() {
        // Load images from the resources/image/ directory
        try {
            emailImageView.setImage(new Image(getClass().getResource("/image/email.png").toExternalForm()));
            phoneImageView.setImage(new Image(getClass().getResource("/image/phone.png").toExternalForm()));
            pinImageView.setImage(new Image(getClass().getResource("/image/pin.png").toExternalForm()));
        } catch (Exception e) {
            e.printStackTrace(); // Log if image loading fails
        }
    }
}
