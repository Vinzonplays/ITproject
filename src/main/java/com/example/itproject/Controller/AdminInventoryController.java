package com.example.itproject.Controller;

import com.example.itproject.ProductItem;
import com.example.itproject.Repositories.ProductRepository;
import com.example.itproject.database.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class AdminInventoryController {

    @FXML private ListView<ProductItem> productListView;
    @FXML private TextField productIDAdminInventory;
    @FXML private TextField productNameAdminInventory;
    @FXML private TextField priceAdminInventory;
    @FXML private ChoiceBox<String> categoryAdminInventory;
    @FXML private ChoiceBox<String> imageChoice;
    @FXML private Button reorderButton; // Add this in FXML

    private final ProductDAO productDAO = new ProductDAO();
    private final ProductRepository productRepository = new ProductRepository();

    private final ObservableList<ProductItem> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        refreshAllProductsFromRepository();

        List<ProductItem> allProducts = products;

        List<String> categories = allProducts.stream()
                .map(ProductItem::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        categoryAdminInventory.setItems(FXCollections.observableArrayList(categories));

        List<String> images = allProducts.stream()
                .map(ProductItem::getImagePath)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        imageChoice.setItems(FXCollections.observableArrayList(images));

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

    private void refreshAllProductsFromRepository() {
        products.clear();
        products.addAll(productRepository.getAllProducts()); // From static repo
        products.addAll(productDAO.getAllProducts());         // From DB
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
            showAlert("Invalid price. Please enter a valid number.");
            return;
        }

        boolean exists = products.stream().anyMatch(p -> p.getId().equals(id));
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
            showAlert("Product added successfully.");
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

    @FXML
    private void onClickReorder() {
        ProductItem selected = productListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a product to reorder.");
            return;
        }

        // Generate a new ID based on existing one
        String newId = selected.getId() + "_copy";
        if (products.stream().anyMatch(p -> p.getId().equals(newId))) {
            showAlert("Reordered product already exists. Please use a different ID.");
            return;
        }

        ProductItem reordered = new ProductItem(
                selected.getName(),
                selected.getPrice(),
                selected.getCategory(),
                selected.getImagePath(),
                newId
        );

        boolean success = productDAO.addProduct(reordered);
        if (success) {
            products.add(reordered);
            updateCategoryAndImageChoices(reordered);
            showAlert("Product reordered and added as a new item.");
        } else {
            showAlert("Failed to reorder product.");
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
