package grafika.cafe.grafikacafe.controller;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.controller.update.MenuUpdate;
import grafika.cafe.grafikacafe.models.Menu;
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

public class MenuController implements Initializable {
    Menu menuData = null;

    @FXML
    public TableView<Menu> table;

    @FXML
    public TableColumn<String, Menu> id;

    @FXML
    public TableColumn<String, Menu> name;

    @FXML
    public TableColumn<String, Menu> category;

    @FXML
    public TableColumn<Double, Menu> price;

    @FXML
    public TableColumn<Integer, Menu> stock;

    @FXML
    public TableColumn<Menu, String> action;

    ObservableList<Menu> dataList = FXCollections.observableArrayList();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Connection connection = null;

    public void table() {
        getMenuData();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("menu"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        Callback<TableColumn<Menu, String>, TableCell<Menu, String>> cellFactory = (TableColumn<Menu, String> param) -> {
            final TableCell<Menu, String> cell = new TableCell<Menu, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        HBox updateIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.PENCIL_SQUARE, "30px"));
                        HBox deleteIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.TRASH, "30px"));

                        updateIcon.setStyle(
                                "-fx-cursor: hand;"
                        );

                        deleteIcon.setStyle(
                                "-fx-cursor: hand;"
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                menuData = table.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM menu WHERE id = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, menuData.getId());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                getMenuData();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            menuData = table.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                MenuUpdate menuUpdate = new MenuUpdate();
                                Menu data = new Menu(menuData.getId(), menuData.getMenu(), menuData.getCategory(), menuData.getPrice(), menuData.getStock());
                                menuUpdate.setUpdateMenu(data);
                                main.changeScene("manager/create/menuForm");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                        HBox hBox = new HBox(updateIcon, deleteIcon);
                        hBox.setStyle("-fx-alignment: center");
                        HBox.setMargin(updateIcon,new Insets(2, 2, 0 , 3));
                        HBox.setMargin(deleteIcon,new Insets(2, 3, 0 , 5));

                        setGraphic(hBox);

                        setText(null);
                    }
                };

            };
            return cell;
        };
        action.setCellFactory(cellFactory);
        table.setItems(dataList);
    }

    public void getMenuData() {
        dataList.clear();
        try {
          String query = "SELECT * FROM menu";
          connection = SqliteConnection.Connector();
          preparedStatement = connection.prepareStatement(query);
          resultSet = preparedStatement.executeQuery();
          while (resultSet.next()) {
              dataList.add(new Menu(
                      resultSet.getString("id"),
                      resultSet.getString("nama_menu"),
                      resultSet.getString("kategori"),
                      resultSet.getDouble("harga"),
                      resultSet.getInt("stok")
              ));
              table.setItems(dataList);
          }
          preparedStatement.close();
          resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void createScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("manager/create/menuForm");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.table();
    }
}