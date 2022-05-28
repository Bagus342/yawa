package grafika.cafe.grafikacafe.controller;

import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.models.Detail;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DetailController implements Initializable {

    @FXML
    public TableView<Detail> table;

    @FXML
    public TableColumn<Detail, String> kode;

    @FXML
    public TableColumn<Detail, String> menuColumn;

    @FXML
    public TableColumn<Detail, Integer> quantity;

    @FXML
    public TableColumn<Detail, String> tanggal;

    @FXML
    public TableColumn<Detail, Double> total;

    String code;

    public void table() {

    }

    public void getDetail() {
        Connection connection = MysqlConnection.Connector();
        String query = "SELECT * FROM pesanan WHERE kode_pesanan = ?";
        try {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences preferences = Preferences.userRoot();
        System.out.println(preferences.get("kode_pesanan", "String"));
        this.table();
    }
}
