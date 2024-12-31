package lk.sh.shoesstoreapp.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.sh.shoesstoreapp.dto.ShoeDto;
import lk.sh.shoesstoreapp.model.ShoeModel;

import java.io.IOException;

public class ManageController {

    @FXML
    private TableView<?> tblshoe;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtId;

    @FXML
    private TextField price;

    @FXML
    private TextField txtSize;

    @FXML
    private TextField stock_qty;

    @FXML
    void add(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());
        String brand = txtBrand.getText();
        String gender = txtGender.getText();
        int size = Integer.parseInt(txtSize.getText());
        int stock = Integer.parseInt(stock_qty.getText());
        double price_qty = Double.parseDouble(price.getText());

        boolean saveShoes = ShoeModel.saveShoe(new ShoeDto(id, brand, gender, size, stock, price_qty));

        // Display an alert box based on the result
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (saveShoes) {
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Shoe details saved successfully!");
        } else {
            alert.setTitle("Failure");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save shoe details. Please try again.");
        }
        alert.showAndWait();

    }

    @FXML
    void delete(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText()); // Get user input and convert it to an integer

        boolean status = ShoeModel.deleteShoe(id);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (status) {
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Shoe deleted successfully!");
        } else {
            alert.setTitle("Failure");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete the shoe. Please try again.");
        }
        alert.showAndWait();
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0); // Terminates the program when cancel is clicked.
    }



    @FXML
    void update(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());
        String brand = txtBrand.getText();
        String gender = txtGender.getText();
        int size = Integer.parseInt(txtSize.getText());
        int stock = Integer.parseInt(stock_qty.getText());
        double price_qty = Double.parseDouble(price.getText());

        String saveShoes = ShoeModel.updateShoe(new ShoeDto(id, brand, gender, size, stock, price_qty));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (saveShoes.equals("Data Updated")) {
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Shoe details updated successfully!");
        } else {
            alert.setTitle("Failure");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update shoe details. Please try again.");
        }
        alert.showAndWait();

    }

    private void showAlert(Alert.AlertType alertType, String navigationError, String s) {

    }
    @FXML
    void showAll(ActionEvent event) {

        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/load-view.fxml"));
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
    @FXML
    void search(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());  //user input eka ganna e integer ek string ekkkt convert karagnnawa

        ShoeDto shoeDto = ShoeModel.searchShoeById(id);

        if (shoeDto != null) {
            txtBrand.setText(shoeDto.getBrand());
            txtGender.setText(shoeDto.getGender());
            txtSize.setText(String.valueOf(shoeDto.getSize()));
            stock_qty.setText(String.valueOf(shoeDto.getStock_quantity()));
            price.setText(String.valueOf(shoeDto.getPrice()));


        } else {
            // Display an alert if the shoe ID is not found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The shoe ID is not available.");
            alert.showAndWait();
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
