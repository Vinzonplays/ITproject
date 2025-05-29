package com.example.itproject.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    category TEXT NOT NULL,
                    imagePath TEXT
                );
            """;

            String createOrderHistoryTable = """
                CREATE TABLE IF NOT EXISTS order_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_id TEXT NOT NULL,
                    product_name TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    price REAL NOT NULL,
                    date_time TEXT NOT NULL,
                    cashier_name TEXT NOT NULL,
                    order_type TEXT NOT NULL
                );
            """;

            stmt.execute(createProductsTable);
            stmt.execute(createOrderHistoryTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
