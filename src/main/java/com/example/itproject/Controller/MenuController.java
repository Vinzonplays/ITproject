package com.example.itproject.Controller;

import com.example.itproject.OrderItem;
import com.example.itproject.ProductItem;
import com.example.itproject.Repositories.ProductRepository;
import com.example.itproject.database.OrderHistoryDAO;
import com.example.itproject.HistoryRecord;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MenuController {

    @FXML private FlowPane raatPane;
    @FXML private TextField si_Search;
    @FXML private Button si_Food, si_Drinks, si_Coffee, si_Snack, si_Dessert;  // fixed Dessert spelling
    @FXML private Button si_Clear, si_Checkout;
    @FXML private Label onTotal, orderStatusLabel;
    @FXML private TableView<OrderItem> onTableview;
    @FXML private TableColumn<OrderItem, String> foodItemColumn;
    @FXML private TableColumn<OrderItem, Integer> foodQuantityColumn;
    @FXML private TableColumn<OrderItem, Double> foodTotalColumn;
    @FXML private TableColumn<OrderItem, Void> foodRemoveColumn;

    private double totalAmount = 0.0;
    private final ProductRepository productRepository = new ProductRepository();
    private final OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
    private String currentCategory = "Food";

    @FXML
    private void initialize() {
        si_Search.textProperty().addListener((obs, oldVal, newVal) -> filterProducts(newVal));

        si_Clear.setOnAction(e -> clearOrder());
        si_Checkout.setOnAction(e -> checkout());

        onTableview.setEditable(true);

        foodItemColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProduct().getName()));

        foodQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        foodQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        foodQuantityColumn.setOnEditCommit(event -> {
            OrderItem item = event.getRowValue();
            int newQty = event.getNewValue();
            if (newQty <= 0) {
                onTableview.getItems().remove(item);
            } else {
                item.setQuantity(newQty);
            }
            updateTotal();
        });

        foodTotalColumn.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getProduct().getPrice() * cellData.getValue().getQuantity();
            return new javafx.beans.property.SimpleDoubleProperty(total).asObject();
        });

        foodRemoveColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(e -> {
                    OrderItem item = getTableRow().getItem();
                    if (item != null) {
                        onTableview.getItems().remove(item);
                        updateTotal();
                        orderStatusLabel.setText("Item removed from order.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        displayProducts(currentCategory);
    }

    // === Navigation Handlers ===
    @FXML private void onFood() { currentCategory = "Food"; displayProducts(currentCategory); }
    @FXML private void onDrinks() { currentCategory = "Drinks"; displayProducts(currentCategory); }
    @FXML private void onCoffee() { currentCategory = "Coffee"; displayProducts(currentCategory); }
    @FXML private void onSnack() { currentCategory = "Snack"; displayProducts(currentCategory); }
    @FXML private void onDessert() { currentCategory = "Dessert"; displayProducts(currentCategory); }

    private void displayProducts(String category) {
        List<ProductItem> products = productRepository.getAllProductsByCategory(category);
        raatPane.getChildren().clear();

        for (ProductItem product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/Product.fxml"));
                Node productNode = loader.load();
                ProductControllers controller = loader.getController();
                controller.setData(product);
                controller.getAddButton().setOnAction(e -> addToOrder(product, controller.getQuantity()));
                raatPane.getChildren().add(productNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterProducts(String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            displayProducts(currentCategory);
            return;
        }

        List<ProductItem> filteredProducts = productRepository.getAllProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());

        raatPane.getChildren().clear();

        if (filteredProducts.isEmpty()) {
            raatPane.getChildren().add(new Label("No products found"));
        } else {
            for (ProductItem product : filteredProducts) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/Product.fxml"));
                    Node productNode = loader.load();
                    ProductControllers controller = loader.getController();
                    controller.setData(product);
                    controller.getAddButton().setOnAction(e -> addToOrder(product, controller.getQuantity()));
                    raatPane.getChildren().add(productNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addToOrder(ProductItem product, int quantity) {
        if (quantity <= 0) {
            orderStatusLabel.setText("Quantity must be greater than zero.");
            return;
        }

        for (OrderItem item : onTableview.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                updateTotal();
                orderStatusLabel.setText("Updated quantity for " + product.getName());
                return;
            }
        }
        onTableview.getItems().add(new OrderItem(product, quantity));
        updateTotal();
        orderStatusLabel.setText(product.getName() + " added to order.");
    }

    private void updateTotal() {
        totalAmount = onTableview.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        onTotal.setText(String.format("Total:₱%.2f", totalAmount));
    }

    private void clearOrder() {
        onTableview.getItems().clear();
        updateTotal();
        orderStatusLabel.setText("Order cleared.");
    }

    private void checkout() {
        if (onTableview.getItems().isEmpty()) {
            orderStatusLabel.setText("No items to checkout.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        for (OrderItem item : onTableview.getItems()) {
            HistoryRecord record = new HistoryRecord(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    now
            );
            orderHistoryDAO.insertHistoryRecord(record);
        }

        // Show receipt dialog
        Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
        receiptAlert.setTitle("Order Receipt");
        receiptAlert.setHeaderText("Your Order Receipt");
        TextArea receiptTextArea = new TextArea(generateReceipt());
        receiptTextArea.setEditable(false);
        receiptTextArea.setWrapText(true);
        receiptTextArea.setPrefWidth(400);
        receiptTextArea.setPrefHeight(300);
        receiptAlert.getDialogPane().setContent(receiptTextArea);
        receiptAlert.showAndWait();

        clearOrder();
        orderStatusLabel.setText("Order placed successfully!");
    }

    private String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("===== Receipt =====\n");
        receipt.append("Date: ").append(LocalDateTime.now()).append("\n\n");

        receipt.append(String.format("%-20s %5s %10s %10s\n", "Item", "Qty", "Price", "Total"));
        receipt.append("--------------------------------------------------\n");

        for (OrderItem item : onTableview.getItems()) {
            String name = item.getProduct().getName();
            int qty = item.getQuantity();
            double price = item.getProduct().getPrice();
            double total = price * qty;
            receipt.append(String.format("%-20s %5d %10.2f %10.2f\n", name, qty, price, total));
        }
        receipt.append("--------------------------------------------------\n");
        receipt.append(String.format("TOTAL: ₱%.2f\n", totalAmount));
        receipt.append("===================\n");
        receipt.append("Thank you for your order!");

        return receipt.toString();
    }
}
