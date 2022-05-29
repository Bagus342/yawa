package grafika.cafe.grafikacafe.controller;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.models.Detail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    boolean isManager = false;
    ObservableList<Detail> detailList = FXCollections.observableArrayList();

    public void table() {

        getDetail();

        kode.setCellValueFactory(new PropertyValueFactory<>("code"));
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menu"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("date"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        table.setItems(detailList);
    }

    public void getDetail() {
        Connection connection = MysqlConnection.Connector();
        String query = "SELECT * FROM pesanan WHERE kode_pesanan = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                detailList.add(new Detail(
                        resultSet.getString("kode_pesanan"),
                        resultSet.getString("menu"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("tanggal"),
                        resultSet.getDouble("subtotal")
                ));
                table.setItems(detailList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) {
        Main main = new Main();
        if (isManager) {
            main.changeScene("manager/catatan");
        } else {
            main.changeScene("kasir/catatan");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences preferences = Preferences.userRoot();
        var manager = preferences.get("manager", "String");
        if (manager.equals("iya")) {
            this.isManager = true;
            code = preferences.get("kode_pesanan", "String");
            this.table();
        } else {
            code = preferences.get("kode_pesanan", "String");
            this.table();
            this.isManager = false;
        }
    }
}
