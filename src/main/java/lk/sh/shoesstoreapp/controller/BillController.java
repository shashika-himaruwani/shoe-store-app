package lk.sh.shoesstoreapp.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.sh.shoesstoreapp.tm.BillTM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BillController {

    @FXML
    private VBox billContainer;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblBillNo;

    @FXML
    private Label lblSubTotal;

    private List<BillTM> billItems;
    private double grandTotal;
    private String billNumber;

    public void initialize() {
        // Generate unique bill number
        billNumber = "BILL-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        lblBillNo.setText(billNumber);
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }


    public void setBillData(List<BillTM> billData, double total) {
        this.billItems = billData;
        this.grandTotal = total;
        lblSubTotal.setText(String.format("%.2f", total));
        displayBillDetails();
    }

    private void displayBillDetails() {
        // Clear existing content
        billContainer.getChildren().clear();

        // Add shop header
        Label shopName = new Label("SHOE STORE\n\n*********************************************");
        shopName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        billContainer.getChildren().add(shopName);

        // Add address and contact
        Label address = new Label("123 Shoe Street, Fashion District\n\nPhone: +94 11 1234567\n\n***********************************************");
        address.setStyle("-fx-font-size: 14px;");
        billContainer.getChildren().add(address);

        // Add bill details
        for (BillTM item : billItems) {
            Label itemLabel = new Label(String.format(
                    "Item ID: %d   Quantity: %d   Price: %.2f   Subtotal: %.2f",
                    item.getItemId(), item.getQuantity(), item.getPrice(), item.getSubtotal()
            ));
            itemLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
            billContainer.getChildren().add(itemLabel);
        }

        // Add total
        Label totalLabel = new Label(String.format("Grand Total: Rs. %.2f\n\n*********************************************", grandTotal));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");
        billContainer.getChildren().add(totalLabel);

        // Add thank you message
        Label thankYou = new Label("Thank you for shopping with us!\n\nPlease visit again!\n*********************************************\n\n\n*********************************************\n\n");
        thankYou.setStyle("-fx-font-size: 16px; -fx-font-style: italic; -fx-padding: 10px;");
        billContainer.getChildren().add(thankYou);
    }

    @FXML
    void downloadPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Bill PDF");
        fileChooser.setInitialFileName(billNumber + ".pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            try {
                generatePDF(file);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Bill PDF has been downloaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate PDF: " + e.getMessage());
            }
        }
    }

    private void generatePDF(File file) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));

        document.open();

        // Add shop header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph header = new Paragraph("SHOE STORE\n", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Add address and contact
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
        Paragraph address = new Paragraph(
                "123 Shoe Street, Fashion District\nPhone: +94 11 1234567\n\n",
                normalFont
        );
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Add bill details
        Paragraph billDetails = new Paragraph();
        billDetails.add(new Chunk("Bill Number: " + billNumber + "\n"));
        billDetails.add(new Chunk("Date: " + lblDate.getText() + "\n\n"));
        document.add(billDetails);

        // Create table for items
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // Add table headers
        String[] headers = {"Item ID", "Quantity", "Price", "Subtotal"};
        for (String headerText : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(headerText, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Add items to table
        for (BillTM item : billItems) {
            table.addCell(String.valueOf(item.getItemId()));
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.format("%.2f", item.getPrice()));
            table.addCell(String.format("%.2f", item.getSubtotal()));
        }

        document.add(table);

        // Add total
        Paragraph total = new Paragraph(
                String.format("\nGrand Total: Rs. %.2f\n\n", grandTotal),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)
        );
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        // Add thank you message
        Paragraph thankYou = new Paragraph(
                "Thank you for shopping with us!\n\nPlease visit again!\n\n*********************************************\n\n*********************************************\n\n*********************************************",
                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)
        );
        thankYou.setAlignment(Element.ALIGN_CENTER);
        document.add(thankYou);

        document.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Existing navigation methods remain the same
    @FXML void goBack(ActionEvent event) {

        try {
            // Load the Register screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/sh/shoesstoreapp/order-view.fxml"));
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

}