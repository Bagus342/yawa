package grafika.cafe.grafikacafe.controller;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.models.Stock;
import grafika.cafe.grafikacafe.models.Transaksi;
import grafika.cafe.grafikacafe.session.Session;
import grafika.cafe.grafikacafe.utils.RandomCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class TransaksiController implements Initializable {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Transaksi transaksi = null;

    @FXML
    public ComboBox<String> category;

    @FXML
    public ComboBox<String> menu;

    @FXML
    public TextField quantity;

    @FXML
    public Label result;

    @FXML
    public Label user;

    @FXML
    public Label date_now;

    @FXML
    public TableView<Transaksi> table;

    @FXML
    public TableColumn<Transaksi, String> countColumn;

    @FXML
    public TableColumn<Transaksi, String> kodeColumn;

    @FXML
    public TableColumn<Transaksi, String> deleteColumn;

    @FXML
    public TableColumn<Menu, String> menuColumn;

    @FXML
    public TableColumn<Menu, Integer> quantityColumn;

    @FXML
    public TableColumn<Menu, Double> priceColumn;

    @FXML
    public TableColumn<Menu, Double> totalColumn;

    @FXML
    public Label total;

    @FXML
    public Label cashback;

    @FXML
    public TextField pay;

    @FXML
    public Label stock;

    Double priceValue = 0.0;
    Double totalValue = 0.0;
    RandomCode randomCode = new RandomCode();
    String code = randomCode.getCode();
    ObservableList<String> categoryItem = FXCollections.observableArrayList("Makanan", "Minuman");
    ObservableList<String> menuItem = FXCollections.observableArrayList();
    ObservableList<Transaksi> chartList = FXCollections.observableArrayList();
    ObservableList<Stock> stockList = FXCollections.observableArrayList();

    public void setCategoryItem() {
        category.setItems(categoryItem);
    }

    public void setCashback() {
        pay.textProperty().addListener(((observableValue, s, t1) -> {
            if (!pay.getText().isEmpty()) {
                var cashbackValue = Double.parseDouble(pay.getText()) - totalValue;
                cashback.setText("Rp " + String.valueOf(cashbackValue));
            } else {
                cashback.setText("Rp ");
            }
        }));
    }

    public void setMenuItem() {
        connection = MysqlConnection.Connector();
        String query = "SELECT nama_menu FROM menu WHERE kategori = ?";
        menuItem.clear();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getValue());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                menuItem.add(resultSet.getString("nama_menu"));
            }
            menu.setItems(menuItem);
            preparedStatement.close();
            resultSet.close();
            connection = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addChart(ActionEvent event) {
        connection = MysqlConnection.Connector();
        String query = "SELECT * FROM menu WHERE nama_menu = ?";
        String pesanan = "INSERT INTO pesanan (menu, quantity, subtotal, kode_pesanan) VALUES (?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, menu.getValue());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var quantityValue = Double.parseDouble(quantity.getText());
                var priceMenu = resultSet.getDouble("harga");
                var subTotal = priceValue * quantityValue;
                Connection connection3 = SqliteConnection.Connector();
                PreparedStatement preparedStatement3 = connection3.prepareStatement(pesanan);
                preparedStatement3.setString(1, menu.getValue());
                preparedStatement3.setInt(2, Integer.parseInt(quantity.getText()));
                preparedStatement3.setDouble(3, subTotal);
                preparedStatement3.setString(4, code);
                preparedStatement3.execute();
                preparedStatement3.close();
                preparedStatement.close();
                resultSet.close();
                priceValue = priceMenu;
//                stock(menu.getValue());
//                setStockLabel(menu.getValue());
                category.setValue(null);
                menu.setValue(null);
                quantity.setText(null);
                table();
                totalValue += subTotal;
                total.setText("Rp " + String.valueOf(totalValue));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void table() {

        refreshTable();

        countColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        kodeColumn.setCellValueFactory(new PropertyValueFactory<>("kode"));
        menuColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        Callback<TableColumn<Transaksi, String>, TableCell<Transaksi, String>> cellFactory = (TableColumn<Transaksi, String> param) -> {
            final TableCell<Transaksi, String> cell = new TableCell<Transaksi, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        HBox deleteIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.CLOSE, "15px"));
                        deleteIcon.setStyle("-fx-cursor: hand;");

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            transaksi = table.getSelectionModel().getSelectedItem();
                            connection = MysqlConnection.Connector();
                            String query = "DELETE from pesanan WHERE id = ?";
                            try {
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, transaksi.getId());
                                preparedStatement.execute();
                                preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                        HBox hbox = new HBox(deleteIcon);
                        hbox.setStyle("-fx-alignment: center;");
                        hbox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));

                        setGraphic(hbox);
                        setText(null);
                    }
                };
            };
            return cell;
        };
        deleteColumn.setCellFactory(cellFactory);
        table.setItems(chartList);
    }

