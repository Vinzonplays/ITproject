package com.example.itproject.Controller;

import com.example.itproject.OrderItem;
import com.example.itproject.database.OrderHistoryDAO;
import com.example.itproject.HistoryRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentController {

    @FXML private ComboBox<String> cashierComboBox;
    @FXML private RadioButton dineInRadio;
    @FXML private RadioButton takeOutRadio;
    @FXML private RadioButton cashRadio;
    @FXML private RadioButton gcashRadio;
    @FXML private TextField cashAmountField;
    @FXML private Label totalLabel;
    @FXML private Label changeLabel;
    @FXML private ListView<String> orderListView;
    @FXML private Button confirmButton;

    private double totalAmount = 0.0;
    private final ToggleGroup orderTypeGroup = new ToggleGroup();
    private final ToggleGroup paymentMethodGroup = new ToggleGroup();
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private static int receiptCounter = 1000;
    private int receiptNumber;
    private String orderType;
    private String paymentMethod;
    private String cashierName;
    private String dateTime;

    // ✅ DAO for saving history
    private final OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();

    @FXML
    public void initialize() {
        dineInRadio.setToggleGroup(orderTypeGroup);
        takeOutRadio.setToggleGroup(orderTypeGroup);
        cashRadio.setToggleGroup(paymentMethodGroup);
        gcashRadio.setToggleGroup(paymentMethodGroup);

        cashierComboBox.setItems(FXCollections.observableArrayList("Cashier 1", "Cashier 2", "Cashier 3"));
    }

    public void setOrderDetails(List<OrderItem> items, double total) {
        this.orderItems.setAll(items);
        this.totalAmount = total;
        totalLabel.setText(String.format("₱%.2f", totalAmount));

        ObservableList<String> orderDescriptions = FXCollections.observableArrayList();
        for (OrderItem item : items) {
            String line = item.getProduct().getName() + " x" + item.getQuantity() + " = ₱" +
                    String.format("%.2f", item.getProduct().getPrice() * item.getQuantity());
            orderDescriptions.add(line);
        }
        orderListView.setItems(orderDescriptions);
    }

    @FXML
    private void handleConfirmPayment() {
        if (!cashRadio.isSelected() && !gcashRadio.isSelected()) {
            showAlert("Payment Error", "Please select a payment method.");
            return;
        }

        cashierName = cashierComboBox.getValue();
        if (cashierName == null || cashierName.isBlank()) {
            showAlert("Cashier Error", "Please select a cashier.");
            return;
        }

        if (!dineInRadio.isSelected() && !takeOutRadio.isSelected()) {
            showAlert("Order Type Error", "Please select Dine In or Take Out.");
            return;
        }

        orderType = dineInRadio.isSelected() ? "Dine In" : "Take Out";
        paymentMethod = cashRadio.isSelected() ? "Cash" : "GCash";

        double cashGiven = 0.0;
        double change = 0.0;

        if (cashRadio.isSelected()) {
            try {
                cashGiven = Double.parseDouble(cashAmountField.getText());
                if (cashGiven < totalAmount) {
                    showAlert("Insufficient Cash", "The cash amount is less than the total.");
                    return;
                }
                change = cashGiven - totalAmount;
                changeLabel.setText(String.format("₱%.2f", change));
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid cash amount.");
                return;
            }
        }

        receiptNumber = receiptCounter++;
        dateTime = LocalDateTime.now().toString();

        // ✅ Show Receipt
        showReceipt(receiptNumber, cashierName, orderType, paymentMethod, cashGiven, change, dateTime);

        // ✅ Save each order item into the database
        LocalDateTime orderDateTime = LocalDateTime.parse(dateTime);
        for (OrderItem item : orderItems) {
            double admintotal = item.getQuantity() * item.getProduct().getPrice();
            HistoryRecord record = new HistoryRecord(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    orderDateTime,
                    cashierName,
                    orderType,
                    admintotal);


            orderHistoryDAO.insertHistoryRecord(record);
        }

        showAlert("Payment Successful", "Thank you for your order!");
        ((Stage) confirmButton.getScene().getWindow()).close();
    }

    private void showReceipt(int receiptNum, String cashier, String orderType, String paymentMethod,
                             double cash, double change, String dateTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/receipt.fxml"));
            Parent receiptRoot = loader.load();
            ReceiptController controller = loader.getController();

            controller.setReceiptData(receiptNum, cashier, orderType, paymentMethod, cash, change, dateTime);

            ObservableList<String> itemDescriptions = FXCollections.observableArrayList();
            for (OrderItem item : orderItems) {
                String name = item.getProduct().getName();
                int quantity = item.getQuantity();
                double price = item.getProduct().getPrice();
                double subtotal = quantity * price;
                String line = String.format("%s x%d - ₱%.2f", name, quantity, subtotal);
                itemDescriptions.add(line);
            }

            controller.setItemDescriptions(itemDescriptions);

            Stage receiptStage = new Stage();
            receiptStage.setTitle("Official Receipt");
            receiptStage.setScene(new Scene(receiptRoot));
            receiptStage.initModality(Modality.APPLICATION_MODAL);
            receiptStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) confirmButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
