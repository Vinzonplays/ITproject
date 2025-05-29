package com.example.itproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptController {

    @FXML private Label receiptNumberLabel;
    @FXML private Label orderNumberLabel;
    @FXML private Label cashierNameLabel;
    @FXML private Label orderTypeLabel;
    @FXML private ListView<String> itemsListView;
    @FXML private Label totalLabel;
    @FXML private Label cashLabel;
    @FXML private Label changeLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label dateTimeLabel;

    private static int orderNumberCounter = 2000;  // Optional counter for Order #

    public void setReceiptData(int receiptNumber, String cashier, String orderType, String paymentMethod,
                               double cash, double change, String dateTime) {
        receiptNumberLabel.setText(String.format("#%04d", receiptNumber));
        orderNumberLabel.setText(String.format("#%04d", orderNumberCounter++));
        cashierNameLabel.setText(cashier);
        orderTypeLabel.setText(orderType);
        paymentMethodLabel.setText(paymentMethod);

        totalLabel.setText(String.format("₱%.2f", cash - change));
        cashLabel.setText(String.format("₱%.2f", cash));
        changeLabel.setText(String.format("₱%.2f", change));

        // Format the date
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        dateTimeLabel.setText("Date: " + parsedDateTime.format(formatter));
    }

    public void setItemDescriptions(List<String> itemDescriptions) {
        itemsListView.getItems().setAll(itemDescriptions);
    }
}
