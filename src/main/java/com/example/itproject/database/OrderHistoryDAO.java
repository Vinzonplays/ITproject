package com.example.itproject.database;

import com.example.itproject.HistoryRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDAO {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void insertHistoryRecord(HistoryRecord record) {
        String query = "INSERT INTO order_history (product_id, product_name, quantity, price, order_datetime) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getProductName());
            stmt.setInt(3, record.getQuantity());
            stmt.setDouble(4, record.getPrice());
            stmt.setString(5, record.getDateTime().format(formatter));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoryRecord> getAllHistoryRecords() {
        List<HistoryRecord> records = new ArrayList<>();
        String query = "SELECT * FROM order_history";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                records.add(new HistoryRecord(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        LocalDateTime.parse(rs.getString("order_datetime"), formatter)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void deleteHistoryRecord(HistoryRecord record) {
        String query = "DELETE FROM order_history WHERE product_id = ? AND order_datetime = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getDateTime().format(formatter));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
