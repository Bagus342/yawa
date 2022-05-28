package grafika.cafe.grafikacafe.controller.create;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.controller.update.MenuUpdate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MenuController implements Initializable {
    Connection connection;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    boolean update = false;
    String id;

    @FXML
    public TextField name;

    @FXML
    public ComboBox<String> kategori;

    @FXML
    public TextField price;

    @FXML
    public TextField stock;

    ObservableList<String> categoryItem = FXCollections.observableArrayList("Makanan", "Minuman");

    public void setCategoryItem() {
        kategori.setItems(categoryItem);
    }

    public void submitData(ActionEvent event) {
        Main main = new Main();
        connection = MysqlConnection.Connector();
        String query = "INSERT INTO menu (name, category, price, stok) values (?,?,?,?)";
        if (name.getText().isEmpty() || kategori.getValue() == null || price.getText().isEmpty() || stock.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Kolom tidak boleh kosong !!");
            alert.showAndWait();
        } else {
            if (update) {
                var q_update = "UPDATE menu set name = ?, category = ?, price = ?, stok = ? WHERE id = ?";
                PreparedStatement preparedStatement1 = null;
                Connection connection1 = null;
                connection1 = MysqlConnection.Connector();
                try {
                    preparedStatement1 = connection1.prepareStatement(q_update);
                    preparedStatement1.setString(1, name.getText());
                    preparedStatement1.setString(2, kategori.getValue());
                    preparedStatement1.setString(3, price.getText());
                    preparedStatement1.setString(4, stock.getText());
                    preparedStatement1.setString(5, id);
                    preparedStatement1.execute();
                    preparedStatement1.close();
                    connection1.close();
                    Preferences preferences = Preferences.userRoot();
                    preferences.clear();
                    main.changeScene("manager/menu");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, name.getText());
                    preparedStatement.setString(2, kategori.getValue());
                    preparedStatement.setString(3, price.getText());
                    preparedStatement.setString(4, stock.getText());
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                    Preferences preferences = Preferences.userRoot();
                    preferences.clear();
                    clearForm();
                    main.changeScene("manager/menu");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void backScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("manager/menu");
        Preferences preferences = Preferences.userRoot();
        try {
            preferences.clear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateStatus(boolean check) {
        this.update = check;
    }

    public void clearForm() {
        name.setText(null);
        kategori.setValue(null);
        price.setText(null);
        stock.setText(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setCategoryItem();
        var menuData = MenuUpdate.getUpdateMenu();
        if (menuData.menu.equals("String")) {
            updateStatus(false);
            clearForm();
        } else {
            updateStatus(true);
            this.id = menuData.id;
            name.setText(menuData.menu);
            kategori.setValue(menuData.category);
            price.setText(String.valueOf(menuData.price));
            stock.setText(String.valueOf(menuData.stock));
        }
    }
}
