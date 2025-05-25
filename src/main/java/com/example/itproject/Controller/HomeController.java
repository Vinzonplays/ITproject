package com.example.itproject.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.InputStream;

public class HomeController {

    @FXML
    private ImageView homeImageView;

    @FXML
    private Button si_OrderNow;

    private final String[] imagePaths = {
            "/image/1.jpg",
            "/image/2.jpg",
            "/image/3.jpg",
            "/image/4.jpg",
            "/image/5.jpg",
            "/image/6.jpg",
            "/image/7.jpg",
            "/image/8.jpg",
            "/image/9.jpg",
            "/image/10.jpg",
            "/image/11.jpg"
    };

    private int currentIndex = 0;
    private Timeline imageSlider;
    private DashController dashController;

    public void setDashController(DashController dashController) {
        this.dashController = dashController;
    }

    @FXML
    private void initialize() {
        loadImage(imagePaths[currentIndex]); // Load the first image immediately
        startImageSlider();                 // Start the slideshow
    }

    private void startImageSlider() {
        imageSlider = new Timeline(new KeyFrame(Duration.seconds(2), e -> switchImage()));
        imageSlider.setCycleCount(Timeline.INDEFINITE);
        imageSlider.play();
    }

    private void switchImage() {
        currentIndex = (currentIndex + 1) % imagePaths.length;
        loadImage(imagePaths[currentIndex]);
    }

    private void loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.out.println("Image not found: " + path);
            return;
        }
        homeImageView.setImage(new Image(stream));
    }

    @FXML
    private void onOderNow() {
        if (dashController != null) {
            dashController.loadUI("hello-view.fxml");
        }
    }
}
