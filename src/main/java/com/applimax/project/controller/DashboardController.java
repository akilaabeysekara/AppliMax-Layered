package com.applimax.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController {

    private static final String ACTIVE_STYLE = "-fx-background-color: #00003E; -fx-text-fill: #FFC312; -fx-background-radius: 3; -fx-border-radius: 3; -fx-border-color: #FFC312;";
    private static final String DEFAULT_STYLE = "-fx-background-color: #00003E; -fx-text-fill: #74b9ff; -fx-background-radius: 3; -fx-border-radius: 3; -fx-border-color: #00003E;";

    @FXML private Label lblDate;

    public AnchorPane ancMainContainerPlus;
    public Button btnLogout;

    @FXML
    private AnchorPane ancMainContainer;

    @FXML private Button btnCustomer;
    @FXML private Button btnDashboard;
    @FXML private Button btnEmployee;
    @FXML private Button btnExpenses;
    @FXML private Button btnInventory;
    @FXML private Button btnItems;
    @FXML private Button btnOrders;
    @FXML private Button btnReport;
    @FXML private Button btnSales;
    @FXML private Button btnSupplier;
    @FXML private Button btnUser;


    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        navigateTo("/view/CustomerPage.fxml");
        resetButtons();
        btnCustomer.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        navigateTo("/view/OpenPage.fxml");
        resetButtons();
        btnDashboard.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        navigateTo("/view/EmployeeManagementPage.fxml");
        resetButtons();
        btnEmployee.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnExpensesOnAction(ActionEvent event) {
        navigateTo("/view/ExpensesPage.fxml");
        resetButtons();
        btnExpenses.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        navigateTo("/view/InventoryPage.fxml");
        resetButtons();
        btnInventory.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnItemOnAction(ActionEvent event) {
        navigateTo("/view/ItemPage.fxml");
        resetButtons();
        btnItems.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {
        navigateTo("/view/OrderPage.fxml");
        resetButtons();
        btnOrders.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {
        navigateTo("/view/ReportPage.fxml");
        resetButtons();
        btnReport.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnSalesOnAction(ActionEvent event) {
        navigateTo("/view/SalesPage.fxml");
        resetButtons();
        btnSales.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        navigateTo("/view/SupplierPage.fxml");
        resetButtons();
        btnSupplier.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void btnUserOnAction(ActionEvent event) {
        navigateTo("/view/VerifyAdminLayout.fxml");
        resetButtons();
        btnUser.setStyle(ACTIVE_STYLE);
    }

    @FXML
    public void btnLogoutOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to Logout ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            navigateToLogout("/view/LoginPage.fxml");
        }
    }

    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());
            ancMainContainer.getChildren().add(anchorPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void navigateToLogout(String path) {
        try {
            ancMainContainerPlus.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancMainContainerPlus.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainerPlus.heightProperty());
            ancMainContainerPlus.getChildren().add(anchorPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        navigateTo("/view/OpenPage.fxml");
        resetButtons();
        btnDashboard.setStyle(ACTIVE_STYLE);
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
    }

    public void resetButtons() {
        btnDashboard.setStyle(DEFAULT_STYLE);
        btnCustomer.setStyle(DEFAULT_STYLE);
        btnItems.setStyle(DEFAULT_STYLE);
        btnOrders.setStyle(DEFAULT_STYLE);
        btnEmployee.setStyle(DEFAULT_STYLE);
        btnSupplier.setStyle(DEFAULT_STYLE);
        btnInventory.setStyle(DEFAULT_STYLE);
        btnSales.setStyle(DEFAULT_STYLE);
        btnReport.setStyle(DEFAULT_STYLE);
        btnExpenses.setStyle(DEFAULT_STYLE);
        btnUser.setStyle(DEFAULT_STYLE);
    }
}
