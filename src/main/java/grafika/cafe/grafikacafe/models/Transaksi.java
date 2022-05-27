package grafika.cafe.grafikacafe.models;

public class Transaksi {
    public Integer count;
    public String menuName;
    public Integer quantity;
    public Double price;
    public Double subTotal;

    public Transaksi(Integer count, String menuName, Integer quantity, Double price, Double subTotal) {
        this.count = count;
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
