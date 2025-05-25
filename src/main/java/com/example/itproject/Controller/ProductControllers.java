package com.example.itproject.Controller;

import com.example.itproject.ProductItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.InputStream;

public class ProductControllers {

    @FXML private ImageView imageView;
    @FXML private Label nameAdd;
    @FXML private Spinner<Integer> onSpinner;
    @FXML private Button onAdd;
    @FXML private Label amountAdd;

    private ProductItem product;

    @FXML
    public void initialize() {
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        onSpinner.setValueFactory(valueFactory);
    }

    public void setData(ProductItem productItem) {
        this.product = productItem;
        nameAdd.setText(productItem.getName());
        amountAdd.setText(String.format("₱%.2f", productItem.getPrice()));
        loadProductImage(productItem.getImagePath());
    }

    private void loadProductImage(String imagePath) {
        // First try loading from resources
        InputStream resource = getClass().getResourceAsStream("/com/example/itproject/images/" + imagePath);
        if (resource != null) {
            imageView.setImage(new Image(resource));
        } else {
            // Try loading from file system (useful if image added via file chooser)
            File file = new File("images/" + imagePath);
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            } else {
                imageView.setImage(null);
                System.err.println("Image not found: " + imagePath);
            }
        }
    }

    public Button getAddButton() {
        return onAdd;
    }

    public int getQuantity() {
        return onSpinner.getValue();
    }

    @FXML
    private void handleAddButtonAction() {
        int quantity = getQuantity();
        double total = product.getPrice() * quantity;
        System.out.println("Adding to order: " + product.getName() + " x" + quantity + " = ₱" + total);
        // Add to cart/order logic goes here
    }

    @FXML
    private void onMouseEntered() {
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0, 0, 0);");
    }

    @FXML
    private void onMouseExited() {
        imageView.setStyle("-fx-effect: none;");
    }
}
