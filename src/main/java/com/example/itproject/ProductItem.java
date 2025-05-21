package com.example.itproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductItem {
    private final StringProperty id;
    private final StringProperty name;
    private final double price;
    private final StringProperty category;
    private final StringProperty imagePath;

    public ProductItem(String name, double price, String category, String imagePath, String id) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = price;
        this.category = new SimpleStringProperty(category);
        this.imagePath = new SimpleStringProperty(imagePath);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category.get();
    }

    public String getImagePath() {
        return imagePath.get();
    }
}
