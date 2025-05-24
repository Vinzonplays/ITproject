package com.example.itproject.Controller;

import com.example.itproject.ProductItem;
import com.example.itproject.database.ProductDAO;
import com.example.itproject.Repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class AdminInventoryController {

    @FXML private ListView<ProductItem> productListView;
    @FXML private TextField productIDAdminInventory;
    @FXML private TextField productNameAdminInventory;
    @FXML private TextField priceAdminInventory;
    @FXML private ChoiceBox<String> categoryAdminInventory;
    @FXML private ChoiceBox<String> imageChoice;

    private final ProductDAO productDAO = new ProductDAO();
    private final ProductRepository productRepository = new ProductRepository();

    private final ObservableList<ProductItem> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load categories dynamically from the existing products in DB or repo
        List<String> categories = productRepository.getAllProducts()
                .stream()
                .map(ProductItem::getCategory)
                .distinct()
                .sorted()
                .toList();

        categoryAdminInventory.setItems(FXCollections.observableArrayList(categories));

        // You can also load images dynamically from existing products
        List<String> images = productRepository.getAllProducts()
                .stream()
                .map(ProductItem::getImagePath)
                .distinct()
                .sorted()
                .toList();

        imageChoice.setItems(FXCollections.observableArrayList(images));

        // Load products from database
        refreshProductsFromDB();

        // Setup ListView cell formatting
        productListView.setItems(products);
        productListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProductItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getId() + " - " + item.getName() +
                                " (" + item.getCategory() + ") - ₱" + item.getPrice());
            }
        });
    }

    private void refreshProductsFromDB() {
        products.clear();
        products.addAll(productDAO.getAllProducts());
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
            showAlert("Invalid price. Please enter a number.");
            return;
        }

        // Check if product ID already exists in DB
        boolean exists = productDAO.getAllProducts()
                .stream()
                .anyMatch(p -> p.getId().equals(id));
        if (exists) {
            showAlert("Product ID already exists.");
            return;
        }

        ProductItem newItem = new ProductItem(name, price, category, image, id);

        boolean success = productDAO.addProduct(newItem);
        if (success) {
            products.add(newItem);
            updateCategoryAndImageChoices(newItem);
            clearFields();
        } else {
            showAlert("Failed to add product to database.");
        }
    }

    @FXML
    private void onClickDelete() {
        ProductItem selected = productListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = productDAO.deleteProduct(selected.getId());
            if (success) {
                products.remove(selected);
                showAlert("Product deleted successfully.");
            } else {
                showAlert("Failed to delete product from database.");
            }
        } else {
            showAlert("Please select a product to delete.");
        }
    }

    private void clearFields() {
        productIDAdminInventory.clear();
        productNameAdminInventory.clear();
        priceAdminInventory.clear();
        categoryAdminInventory.getSelectionModel().clearSelection();
        imageChoice.getSelectionModel().clearSelection();
    }

    private void updateCategoryAndImageChoices(ProductItem newProduct) {
        if (!categoryAdminInventory.getItems().contains(newProduct.getCategory())) {
            categoryAdminInventory.getItems().add(newProduct.getCategory());
            FXCollections.sort(categoryAdminInventory.getItems());
        }
        if (!imageChoice.getItems().contains(newProduct.getImagePath())) {
            imageChoice.getItems().add(newProduct.getImagePath());
            FXCollections.sort(imageChoice.getItems());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin Inventory");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
