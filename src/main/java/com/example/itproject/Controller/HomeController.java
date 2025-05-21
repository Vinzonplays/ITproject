package com.example.itproject.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HomeController {

    @FXML
    private ImageView homeImageView;

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

    @FXML
    public void initialize() {
        showImage(currentIndex);

        // Timeline to change image every 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            currentIndex = (currentIndex + 1) % imagePaths.length;
            showImage(currentIndex);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showImage(int index) {
        try {
            Image image = new Image(getClass().getResource(imagePaths[index]).toExternalForm());
            homeImageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + imagePaths[index]);
        }
    }
}