//    public void setStockLabel(String menu) {
//        stockList.forEach(v -> {
//            if (v.name.equals(menu)) {
//                stock.setText("Tersedia " + String.valueOf(v.stock));
//            } else {
//                System.out.println(v.name);
//            }
//        });
//    }

//    public void stock(String menu) {
//        String query = "SELECT stok FROM menu WHERE nama_menu = ?";
//        Connection connection1 = SqliteConnection.Connector();
//        try {
//            PreparedStatement preparedStatement1 = connection1.prepareStatement(query);
//            preparedStatement1.setString(1, menu);
//            ResultSet resultSet1 = preparedStatement1.executeQuery();
//            if (resultSet1.next()) {
//                if (stockList.isEmpty()) {
//                    System.out.println("asd");
//                    stockList.add(new Stock(
//                       menu,
//                       resultSet1.getInt("stok")
//                    ));
//                } else {
//                    System.out.println("dsa");
//                    stockList.forEach(stockItem -> {
//                        if (stockItem.name.equals(menu)) {
//                            stockList.add(new Stock(
//                                    menu,
//                                    stockItem.stock -= Integer.parseInt(quantity.getText())
//                            ));
//                            stockList.remove(stockItem);
//                        } else {
//                            stockList.add(new Stock(
//                                    menu,
//                                    Integer.parseInt(quantity.getText())
//                            ));
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e.getCause());
//        }
//    }

    public void catatanScene(ActionEvent event) {
        Main main = new Main();
    }

    public void submit(ActionEvent event) {
        String transaksi = "INSERT INTO transaksi (user, pesanan, total, date, kode_transaksi) VALUES (?,?,?,?,?)";
        Connection connection1 = MysqlConnection.Connector();
        var session = Session.getSession();
        try {
            PreparedStatement preparedStatement1 = connection1.prepareStatement(transaksi);
            preparedStatement1.setString(1, session.name);
            preparedStatement1.setString(2, code);
            preparedStatement1.setDouble(3, totalValue);
            preparedStatement1.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement1.setString(5, randomCode.getCode());
            preparedStatement1.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout(ActionEvent event) throws BackingStoreException {
        Main main = new Main();
        main.changeScene("login");
        Preferences preferences = Preferences.userRoot();
        preferences.clear();
    }

    public void refreshTable() {
        Connection connection2 = MysqlConnection.Connector();
        String query = "SELECT * FROM pesanan WHERE kode_pesanan = ?";
        try {
            PreparedStatement preparedStatement2 = connection2.prepareStatement(query);
            preparedStatement2.setString(1, code);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while(resultSet2.next()){
                chartList.add(new Transaksi(
                        resultSet2.getString("id"),
                        resultSet2.getString("kode_pesanan"),
                        resultSet2.getString("menu"),
                        resultSet2.getInt("quantity"),
                        priceValue,
                        resultSet2.getDouble("subtotal")
                ));
                table.setItems(chartList);
            }
            preparedStatement2.close();
            resultSet2.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setCategoryItem();
        this.setCashback();
        var session = Session.getSession();
        var today = LocalDate.now();
        var format = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        user.setText(session.name);
        date_now.setText(format);
    }
}
