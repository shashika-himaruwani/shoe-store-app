package lk.sh.shoesstoreapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lk.sh.shoesstoreapp.db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphController implements Initializable {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button btnExportReport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupChart();
        loadChartData();
    }

    private void setupChart() {

        xAxis.setLabel("Brand");
        yAxis.setLabel("Quantity");
    }

    private void loadChartData() {
        try {
            // Create series for available and ordered stock
            XYChart.Series<String, Number> availableSeries = new XYChart.Series<>();
            availableSeries.setName("Available Stock");

            XYChart.Series<String, Number> orderedSeries = new XYChart.Series<>();
            orderedSeries.setName("Ordered Quantity");

            // Get available stock by brand
            Map<String, Integer> availableStock = getAvailableStockByBrand();
            Map<String, Integer> orderedStock = getOrderedStockByBrand();

            // Add data to series
            for (String brand : availableStock.keySet()) {
                availableSeries.getData().add(new XYChart.Data<>(brand, availableStock.get(brand)));
                orderedSeries.getData().add(new XYChart.Data<>(brand, orderedStock.getOrDefault(brand, 0)));
            }

            barChart.getData().addAll(availableSeries, orderedSeries);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Data Error", "Failed to load chart data");
        }
    }

    private Map<String, Integer> getAvailableStockByBrand() throws SQLException, ClassNotFoundException {
        Map<String, Integer> stockMap = new HashMap<>();
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "SELECT brand, SUM(stock_quantity) as total_stock FROM shoes GROUP BY brand";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stockMap.put(rs.getString("brand"), rs.getInt("total_stock"));
            }
        }
        return stockMap;
    }

    private Map<String, Integer> getOrderedStockByBrand() throws SQLException, ClassNotFoundException {
        Map<String, Integer> orderedMap = new HashMap<>();
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = """
            SELECT s.brand, SUM(od.qty) as total_ordered
            FROM order_details od
            JOIN shoes s ON od.sid = s.id
            JOIN orders o ON od.oid = o.id
            WHERE o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 1 YEAR)
            GROUP BY s.brand
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orderedMap.put(rs.getString("brand"), rs.getInt("total_ordered"));
            }
        }
        return orderedMap;
    }

//    @FXML
//    private void exportReport(ActionEvent event) {
//        // Add report generation logic here
//        try {
//            generatePDFReport();
//            showAlert(Alert.AlertType.INFORMATION, "Success", "Report generated successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate report");
//        }
//    }
//
//    private void generatePDFReport() {
//        // Implement PDF generation logic here
//        // You can use libraries like iText or Apache PDFBox
//    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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