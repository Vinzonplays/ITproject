package com.example.itproject.Controller;

import com.example.itproject.HistoryRecord;
import com.example.itproject.database.OrderHistoryDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderHistoryController {

    @FXML private TableView<HistoryRecord> adminTableview;
    @FXML private TableColumn<HistoryRecord, String> adminProductId;
    @FXML private TableColumn<HistoryRecord, String> adminProductName;
    @FXML private TableColumn<HistoryRecord, Integer> adminQuantity;
    @FXML private TableColumn<HistoryRecord, Double> adminPrice;
    @FXML private TableColumn<HistoryRecord, String> adminDateTime;
    @FXML private TableColumn<HistoryRecord, String> adminCashier;
    @FXML private TableColumn<HistoryRecord, String> adminDineINTakeOut;
    @FXML private TableColumn<HistoryRecord, Double> adminTotal;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label totalSalesLabel;
    @FXML private BorderPane paatPane;
    @FXML private DatePicker si_Datepicker; // ADD this

    private final ObservableList<HistoryRecord> historyData = FXCollections.observableArrayList();
    private final OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();

    @FXML
    public void initialize() {
        configureColumns();
        loadHistoryData();

        filterComboBox.getItems().addAll("Weekly", "Monthly", "Yearly");
        filterComboBox.setValue("Weekly");
        filterComboBox.setOnAction(e -> handleFilterSelection());

        si_Datepicker.setOnAction(e -> onDatePicker()); // Set action for DatePicker

        handleFilterSelection(); // Apply default filter
    }

    private void configureColumns() {
        adminProductId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductId()));
        adminProductName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        adminQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        adminPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        adminCashier.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCashierName()));
        adminDineINTakeOut.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderType()));
        adminTotal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAdmintotal()).asObject());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        adminDateTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateTime().format(formatter)));
    }

    private void loadHistoryData() {
        historyData.setAll(orderHistoryDAO.getAllHistoryRecords());
        adminTableview.setItems(historyData);
    }

    @FXML
    private void handleFilterSelection() {
        String selectedFilter = filterComboBox.getValue();

        if (selectedFilter != null) {
            List<HistoryRecord> filtered = historyData.stream()
                    .filter(record -> {
                        LocalDateTime date = record.getDateTime();
                        switch (selectedFilter) {
                            case "Weekly":
                                return date.isAfter(LocalDateTime.now().minusWeeks(1));
                            case "Monthly":
                                return date.isAfter(LocalDateTime.now().minusMonths(1));
                            case "Yearly":
                                return date.isAfter(LocalDateTime.now().minusYears(1));
                            default:
                                return true;
                        }
                    })
                    .collect(Collectors.toList());

            adminTableview.setItems(FXCollections.observableArrayList(filtered));

            double total = filtered.stream().mapToDouble(HistoryRecord::getAdmintotal).sum();
            totalSalesLabel.setText("₱" + String.format("%.2f", total));
        }
    }

    @FXML
    private void onDatePicker() {
        LocalDate selectedDate = si_Datepicker.getValue();
        if (selectedDate != null) {
            List<HistoryRecord> filteredByDate = historyData.stream()
                    .filter(record -> record.getDateTime().toLocalDate().isEqual(selectedDate))
                    .collect(Collectors.toList());

            adminTableview.setItems(FXCollections.observableArrayList(filteredByDate));

            double total = filteredByDate.stream().mapToDouble(HistoryRecord::getAdmintotal).sum();
            totalSalesLabel.setText("₱" + String.format("%.2f", total));
        }
    }

    @FXML
    private void onInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/AdminInventory.fxml"));
            Parent inventoryPane = loader.load();
            paatPane.setCenter(inventoryPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/OrderHistory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) paatPane.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OnLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/Dashboard.fxml"));
            Parent dashboardPane = loader.load();
            Stage stage = (Stage) paatPane.getScene().getWindow();
            stage.setScene(new Scene(dashboardPane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
