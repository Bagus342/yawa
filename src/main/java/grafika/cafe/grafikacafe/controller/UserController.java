package grafika.cafe.grafikacafe.controller;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.MysqlConnection;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.controller.update.UserUpdate;
import grafika.cafe.grafikacafe.models.UserData;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserController implements Initializable {
    UserData userData = null;

    @FXML
    public TableView<UserData> table;

    @FXML
    public TableColumn<UserData, String> id;

    @FXML
    public TableColumn<UserData, String> name;

    @FXML
    public TableColumn<UserData, String> user_name;

    @FXML
    public TableColumn<UserData, String> pass_word;

    @FXML
    public TableColumn<UserData, String> role;

    @FXML
    public TableColumn<UserData, String> action;


    ObservableList<UserData> dataList = FXCollections.observableArrayList();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void table() {

        getUser();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        user_name.setCellValueFactory(new PropertyValueFactory<>("username"));
        pass_word.setCellValueFactory(new PropertyValueFactory<>("password"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        Callback<TableColumn<UserData, String>, TableCell<UserData, String>> cellFactory = (TableColumn<UserData, String> param) -> {
            final TableCell<UserData, String> cell = new TableCell<UserData, String>() {
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
                                userData = table.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM users WHERE id = ?";
                                var connection = MysqlConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, userData.getId());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                getUser();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });

                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            userData = table.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                UserUpdate userUpdate = new UserUpdate();
                                UserData data = new UserData(userData.getId(), userData.getUsername(), userData.getPassword(), userData.getName(), userData.getRole());
                                userUpdate.setUpdateUser(data);
                                main.changeScene("admin/create/user");
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

    public void getUser() {
        try {
            dataList.clear();

            var query = "SELECT * FROM users";
            var connection = MysqlConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new UserData(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("role")
                ));
                table.setItems(dataList);
            }
            preparedStatement.close();
            resultSet.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createScene(ActionEvent event) {
        Main main = new Main();
        main.changeScene("admin/create/user");
    }

    public void logout(ActionEvent event) throws BackingStoreException {
        Main main = new Main();
        main.changeScene("login");
        Preferences preferences = Preferences.userRoot();
        preferences.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.table();
    }
}