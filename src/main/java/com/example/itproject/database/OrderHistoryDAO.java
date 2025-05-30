package com.example.itproject.database;

import com.example.itproject.HistoryRecord;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDAO {

    public void insertHistoryRecord(HistoryRecord record) {
        String sql = "INSERT INTO order_history (product_id, product_name, quantity, price, date_time, cashier_name, order_type, admintotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getProductName());
            stmt.setInt(3, record.getQuantity());
            stmt.setDouble(4, record.getPrice());
            stmt.setString(5, record.getDateTime().toString());
            stmt.setString(6, record.getCashierName());
            stmt.setString(7, record.getOrderType());
            stmt.setDouble(8, record.getAdmintotal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoryRecord> getAllHistoryRecords() {
        List<HistoryRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM order_history";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String dateTimeStr = rs.getString("date_time");
                LocalDateTime dateTime;

                try {
                    if (dateTimeStr == null || dateTimeStr.isEmpty()) {
                        dateTime = LocalDateTime.now();
                    } else if (dateTimeStr.contains("T")) {
                        dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } else {
                        dateTime = Timestamp.valueOf(dateTimeStr).toLocalDateTime();
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse date_time: " + dateTimeStr);
                    dateTime = LocalDateTime.now();
                }

                HistoryRecord record = new HistoryRecord(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        dateTime,
                        rs.getString("cashier_name"),
                        rs.getString("order_type"),
                        rs.getDouble("admintotal")
                );

                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    public void deleteHistoryRecord(HistoryRecord record) {
        String sql = "DELETE FROM order_history WHERE product_id = ? AND date_time = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getDateTime().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
