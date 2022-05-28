package grafika.cafe.grafikacafe.models;

public class Transaksi {
    public String id;
    public String kode;
    public String menuName;
    public Integer quantity;
    public Double price;
    public Double subTotal;

    public Transaksi(String id, String kode, String menuName, Integer quantity, Double price, Double subTotal) {
        this.id = id;
        this.kode = kode;
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode(){
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
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
