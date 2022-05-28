package grafika.cafe.grafikacafe.models;

public class Detail {
    public String code;
    public String menu;
    public Integer quantity;
    public String date;
    public Double total;

    public Detail(String code, String menu, Integer quantity, String date, Double total) {
        this.code = code;
        this.menu = menu;
        this.quantity = quantity;
        this.date = date;
        this.total = total;
    }
}
