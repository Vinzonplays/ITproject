package com.example.itproject;

import java.time.LocalDateTime;

public class HistoryRecord {
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double price;
    private final LocalDateTime dateTime;

    public HistoryRecord(String productId, String productName, int quantity, double price, LocalDateTime dateTime) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
