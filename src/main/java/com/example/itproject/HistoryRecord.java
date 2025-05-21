package com.example.itproject;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryRecord {
    private final StringProperty productId;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final ObjectProperty<LocalDateTime> dateTime;

    public HistoryRecord(String productId, String productName, double price, LocalDateTime dateTime) {
        this.productId = new SimpleStringProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
        this.dateTime = new SimpleObjectProperty<>(dateTime);
    }

    // JavaFX property getters for TableView binding
    public StringProperty productIdProperty() {
        return productId;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTime;
    }

    // Convenience method for formatted datetime string property
    public StringProperty formattedDateTimeProperty() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new SimpleStringProperty(dateTime.get().format(formatter));
    }

    // Standard getters (useful for DAO and elsewhere)
    public String getProductId() {
        return productId.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public double getPrice() {
        return price.get();
    }

    public LocalDateTime getDateTime() {
        return dateTime.get();
    }
}
