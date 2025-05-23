package com.example.itproject.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HomeController {

    @FXML
    private ImageView homeImageView;

    @FXML
    private Button si_OrderNow;

    private final String[] imagePaths = {
            "/image/1.png",
            "/image/2.png",
            "/image/3.png",
            "/image/4.png",
            "/image/5.png",
            "/image/6.png",
            "/image/7.png",
            "/image/8.png",
            "/image/9.png",
    };

    private int currentIndex = 0;
    private Timeline imageSlider;
    private DashController dashController;

    public void setDashController(DashController dashController) {
        this.dashController = dashController;
    }

    @FXML
    private void initialize() {
        startImageSlider();
    }

    private void startImageSlider() {
        imageSlider = new Timeline(new KeyFrame(Duration.seconds(2), e -> switchImage()));
        imageSlider.setCycleCount(Timeline.INDEFINITE);
        imageSlider.play();
    }

    private void switchImage() {
        currentIndex = (currentIndex + 1) % imagePaths.length;
        homeImageView.setImage(new Image(getClass().getResourceAsStream(imagePaths[currentIndex])));
    }

    @FXML
    private void onOderNow() {
        if (dashController != null) {
            dashController.loadUI("hello-view.fxml");
        }
    }
}
