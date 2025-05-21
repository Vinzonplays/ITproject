package com.example.itproject.database;

import com.example.itproject.HistoryRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDAO {

    public void insertHistoryRecord(HistoryRecord record) {
        String sql = "INSERT INTO order_history(product_id, product_name, price, order_datetime) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getProductName());
            stmt.setDouble(3, record.getPrice());
            stmt.setString(4, record.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoryRecord> getAllHistoryRecords() {
        List<HistoryRecord> history = new ArrayList<>();
        String sql = "SELECT * FROM order_history ORDER BY order_datetime DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                history.add(new HistoryRecord(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        LocalDateTime.parse(rs.getString("order_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    public void deleteHistoryRecord(HistoryRecord record) {
        String sql = "DELETE FROM order_history WHERE product_id = ? AND order_datetime = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getProductId());
            stmt.setString(2, record.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
