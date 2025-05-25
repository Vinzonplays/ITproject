package com.example.itproject.Repositories;

import com.example.itproject.ProductItem;
import com.example.itproject.database.ProductDAO;

import java.util.*;
import java.util.stream.Collectors;

public class ProductRepository {

    private final List<ProductItem> allProducts;

    public ProductRepository() {
        ProductDAO dao = new ProductDAO();
        List<ProductItem> dbProducts = dao.getAllProducts();
        List<ProductItem> hardcoded = loadHardcodedProducts();

        // Combine DB and hardcoded, without duplicates based on ID
        Set<String> existingIds = new HashSet<>();
        allProducts = new ArrayList<>();

        for (ProductItem p : hardcoded) {
            allProducts.add(p);
            existingIds.add(p.getId());
        }

        for (ProductItem p : dbProducts) {
            if (!existingIds.contains(p.getId())) {
                allProducts.add(p);
            }
        }
    }

    public List<ProductItem> getAllProducts() {
        return allProducts;
    }

    public List<ProductItem> getAllProductsByCategory(String category) {
        return allProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    private List<ProductItem> loadHardcodedProducts() {
        List<ProductItem> hardcoded = new ArrayList<>();

        // Filipino Dishes (Main)
        hardcoded.add(new ProductItem("Adobong Manok", 120.00, "Food", "Adobong Manok.jpg", "1"));
        hardcoded.add(new ProductItem("Sinigang na Baboy", 150.00, "Food", "Sinigang Na Baboy.jpg", "2"));
        hardcoded.add(new ProductItem("Inihaw na Liempo", 160.00, "Food", "Inihaw na liempo.jpg", "3"));
        hardcoded.add(new ProductItem("Beef Steak", 180.00, "Food", "Beef Steak.jpg", "4"));
        hardcoded.add(new ProductItem("Chicken Inasal", 130.00, "Food", "Chicken Inasal2.jpg", "5"));
        hardcoded.add(new ProductItem("Bangus Ala Pobre", 140.00, "Food", "Pritong Bangus.jpg", "6"));
        hardcoded.add(new ProductItem("Kare-Kare", 125.00, "Food", "Kare Kare.jpg", "7"));

        // Coffee
        hardcoded.add(new ProductItem("Kapeng Barako", 65.00, "Coffee", "Kape Barako.jpg", "8"));
        hardcoded.add(new ProductItem("Iced Coffee Barako", 85.00, "Coffee", "iced coffee float.jpg", "9"));
        hardcoded.add(new ProductItem("Cafe Latte", 100.00, "Coffee", "coffee latte.jpg", "10"));
        hardcoded.add(new ProductItem("Cafe Mocha", 105.00, "Coffee", "cafe mocha.jpg", "11"));
        hardcoded.add(new ProductItem("Caramel Macchiato", 110.00, "Coffee", "caramel macchiato.jpg", "12"));
        hardcoded.add(new ProductItem("Spanish Latte", 110.00, "Coffee", "spanish latte.jpg", "13"));
        hardcoded.add(new ProductItem("Cold Brew Coffee", 95.00, "Coffee", "Cold Brew Coffee.jpg", "14"));

        // Drinks
        hardcoded.add(new ProductItem("Sago't Gulaman", 40.00, "Drinks", "sago't gulaman.jpg", "15"));
        hardcoded.add(new ProductItem("Calamansi Juice", 35.00, "Drinks", "Calamansi Juice.jpg", "16"));
        hardcoded.add(new ProductItem("Iced Tea", 30.00, "Drinks", "iced tea.jpg", "17"));
        hardcoded.add(new ProductItem("Hot Chocolate", 45.00, "Drinks", "hot chocolate.jpg", "18"));
        hardcoded.add(new ProductItem("Buko Juice", 50.00, "Drinks", "buko juice.jpg", "19"));
        hardcoded.add(new ProductItem("Mango Shake", 60.00, "Drinks", "mango juice.jpg", "20"));

        // Snacks
        hardcoded.add(new ProductItem("Pancit Palabok", 70.00, "Snack", "pancit palabok.jpg", "21"));
        hardcoded.add(new ProductItem("Pancit Canton", 65.00, "Snack", "pancit canton.jpg", "22"));
        hardcoded.add(new ProductItem("Arroz Caldo", 60.00, "Snack", "arroz caldo.jpg", "23"));
        hardcoded.add(new ProductItem("Empanada", 55.00, "Snack", "ilocos impanada.jpg", "24"));
        hardcoded.add(new ProductItem("Puto", 25.00, "Snack", "puto.jpg", "25"));
        hardcoded.add(new ProductItem("Kutsinta", 25.00, "Snack", "kutsinta.jpg", "26"));
        hardcoded.add(new ProductItem("Suman sa Lihiya", 30.00, "Snack", "suman lihiya.jpg", "27"));

        // Desserts
        hardcoded.add(new ProductItem("Halo-Halo Supreme", 85.00, "Dessert", "HALO-HALO-SUPREME.jpg", "28"));
        hardcoded.add(new ProductItem("Leche Flan", 45.00, "Dessert", "leche flan.jpg", "29"));
        hardcoded.add(new ProductItem("Bibingka", 50.00, "Dessert", "bibingka.jpg", "30"));
        hardcoded.add(new ProductItem("Puto Bumbong", 50.00, "Dessert", "Puto Bumbong.jpg", "31"));
        hardcoded.add(new ProductItem("Cassava Cake", 40.00, "Dessert", "Cassava Cake.jpg", "32"));
        hardcoded.add(new ProductItem("Buko Pandan Salad", 60.00, "Dessert", "Buko Pandan Salad.jpg", "33"));

        return hardcoded;
    }

    public void syncHardcodedProductsToDatabase() {
        ProductDAO dao = new ProductDAO();
        List<ProductItem> hardcodedProducts = loadHardcodedProducts();
        List<ProductItem> existingProducts = dao.getAllProducts();

        for (ProductItem product : hardcodedProducts) {
            boolean exists = existingProducts.stream()
                    .anyMatch(p -> p.getId().equals(product.getId()));

            if (!exists) {
                boolean added = dao.addProduct(product);
                if (added) {
                    System.out.println("Added product: " + product.getName());
                } else {
                    System.out.println("Failed to add product: " + product.getName());
                }
            } else {
                System.out.println("Product already exists, skipping: " + product.getName());
            }
        }
    }
}
