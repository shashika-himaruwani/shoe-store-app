package lk.sh.shoesstoreapp.tm;

public class ItemTM {
    private int itemid;
    private String brand;
    private String gender;
    private int size;
    private int qty;
    private double price;
    private double total;

    public ItemTM(String brand, String gender, int size, int qty, double price, double total) {

        this.brand = brand;
        this.gender = gender;
        this.size = size;
        this.qty = qty;
        this.price = price;
        this.total = total;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }
}
