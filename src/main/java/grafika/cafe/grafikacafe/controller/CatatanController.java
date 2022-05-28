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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class CatatanController implements Initializable {
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

    ObservableList<Catatan> catatanList = FXCollections.observableArrayList();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void table() {

        getCatatan();

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
        var session = Session.getSession();
        String query = "SELECT * FROM transaksi WHERE user = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, session.name);
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
            System.out.println(e.getCause());
        }
    }

    public void transaksiScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("kasir/transaksi");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table();
    }
}
