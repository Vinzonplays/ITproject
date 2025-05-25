package com.example.itproject.Controller;

import com.example.itproject.ProductItem;
import com.example.itproject.Repositories.ProductRepository;
import com.example.itproject.database.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class AdminInventoryController {

    @FXML private ListView<ProductItem> productListView;
    @FXML private TextField productIDAdminInventory;
    @FXML private TextField productNameAdminInventory;
    @FXML private TextField priceAdminInventory;
    @FXML private ChoiceBox<String> categoryAdminInventory;
    @FXML private ChoiceBox<String> imageChoice;
    @FXML private Button browseImageButton;
    @FXML private ImageView productImageView;

    private final ProductDAO productDAO = new ProductDAO();
    private final ProductRepository productRepository = new ProductRepository();
    private final ObservableList<ProductItem> products = FXCollections.observableArrayList();

    private final String imageFolderPath = "images/";

    @FXML
    public void initialize() {
        refreshAllProducts();

        List<String> categories = products.stream()
                .map(ProductItem::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        categoryAdminInventory.setItems(FXCollections.observableArrayList(categories));

        List<String> imagePaths = products.stream()
                .map(ProductItem::getImagePath)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        imageChoice.setItems(FXCollections.observableArrayList(imagePaths));

        productListView.setItems(products);
        productListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProductItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getName() + " (₱" + item.getPrice() + ")");
                }
            }
        });

        imageChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                File imageFile = new File(imageFolderPath + newVal);
                if (imageFile.exists()) {
                    productImageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    productImageView.setImage(null);
                }
            }
        });
    }

    private void refreshAllProducts() {
        products.clear();
        products.addAll(productRepository.getAllProducts());
        products.addAll(productDAO.getAllProducts());
    }

    @FXML
    private void onBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                File imageDir = new File(imageFolderPath);
                if (!imageDir.exists()) imageDir.mkdirs();

                File destFile = new File(imageFolderPath + selectedFile.getName());
                if (!destFile.exists()) {
                    java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath());
                }

                String fileName = selectedFile.getName();
                if (!imageChoice.getItems().contains(fileName)) {
                    imageChoice.getItems().add(fileName);
                    FXCollections.sort(imageChoice.getItems());
                }
                imageChoice.setValue(fileName);
                productImageView.setImage(new Image(destFile.toURI().toString()));
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Failed to load image.");
            }
        }
    }


    @FXML
    private void onClickAdd() {
        String id = productIDAdminInventory.getText().trim();
        String name = productNameAdminInventory.getText().trim();
        String priceText = priceAdminInventory.getText().trim();
        String category = categoryAdminInventory.getValue();
        String image = imageChoice.getValue();

        if (id.isEmpty() || name.isEmpty() || priceText.isEmpty() || category == null || image == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Invalid price entered.");
            return;
        }

        if (products.stream().anyMatch(p -> p.getId().equals(id))) {
            showAlert("Product ID already exists.");
            return;
        }

        ProductItem newItem = new ProductItem(name, price, category, image, id);
        if (productDAO.addProduct(newItem)) {
            products.add(newItem);
            updateCategoryAndImageChoices(newItem);
            clearFields();
            showAlert("Product added successfully.");
        } else {
            showAlert("Failed to add product to the database.");
        }
    }

    @FXML
    private void onClickDelete() {
        ProductItem selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a product to delete.");
            return;
        }

        if (productDAO.deleteProduct(selected.getId())) {
            products.remove(selected);
            showAlert("Product deleted.");
        } else {
            showAlert("Failed to delete product.");
        }
    }

    @FXML
    private void onClickReorder() {
        ProductItem selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a product to reorder.");
            return;
        }

        String newId = selected.getId() + "_copy";
        if (products.stream().anyMatch(p -> p.getId().equals(newId))) {
            showAlert("Reordered product already exists.");
            return;
        }

        ProductItem reordered = new ProductItem(
                selected.getName(), selected.getPrice(), selected.getCategory(),
                selected.getImagePath(), newId
        );

        if (productDAO.addProduct(reordered)) {
            products.add(reordered);
            updateCategoryAndImageChoices(reordered);
            showAlert("Reordered and added as new product.");
        } else {
            showAlert("Failed to reorder product.");
        }
    }

    private void updateCategoryAndImageChoices(ProductItem product) {
        if (!categoryAdminInventory.getItems().contains(product.getCategory())) {
            categoryAdminInventory.getItems().add(product.getCategory());
            FXCollections.sort(categoryAdminInventory.getItems());
        }
        if (!imageChoice.getItems().contains(product.getImagePath())) {
            imageChoice.getItems().add(product.getImagePath());
            FXCollections.sort(imageChoice.getItems());
        }
    }

    private void clearFields() {
        productIDAdminInventory.clear();
        productNameAdminInventory.clear();
        priceAdminInventory.clear();
        categoryAdminInventory.getSelectionModel().clearSelection();
        imageChoice.getSelectionModel().clearSelection();
        productImageView.setImage(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin Inventory");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
