package grafika.cafe.grafikacafe.controller;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PendapatanController implements Initializable {

    @FXML
    public DatePicker hari;

    @FXML
    public ComboBox<String> bulan;

    @FXML
    public Label total_hari;

    @FXML
    public Label total_bulan;

    Double dayValue = 0.0;
    Double monthValue = 0.0;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<String> bulanList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    public void setBulanItems() {
        bulan.setItems(bulanList);
    }

    public void setTotalHari() {
        dayValue = 0.0;
        total_hari.setText("Rp ");
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE tanggal = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(hari.getValue()));
            resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    dayValue += resultSet.getDouble("total");
                    total_hari.setText("Rp " + String.valueOf(dayValue));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTotalBulan() throws ParseException {
        monthValue = 0.0;
        total_bulan.setText("Rp ");
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE MONTH(tanggal) = ?";
        Date date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(bulan.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, calendar.get(Calendar.MONTH) + 1);
            resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    monthValue += resultSet.getDouble("total");
                    total_bulan.setText("Rp " + String.valueOf(monthValue));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void today() {
        dayValue = 0.0;
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE tanggal = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(LocalDate.now()));
            resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    dayValue += resultSet.getDouble("total");
                    total_hari.setText("Rp " + String.valueOf(dayValue));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void monthNow() {
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE MONTH(tanggal) = ?";
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, localDate.getMonthValue());
            resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    monthValue += resultSet.getDouble("total");
                    total_bulan.setText("Rp " + String.valueOf(monthValue));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void menuScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("manager/menu");
    }

    public void catatanScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("manager/catatan");
    }

    public void logout(ActionEvent event) throws BackingStoreException {
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

    public void logScene() {
        Main main = new Main();
        main.changeScene("manager/log");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setBulanItems();
        this.today();
        this.monthNow();
    }
}
