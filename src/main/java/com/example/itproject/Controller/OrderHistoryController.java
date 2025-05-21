package com.example.itproject.Controller;

import com.example.itproject.HistoryRecord;
import com.example.itproject.database.OrderHistoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class OrderHistoryController {

    @FXML
    private TableView<HistoryRecord> adminTableview;

    @FXML
    private TableColumn<HistoryRecord, String> adminProductId;

    @FXML
    private TableColumn<HistoryRecord, String> adminProductName;

    @FXML
    private TableColumn<HistoryRecord, Double> adminPrice;

    @FXML
    private TableColumn<HistoryRecord, String> adminDateTime;

    @FXML
    private TableColumn<HistoryRecord, Void> adminRemove;

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
        adminProductId.setCellValueFactory(data -> data.getValue().productIdProperty());
        adminProductName.setCellValueFactory(data -> data.getValue().productNameProperty());
        adminPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        adminDateTime.setCellValueFactory(data -> data.getValue().formattedDateTimeProperty());
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
                            historyData.remove(record);
                            orderHistoryDAO.deleteHistoryRecord(record); // delete in DB
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
}
