package grafika.cafe.grafikacafe.controller;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.models.Activity;
import grafika.cafe.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ActivityController implements Initializable {

    @FXML
    public TableView<Activity> table;

    @FXML
    public TableColumn<Activity, String> log;

    @FXML
    public TableColumn<Activity, String> user;

    @FXML
    public TableColumn<Activity, String> tanggal;

    ObservableList<Activity> activitiesList = FXCollections.observableArrayList();

    public void table() {

        getActivitiesItem();

        log.setCellValueFactory(new PropertyValueFactory<>("activity"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(activitiesList);
    }

    public void getActivitiesItem() {
        Connection connection = MysqlConnection.Connector();
        String query = "SELECT * FROM log";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                activitiesList.add(new Activity(
                        resultSet.getString("log"),
                        resultSet.getString("user"),
                        resultSet.getString("tanggal")
                ));
                table.setItems(activitiesList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout() throws BackingStoreException {
        Connection connection = MysqlConnection.Connector();
        var session = Session.getSession();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String format = localDateTime.format(dateTimeFormatter);
        String query = "INSERT INTO log (log, user, tanggal) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Telah Melakukan Logout");
            preparedStatement.setString(2, session.name);
            preparedStatement.setString(3, format);
            preparedStatement.execute();
            Main main = new Main();
            main.changeScene("login");
            Preferences preferences = Preferences.userRoot();
            preferences.clear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuScene() {
        Main main = new Main();
        main.changeScene("manager/menu");
    }

    public void catatanScene() {
        Main main = new Main();
        main.changeScene("manager/catatan");
    }

    public void pendapatanScene() {
        Main main = new Main();
        main.changeScene("manager/pendapatan");
    }

    public void userScene() {
        Main main = new Main();
        main.changeScene("admin/user");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.table();
    }
}
