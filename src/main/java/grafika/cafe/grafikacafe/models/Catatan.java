package grafika.cafe.grafikacafe.models;

public class Catatan {
    String code;
    String code_transaction;
    String name;
    Double total;
    String date;

    public Catatan(String code, String code_transaction, String name, Double total, String date) {
        this.code = code;
        this.code_transaction = code_transaction;
        this.name = name;
        this.total = total;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode_transaction(){
        return code_transaction;
    }

    public void setCode_transaction(String code_transaction) {
        this.code_transaction = code_transaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
