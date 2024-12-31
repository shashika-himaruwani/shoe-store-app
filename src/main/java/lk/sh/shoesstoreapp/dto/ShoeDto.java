package lk.sh.shoesstoreapp.dto;



public class ShoeDto {

    private int id;
    private String brand;
    private String gender;
    private int size;
    private int stock_quantity;
    private double price;

    public ShoeDto(int id, String brand, String gender, int size, int stock_quantity, double price) {
        this.id = id;
        this.brand = brand;
        this.gender = gender;
        this.size = size;
        this.stock_quantity = stock_quantity;
        this.price = price;
    }

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
}
