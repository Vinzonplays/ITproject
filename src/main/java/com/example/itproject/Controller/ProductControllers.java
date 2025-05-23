package com.example.itproject.Controller;

import com.example.itproject.ProductItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductControllers {

    @FXML private ImageView imageView;
    @FXML private Label nameAdd;
    @FXML private Spinner<Integer> onSpinner;
    @FXML private Button onAdd;
    @FXML private Label amountAdd;
    @FXML private Label priceLabel; // Optional, if used in FXML

    private ProductItem product;

    // Initialize method is called after @FXML fields are injected
    @FXML
    public void initialize() {
        // Set fixed size for imageView to ensure same size for all images
        imageView.setFitWidth(200);  // width in pixels
        imageView.setFitHeight(150); // height in pixels
        imageView.setPreserveRatio(true); // keep aspect ratio without distortion

        // Initialize spinner value factory here if desired
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        onSpinner.setValueFactory(valueFactory);
    }

    public void setData(ProductItem productItem) {
        this.product = productItem;
        nameAdd.setText(productItem.getName());
        loadProductImage(productItem.getImagePath());

        // Always show the original price only
        amountAdd.setText(String.format("₱%.2f", productItem.getPrice()));

        // Spinner is already initialized in initialize() method
    }

    private void loadProductImage(String imagePath) {
        var resource = getClass().getResourceAsStream("/com/example/itproject/images/" + imagePath);
        if (resource != null) {
            // Load image with no extra resizing because imageView handles it
            Image image = new Image(resource);
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
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
        // Add to order logic here if needed
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
