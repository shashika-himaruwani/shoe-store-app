package lk.sh.shoesstoreapp.controller;

import com.itextpdf.text.Font;
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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.sh.shoesstoreapp.db.DBConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnExportPDF;

    @FXML
    private Button btnRefresh;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupChart();
        setupDatePickers();
        setupButtons();
    }

    private void setupChart() {
        xAxis.setLabel("Brand");
        yAxis.setLabel("Quantity");
        barChart.setTitle("");
    }

    private void setupDatePickers() {
        // Set default values
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Add listeners for date changes
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> refreshChart());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> refreshChart());
    }

    private void setupButtons() {
        btnRefresh.setOnAction(e -> refreshChart());
        btnExportExcel.setOnAction(this::exportToExcel);
        btnExportPDF.setOnAction(this::exportToPDF);
    }

    private void refreshChart() {
        barChart.getData().clear();
        loadChartData();
    }

    private void loadChartData() {
        try {
            XYChart.Series<String, Number> availableSeries = new XYChart.Series<>();
            availableSeries.setName("Available Stock");

            XYChart.Series<String, Number> orderedSeries = new XYChart.Series<>();
            orderedSeries.setName("Ordered Quantity");

            Map<String, Integer> availableStock = getAvailableStockByBrand();
            Map<String, Integer> orderedStock = getOrderedStockByBrand();

            for (String brand : availableStock.keySet()) {
                availableSeries.getData().add(new XYChart.Data<>(brand, availableStock.get(brand)));
                orderedSeries.getData().add(new XYChart.Data<>(brand, orderedStock.getOrDefault(brand, 0)));
            }

            barChart.getData().addAll(availableSeries, orderedSeries);

        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Data Error", "Failed to load chart data");
            e.printStackTrace();
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
            WHERE o.order_date BETWEEN ? AND ?
            GROUP BY s.brand
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDatePicker.getValue()));
            stmt.setDate(2, java.sql.Date.valueOf(endDatePicker.getValue()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orderedMap.put(rs.getString("brand"), rs.getInt("total_ordered"));
            }
        }
        return orderedMap;
    }

    @FXML
    private void exportToExcel(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            File file = fileChooser.showSaveDialog(barChart.getScene().getWindow());

            if (file != null) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Stock Report");

                // Create headers
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Brand");
                headerRow.createCell(1).setCellValue("Available Stock");
                headerRow.createCell(2).setCellValue("Ordered Quantity");

                // Add data
                Map<String, Integer> availableStock = getAvailableStockByBrand();
                Map<String, Integer> orderedStock = getOrderedStockByBrand();

                int rowNum = 1;
                for (String brand : availableStock.keySet()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(brand);
                    row.createCell(1).setCellValue(availableStock.get(brand));
                    row.createCell(2).setCellValue(orderedStock.getOrDefault(brand, 0));
                }

                // Write to file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Excel file exported successfully!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export Error", "Failed to export Excel file");
            e.printStackTrace();
        }
    }

    @FXML
    private void exportToPDF(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File file = fileChooser.showSaveDialog(barChart.getScene().getWindow());

            if (file != null) {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("Stock Report", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                // Create table
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);

                // Add headers
                table.addCell("Brand");
                table.addCell("Available Stock");
                table.addCell("Ordered Quantity");

                // Add data
                Map<String, Integer> availableStock = getAvailableStockByBrand();
                Map<String, Integer> orderedStock = getOrderedStockByBrand();

                for (String brand : availableStock.keySet()) {
                    table.addCell(brand);
                    table.addCell(String.valueOf(availableStock.get(brand)));
                    table.addCell(String.valueOf(orderedStock.getOrDefault(brand, 0)));
                }

                document.add(table);
                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "PDF file exported successfully!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export Error", "Failed to export PDF file");
            e.printStackTrace();
        }
    }

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
    public void logout(ActionEvent event) {
        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/login-view.fxml"));
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