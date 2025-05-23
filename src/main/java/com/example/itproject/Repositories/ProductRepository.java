package com.example.itproject.Repositories;

import com.example.itproject.ProductItem;
import com.example.itproject.database.ProductDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {

    private List<ProductItem> allProducts;

    public ProductRepository() {
        ProductDAO dao = new ProductDAO();
        allProducts = dao.getAllProducts();

        if (allProducts == null || allProducts.isEmpty()) {
            loadHardcodedProducts();  // fallback if DB is empty
        }
    }

    private void loadHardcodedProducts() {
        allProducts = new ArrayList<>();

        // Filipino Dishes (Main)
        allProducts.add(new ProductItem("Adobong Manok", 120.00, "Food", "Adobong Manok.jpg", "1"));
        allProducts.add(new ProductItem("Sinigang na Baboy", 150.00, "Food", "Sinigang Na Baboy.jpg", "2"));
        allProducts.add(new ProductItem("Inihaw na Liempo", 160.00, "Food", "Inihaw na liempo.jpg", "3"));
        allProducts.add(new ProductItem("Beef Steak", 180.00, "Food", "Beef Steak.jpg", "4"));
        allProducts.add(new ProductItem("Chicken Inasal", 130.00, "Food", "Chicken Inasal2.jpg", "5"));
        allProducts.add(new ProductItem("Bangus Ala Pobre", 140.00, "Food", "Pritong Bangus.jpg", "6"));
        allProducts.add(new ProductItem("Kare-Kare", 125.00, "Food", "Kare Kare.jpg", "7"));

        // Coffee
        allProducts.add(new ProductItem("Kapeng Barako", 65.00, "Coffee", "Kape Barako.jpg", "8"));
        allProducts.add(new ProductItem("Iced Coffee Barako", 85.00, "Coffee", "iced coffee float.jpg", "9"));
        allProducts.add(new ProductItem("Cafe Latte", 100.00, "Coffee", "coffee latte.jpg", "10"));
        allProducts.add(new ProductItem("Cafe Mocha", 105.00, "Coffee", "cafe mocha.jpg", "11"));
        allProducts.add(new ProductItem("Caramel Macchiato", 110.00, "Coffee", "caramel macchiato.jpg", "12"));
        allProducts.add(new ProductItem("Spanish Latte", 110.00, "Coffee", "spanish latte.jpg", "13"));
        allProducts.add(new ProductItem("Cold Brew Coffee", 95.00, "Coffee", "Cold Brew Coffee.jpg", "14"));

        // Drinks
        allProducts.add(new ProductItem("Sago't Gulaman", 40.00, "Drinks", "sago't gulaman.jpg", "15"));
        allProducts.add(new ProductItem("Calamansi Juice", 35.00, "Drinks", "Calamansi Juice.jpg", "16"));
        allProducts.add(new ProductItem("Iced Tea", 30.00, "Drinks", "iced tea.jpg", "17"));
        allProducts.add(new ProductItem("Hot Chocolate", 45.00, "Drinks", "hot chocolate.jpg", "18"));
        allProducts.add(new ProductItem("Buko Juice", 50.00, "Drinks", "buko juice.jpg", "19"));
        allProducts.add(new ProductItem("Mango Shake", 60.00, "Drinks", "mango juice.jpg", "20"));

        // Snacks
        allProducts.add(new ProductItem("Pancit Palabok", 70.00, "Snack", "pancit palabok.jpg", "21"));
        allProducts.add(new ProductItem("Pancit Canton", 65.00, "Snack", "pancit canton.jpg", "22"));
        allProducts.add(new ProductItem("Arroz Caldo", 60.00, "Snack", "arroz caldo.jpg", "23"));
        allProducts.add(new ProductItem("Empanada", 55.00, "Snack", "ilocos impanada.jpg", "24"));
        allProducts.add(new ProductItem("Puto", 25.00, "Snack", "puto.jpg", "25"));
        allProducts.add(new ProductItem("Kutsinta", 25.00, "Snack", "kutsinta.jpg", "26"));
        allProducts.add(new ProductItem("Suman sa Lihiya", 30.00, "Snack", "suman lihiya.jpg", "27"));

        // Desserts
        allProducts.add(new ProductItem("Halo-Halo Supreme", 85.00, "Dessert", "HALO-HALO-SUPREME.jpg", "28"));
        allProducts.add(new ProductItem("Leche Flan", 45.00, "Dessert", "leche flan.jpg", "29"));
        allProducts.add(new ProductItem("Bibingka", 50.00, "Dessert", "bibingka.jpg", "30"));
        allProducts.add(new ProductItem("Puto Bumbong", 50.00, "Dessert", "Puto Bumbong.jpg", "31"));
        allProducts.add(new ProductItem("Cassava Cake", 40.00, "Dessert", "Cassava Cake.jpg", "32"));
        allProducts.add(new ProductItem("Buko Pandan Salad", 60.00, "Dessert", "Buko Pandan Salad.jpg", "33"));
    }


    public List<ProductItem> getAllProducts() {
        return allProducts;
    }

    public List<ProductItem> getAllProductsByCategory(String category) {
        return allProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
