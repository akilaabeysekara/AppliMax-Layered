package com.applimax.project.controller;

import com.applimax.project.dto.ItemDTO;
import com.applimax.project.dto.OrderDetailDTO;
import com.applimax.project.dto.OrderTableDTO;
import com.applimax.project.dto.tm.CartTM;
import com.applimax.project.model.CustomerModel;
import com.applimax.project.model.ItemModel;
import com.applimax.project.model.OrderDetailModel;
import com.applimax.project.model.OrderModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    public AnchorPane ancOrderPage;
    public ComboBox<String> cmbDiscount;
    public TextField txtTotalPayAmount;
    @FXML
    private  ComboBox<String> cmbPay;
    @FXML
    private TableColumn<CartTM, Button> colAction;
    @FXML
    private Label lblQtyOnHand;
    @FXML
    private TableColumn<CartTM, String> colName;
    @FXML
    private Button btnAddToCart;
    @FXML
    private ComboBox<String> cmbCustomerId;
    @FXML
    private ComboBox<String> cmbItemId;
    @FXML
    private TableColumn<CartTM, String> colId;
    @FXML
    private TableColumn<CartTM, Integer> colQty;
    @FXML
    private TableColumn<CartTM, Double> colTotalAmount;
    @FXML
    private TableColumn<CartTM, Double> colUnitPrice;
    @FXML
    private Label lblCustomerName;
    @FXML
    private Button myButton;
    @FXML
    private Label lblId;
    @FXML
    private Label lblItemName;
    @FXML
    private TableView<CartTM> tblOrder;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtTotalAmount;
    @FXML
    private Label txtUnitPrice;

    private final ObservableList<CartTM> cartData = FXCollections.observableArrayList();
    private final OrderModel orderModel = new OrderModel();
    private final OrderDetailModel orderDetailModel = new OrderDetailModel();
    private final ItemModel itemModel = new ItemModel();
    private final CustomerModel customerModel = new CustomerModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
                removeButton.setOnAction(event -> {
                    CartTM cartTM = getTableView().getItems().get(getIndex());
                    cartData.remove(cartTM);
                    tblOrder.refresh();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        cmbPay.setItems(FXCollections.observableArrayList("Cash","Card"));
        cmbDiscount.setItems(FXCollections.observableArrayList("10%","20%","30%","No Discount"));
        tblOrder.setItems(cartData);

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading data!", ButtonType.OK).show();
        }
    }

    public void resetPage() throws SQLException, ClassNotFoundException {
        lblId.setText(orderModel.getNextId());
        loadCustomerIds();
        loadItemIds();
    }

    private void loadItemIds() throws SQLException, ClassNotFoundException {
        cmbItemId.setItems(FXCollections.observableArrayList(itemModel.getAllItemIds()));
    }

    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        cmbCustomerId.setItems(FXCollections.observableArrayList(customerModel.getAllCustomerIds()));
    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedId = cmbCustomerId.getValue();
        String name = customerModel.findNameById(selectedId);
        lblCustomerName.setText(name);
    }

    @FXML
    void cmbItemOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedId = cmbItemId.getValue();
        ItemDTO itemDTO = itemModel.findById(selectedId);

        if (itemDTO != null) {
            lblItemName.setText(itemDTO.getItemName());
            lblQtyOnHand.setText(String.valueOf(itemDTO.getQtyOnHand()));
            txtUnitPrice.setText(String.valueOf(itemDTO.getItemPrice()));
        } else {
            lblItemName.setText("");
            lblQtyOnHand.setText("");
            txtUnitPrice.setText("");
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedItemId = cmbItemId.getValue();
        String cartQtyString = txtQty.getText();

        if (selectedItemId == null) {
            showAlert(Alert.AlertType.WARNING, "Please select item..!");
            return;
        }

        if (!cartQtyString.matches("^[0-9]+$")) {
            showAlert(Alert.AlertType.WARNING, "Please enter valid quantity..!");
            return;
        }

        int cartQty = Integer.parseInt(cartQtyString);
        int itemStockQty = Integer.parseInt(lblQtyOnHand.getText());

        if (itemStockQty < cartQty) {
            showAlert(Alert.AlertType.WARNING, "Not enough item quantity..!");
            return;
        }

        String itemName = lblItemName.getText();
        double itemUnitPrice = Double.parseDouble(txtUnitPrice.getText());
        double total = itemUnitPrice * cartQty;

        for (CartTM cartTM : cartData) {
            if (cartTM.getItemId().equals(selectedItemId)) {
                int newQty = cartTM.getQuantity() + cartQty;

                if (itemStockQty < newQty) {
                    showAlert(Alert.AlertType.WARNING, "Not enough item quantity..!");
                    return;
                }

                cartTM.setQuantity(newQty);
                cartTM.setTotalAmount(newQty * itemUnitPrice);
                tblOrder.refresh();
                txtQty.clear();
                return;
            }
        }

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");

        CartTM cartTM = new CartTM(
                selectedItemId,
                itemName,
                LocalDate.now(),
                cartQty,
                itemUnitPrice,
                total,
                removeBtn
        );

        removeBtn.setOnAction(action -> {
            cartData.remove(cartTM);
            tblOrder.refresh();
        });

        cartData.add(cartTM);
        clearFields();

    }

    private void clearFields() {
        txtQty.clear();
        lblItemName.setText(null);
        lblQtyOnHand.setText(null);
        txtUnitPrice.setText(null);
        lblCustomerName.setText(null);
        txtTotalAmount.setText("0.00");

    }

    @FXML
    public void setTotal(KeyEvent keyEvent) {
        String qtyText = txtQty.getText();
        String priceText = txtUnitPrice.getText();

        if (qtyText == null || qtyText.isEmpty() || priceText == null) {
            txtTotalAmount.setText("0.00");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            double price = Double.parseDouble(priceText);
            txtTotalAmount.setText(String.valueOf(qty * price));
        } catch (NumberFormatException e) {
            txtTotalAmount.setText("0.00");
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }

    public void onReset(ActionEvent actionEvent) {
        clearFields();
        tblOrder.getItems().clear();
    }


    @FXML
    void onPlaceOrder(ActionEvent event) {
        if (tblOrder.getItems().isEmpty()) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Please add items to cart..!"
            ).show();
            return;
        }

        String selectedCustomerId = cmbCustomerId.getValue();
        System.out.println("Selected Customer ID: " + selectedCustomerId);

        if (selectedCustomerId == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Please select customer for place order..!"
            ).show();
            return;
        }


        String orderId = lblId.getText();
        LocalDate date = LocalDate.now();

        ArrayList<OrderDetailDTO> cartList = new ArrayList<>();

        for (CartTM cartTM : cartData) {
            OrderDetailDTO orderDetailsDTO = new OrderDetailDTO(
                    orderId,
                    cartTM.getItemId(),
                    cartTM.getQuantity(),
                    cartTM.getUnitPrice()
            );
            cartList.add(orderDetailsDTO);
        }

        OrderTableDTO orderDTO = new OrderTableDTO(
                orderId,
                selectedCustomerId,
                date,
                cartList
        );

        try {
            if ("Card".equals(cmbPay.getValue())) {
                Parent parent =FXMLLoader.load(getClass().getResource("/view/CardPayLayout.fxml"));
                Stage stage =new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(parent));
                stage.show();
            }
            boolean isPlaced = orderModel.placeOrder(orderDTO);

            if (isPlaced) {
                new Alert(Alert.AlertType.INFORMATION, "Successfully placed the order!").show();
                clearFields();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to place order..!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to place order..!").show();
        }
    }

    public void cmbDiscountOnAction(ActionEvent actionEvent) {
        String discount = cmbDiscount.getValue();
        double price = tblOrder.getItems().stream().mapToDouble(CartTM::getTotalAmount).sum();
        if (discount != null) {
            double discountAmount = 0.0;
            switch (discount) {
                case "10%":
                    discountAmount = 0.1;
                    break;
                case "20%":
                    discountAmount = 0.2;
                    break;
                    case "30%":
                    discountAmount = 0.3;
            }

            txtTotalPayAmount.setText(String.valueOf(discountAmount * price));

        }
    }

}