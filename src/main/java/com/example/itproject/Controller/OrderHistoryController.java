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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class OrderHistoryController {

    @FXML private TableView<HistoryRecord> adminTableview;
    @FXML private TableColumn<HistoryRecord, String> adminProductId;
    @FXML private TableColumn<HistoryRecord, String> adminProductName;
    @FXML private TableColumn<HistoryRecord, Integer> adminQuantity;
    @FXML private TableColumn<HistoryRecord, Double> adminPrice;
    @FXML private TableColumn<HistoryRecord, String> adminDateTime;
    @FXML private TableColumn<HistoryRecord, Void> adminRemove;
    @FXML private BorderPane paatPane;

    private final ObservableList<HistoryRecord> historyData = FXCollections.observableArrayList();
    private final OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();

    @FXML
    public void initialize() {
        configureColumns();
        addRemoveButtonToTable();
        loadHistoryData();
    }

    private void loadHistoryData() {
        historyData.setAll(orderHistoryDAO.getAllHistoryRecords());
        adminTableview.setItems(historyData);
    }

    private void configureColumns() {
        adminProductId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductId()));
        adminProductName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        adminQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        adminPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        adminDateTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateTime().format(formatter)));
    }

    private void addRemoveButtonToTable() {
        adminRemove.setCellFactory(new Callback<>() {
            @Override
            public TableCell<HistoryRecord, Void> call(final TableColumn<HistoryRecord, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Remove");

                    {
                        btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        btn.setOnAction(event -> {
                            HistoryRecord record = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirm Delete");
                            alert.setHeaderText("Delete this record?");
                            alert.setContentText("Are you sure you want to remove this history entry?");

                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    historyData.remove(record);
                                    orderHistoryDAO.deleteHistoryRecord(record);
                                }
                            });
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        });
    }

    @FXML
    private void onInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/AdminInventory.fxml"));
            Pane inventoryPane = loader.load();

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
            Scene scene = new Scene(root, 1280, 720);
            Stage stage = (Stage) paatPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void OnLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itproject/Dashboard.fxml"));
            Pane dashboardPane = loader.load();
            Scene scene = new Scene(dashboardPane);
            Stage stage = (Stage) paatPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
