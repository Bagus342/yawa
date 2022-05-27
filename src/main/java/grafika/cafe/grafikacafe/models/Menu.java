package grafika.cafe.grafikacafe.models;

public class Menu {
    public String id;
    public String menu;
    public String category;
    public double price;
    public Integer stock;

    public Menu(String id, String menu, String category, double price, Integer stock) {
        this.id = id;
        this.menu = menu;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu() {
        return this.menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
