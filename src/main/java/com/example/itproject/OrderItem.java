package com.example.itproject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class OrderItem {
    private final ProductItem product;
    private final IntegerProperty quantity;

    public OrderItem(ProductItem product, int quantity) {
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public ProductItem getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }
}
