package lk.sh.shoesstoreapp.dto;

public class OrderDetailDto {
   private int productId;
   private int qty;
   private double price;

    public OrderDetailDto(int productId, int qty, double price) {
        this.productId = productId;
        this.qty = qty;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
}
