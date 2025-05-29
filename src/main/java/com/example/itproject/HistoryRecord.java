package com.example.itproject;

import java.time.LocalDateTime;

public class HistoryRecord {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private LocalDateTime dateTime;
    private String cashierName;
    private String orderType;

    public HistoryRecord(String productId, String productName, int quantity, double price,
                         LocalDateTime dateTime, String cashierName, String orderType) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = dateTime;
        this.cashierName = cashierName;
        this.orderType = orderType;
    }

    // ✅ Add getters here
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getCashierName() { return cashierName; }
    public String getOrderType() { return orderType; }
}
