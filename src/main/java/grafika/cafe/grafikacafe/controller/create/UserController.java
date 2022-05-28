package grafika.cafe.grafikacafe.controller.create;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.controller.update.UserUpdate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class UserController implements Initializable {
    Connection connection;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    boolean update;
    String id;

    @FXML
    public TextField username;

    @FXML
    public TextField name;

    @FXML
    public PasswordField password;

    @FXML
    public ComboBox<String> role = new ComboBox<>();

    ObservableList<String> roleItems = FXCollections.observableArrayList("Admin", "Manager", "Kasir");

    public void setRoleItems() {
        role.setItems(roleItems);
    }

    public void submitData(ActionEvent event) {
        Main main = new Main();
        connection = MysqlConnection.Connector();
        String query = "INSERT INTO users (username, password, name, role) values (?,?,?,?)";
        if (name.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty() || role.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Kolom tidak boleh kosong !!");
            alert.showAndWait();
        } else {
            if (update) {
                var q_update = "UPDATE users set username = ?, password = ?, name = ?, role = ? WHERE id = ?";
                PreparedStatement preparedStatement1 = null;
                Connection connection1 = null;
                connection1 = MysqlConnection.Connector();
                try {
                    preparedStatement1 = connection1.prepareStatement(q_update);
                    preparedStatement1.setString(1, username.getText());
                    preparedStatement1.setString(2, password.getText());
                    preparedStatement1.setString(3, name.getText());
                    preparedStatement1.setString(4, role.getValue());
                    preparedStatement1.setString(5, id);
                    preparedStatement1.execute();
                    preparedStatement1.close();
                    connection1.close();
                    Preferences preferences = Preferences.userRoot();
                    preferences.clear();
                    main.changeScene("admin/user");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username.getText());
                    preparedStatement.setString(2, password.getText());
                    preparedStatement.setString(3, name.getText());
                    preparedStatement.setString(4, role.getValue());
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                    Preferences preferences = Preferences.userRoot();
                    preferences.clear();
                    main.changeScene("admin/user");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void backScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("admin/user");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setRoleItems();
        var data = UserUpdate.getUpdateUser();
        if (data.username.equals("String")) {
            this.updateStatus(false);
            name.setText(null);
            username.setText(null);
            password.setText(null);
            name.setText(null);
            role.setValue(null);
        } else {
            this.id = null;
            this.updateStatus(true);
            this.id = data.id;
            name.setText(data.name);
            username.setText(data.username);
            password.setText(data.password);
            role.setValue(data.role);
        }
    }
}
