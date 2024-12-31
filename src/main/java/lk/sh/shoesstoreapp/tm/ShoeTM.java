package lk.sh.shoesstoreapp.tm;

import javafx.scene.control.Button;

public class ShoeTM {
    private int id;
    private String brand;
    private String gender;
    private int size;
    private int stock_quantity;
    private double price;

    private Button deleteButton;

    public ShoeTM(int id, String brand, String gender, int size, int stock_quantity, double price) {
        this.id = id;
        this.brand = brand;
        this.gender = gender;
        this.size = size;
        this.stock_quantity = stock_quantity;
        this.price = price;

        this.deleteButton = new Button("Delete");
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
