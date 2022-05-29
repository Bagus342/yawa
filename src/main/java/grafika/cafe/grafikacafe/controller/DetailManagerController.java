package grafika.cafe.grafikacafe.controller;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.models.Catatan;
import grafika.cafe.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DetailManagerController implements Initializable {
    Catatan catatan = null;

    @FXML
    public TableView<Catatan> table;

    @FXML
    public TableColumn<Catatan, String> kode;

    @FXML
    public TableColumn<Catatan, String> kode_transaksi;

    @FXML
    public TableColumn<Catatan, String> kasir;

    @FXML
    public TableColumn<Catatan, Double> total;

    @FXML
    public TableColumn<Catatan, String> tanggal;

    @FXML
    public TableColumn<Catatan, String> action;

    @FXML
    public ComboBox<String> category;

    @FXML
    public ComboBox<String> pegawai;

    @FXML
    public DatePicker date_filter;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ObservableList<Catatan> catatanList = FXCollections.observableArrayList();
    ObservableList<String> categoryList = FXCollections.observableArrayList("Harian", "Bulanan");
    ObservableList<String> userList = FXCollections.observableArrayList();

    public void getByDate() {
        catatanList.clear();
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE tanggal = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(date_filter.getValue()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                catatanList.add(new Catatan(
                        resultSet.getString("pesanan"),
                        resultSet.getString("kode_transaksi"),
                        resultSet.getString("user"),
                        resultSet.getDouble("total"),
                        resultSet.getString("tanggal")
                ));
                table.setItems(catatanList);
                table();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getByUser() {
        catatanList.clear();
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi WHERE user = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pegawai.getValue());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                catatanList.add(new Catatan(
                        resultSet.getString("pesanan"),
                        resultSet.getString("kode_transaksi"),
                        resultSet.getString("user"),
                        resultSet.getDouble("total"),
                        resultSet.getString("tanggal")
                ));
                table.setItems(catatanList);
                table();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUserList() {
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM users WHERE role = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Kasir");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(resultSet.getString("username"));
                pegawai.setItems(userList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCategoryItem() {
        category.setItems(categoryList);
    }

    public void setCategory() {
        catatanList.clear();
        connection = MysqlConnection.Connector();
        String harian = "SELECT * FROM transaksi WHERE tanggal = ?";
        String bulanan = "SELECT * from transaksi WHERE MONTH(tanggal) = ?";
        if (category.getValue().equals("Bulanan")) {
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            try {
                preparedStatement = connection.prepareStatement(bulanan);
                preparedStatement.setInt(1, month);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    catatanList.add(new Catatan(
                            resultSet.getString("pesanan"),
                            resultSet.getString("kode_transaksi"),
                            resultSet.getString("user"),
                            resultSet.getDouble("total"),
                            resultSet.getString("tanggal")
                    ));
                    table.setItems(catatanList);
                    table();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            var now = LocalDate.now();
            try {
                preparedStatement = connection.prepareStatement(harian);
                preparedStatement.setString(1, String.valueOf(now));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    catatanList.add(new Catatan(
                            resultSet.getString("pesanan"),
                            resultSet.getString("kode_transaksi"),
                            resultSet.getString("user"),
                            resultSet.getDouble("total"),
                            resultSet.getString("tanggal")
                    ));
                    table.setItems(catatanList);
                    table();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void table() {

        kode.setCellValueFactory(new PropertyValueFactory<>("code"));
        kode_transaksi.setCellValueFactory(new PropertyValueFactory<>("code_transaction"));
        kasir.setCellValueFactory(new PropertyValueFactory<>("name"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("date"));

        Callback<TableColumn<Catatan, String>, TableCell<Catatan, String>> cellFactory = (TableColumn<Catatan, String> param) -> {
            final TableCell<Catatan, String> cell = new TableCell<Catatan, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        HBox detailIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.INFO, "15px"));
                        detailIcon.setStyle("-fx-cursor: hand;");

                        detailIcon.setOnMouseClicked((MouseEvent event) -> {
                            catatan = table.getSelectionModel().getSelectedItem();
                            Preferences preferences = Preferences.userRoot();
                            preferences.put("manager", "iya");
                            preferences.put("kode_pesanan", catatan.getCode());
                            Main main = new Main();
                            main.changeScene("kasir/detail");
                        });

                        HBox hBox = new HBox(detailIcon);
                        hBox.setMargin(detailIcon, new Insets(2,2,0,3));

                        setGraphic(hBox);
                        setText(null);
                    }
                };
            };
            return cell;
        };
        action.setCellFactory(cellFactory);
        table.setItems(catatanList);

    }

    public void getCatatan() {
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM transaksi";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                catatanList.add(new Catatan(
                        resultSet.getString("pesanan"),
                        resultSet.getString("kode_transaksi"),
                        resultSet.getString("user"),
                        resultSet.getDouble("total"),
                        resultSet.getString("tanggal")
                ));
                table.setItems(catatanList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void menuScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("manager/menu");
    }

    public void pendapatanScene() {
        Main main = new Main();
        main.changeScene("manager/pendapatan");
    }

    public void logScene() {
        Main main = new Main();
        main.changeScene("manager/log");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.getCatatan();
        this.table();
        this.setCategoryItem();
        this.setUserList();
    }
}
