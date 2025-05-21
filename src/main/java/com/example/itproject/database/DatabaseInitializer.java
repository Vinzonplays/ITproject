package com.example.itproject.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            // Product Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    price REAL NOT NULL,
                    category TEXT NOT NULL,
                    imagePath TEXT
                );
            """);

            // Order History Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_history (
                    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_id TEXT,
                    product_name TEXT,
                    price REAL,
                    order_datetime TEXT
                );
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
