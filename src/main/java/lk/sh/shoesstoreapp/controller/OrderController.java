package lk.sh.shoesstoreapp.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.sh.shoesstoreapp.dto.OrderDetailDto;
import lk.sh.shoesstoreapp.dto.OrderDto;
import lk.sh.shoesstoreapp.dto.ShoeDto;
import lk.sh.shoesstoreapp.model.OrderModel;
import lk.sh.shoesstoreapp.model.ShoeModel;
import lk.sh.shoesstoreapp.tm.BillTM;
import lk.sh.shoesstoreapp.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TableView<ItemTM> tblOrder;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtSize;

    @FXML
    private Label txtSubTotal;

    //default value null
    private List<ItemTM> itemTMS;

    private ArrayList<OrderDetailDto> orderDetailDtos;

    private double subtotal =0.0;

    @FXML
    void addToCart(ActionEvent event) {
        String brand = txtBrand.getText();
        String gender = txtGender.getText();
        int size = Integer.parseInt(txtSize.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtPrice.getText());
        double total = unitPrice * qty;

        subtotal += total;
        txtSubTotal.setText(String.valueOf(subtotal));

        int id = Integer.parseInt(txtId.getText());

        orderDetailDtos.add(new OrderDetailDto(id ,qty , total)  );

        itemTMS.add(new ItemTM(brand,gender,size,qty,unitPrice,total));
        tblOrder.setItems(FXCollections.observableArrayList(itemTMS));




    }

    

    @FXML
    void placeOrder(ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String format = dateFormat.format(date);

        try {
            boolean orderPlaced = OrderModel.placeOrder(new OrderDto(format, subtotal, orderDetailDtos));
            if (orderPlaced) {
                // Success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Status");
                alert.setHeaderText("Order Placed Successfully");
                alert.setContentText("Your order has been placed on " + format + ".");
                alert.showAndWait();
            } else {
                // Failure alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Order Status");
                alert.setHeaderText("Order Placement Failed");
                alert.setContentText("Something went wrong. Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // SQL exception alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while accessing the database.");
            alert.showAndWait();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Class not found exception alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("System Error");
            alert.setContentText("A system error occurred. Please contact support.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }



    @FXML
    void search(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());  //user input eka ganna e integer ek string ekkkt convert karagnnawa

        ShoeDto shoe = ShoeModel.searchShoeById(id); // Example ID
        if (shoe != null) {
            txtBrand.setText(shoe.getBrand());
            txtGender.setText(shoe.getGender());
            txtSize.setText(String.valueOf(shoe.getSize()));
            txtQtyOnHand.setText(String.valueOf(shoe.getStock_quantity()));
            txtPrice.setText(String.valueOf(shoe.getPrice()));
        } else {
            System.out.println("Shoe not found.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("brand"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("gender"));
        tblOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("size"));
        tblOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblOrder.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("total"));

        //super class reference variable ekt subtype object assign
        itemTMS= new ArrayList<>();

        orderDetailDtos = new ArrayList<>();
    }

    @FXML
    void bill(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/bill-view.fxml"));
            Parent root = loader.load();



            BillController billController = loader.getController();
            billController.setBillData(
                    itemTMS.stream()
                            .map(item -> new BillTM(
                                    new SimpleDateFormat("yyyy/MM/dd").format(new Date()),
                                    item.getItemid(),
                                    item.getQty(),
                                    item.getPrice(),
                                    item.getTotal()
                            ))
                            .toList(),
                    subtotal
            );
            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Order - Shoe Store");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Bill screen.");
        }
    }

    public void manage(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/manage-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Manage - Shoe Store");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Register screen.");
        }
    }

    public void display(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/load-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Load - Shoe Store");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Register screen.");
        }
    }

    public void order(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/order-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Order - Shoe Store");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Order screen.");
        }
    }


    @FXML
    void toHome(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/home-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home - Shoe Store");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Register screen.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String navigationError, String s) {
    }

    @FXML
    void graph(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/graph-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Graph - Shoe Store");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open the Register screen.");
        }
    }

}
