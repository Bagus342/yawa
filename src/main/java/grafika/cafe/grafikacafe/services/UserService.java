package grafika.cafe.grafikacafe.services;

import grafika.cafe.grafikacafe.Main;
import grafika.cafe.grafikacafe.connection.SqliteConnection;
import grafika.cafe.grafikacafe.session.Session;
import grafika.cafe.grafikacafe.session.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public Label failureMessage;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void login(ActionEvent event) {
        Main main = new Main();
        try {
            String user = this.authenticate(username.getText(), password.getText());
            if (user.equals("not exist")) {
                failureMessage.setText("Anda tidak terdaftar !");
            } else if (user.equals("password incorrect")) {
                failureMessage.setText("Password yang anda masukkan salah !");
            } else {
                var session = this.session(username.getText());
                if (session.role.equals("Admin")) {
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    main.changeScene("admin/user");
                } else if (session.role.equals("Manager")){
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    main.changeScene("manager/menu");
                } else {
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    main.changeScene("kasir/transaksi");
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String authenticate(String username, String password) {
        connection = SqliteConnection.Connector();
        String result_username = "SELECT * FROM users WHERE username = ?";
        String result_password = "SELECT * FROM users WHERE password = ?";
        try {
            preparedStatement = connection.prepareStatement(result_username);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                PreparedStatement preparedStatement1;
                ResultSet resultSet1;
                preparedStatement1 = connection.prepareStatement(result_password);
                preparedStatement1.setString(1, password);
                resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    return "Success";
                } else {
                    return "password incorrect";
                }
            } else {
                return "not exist";
            }
        } catch (Exception e) {
            String err = e.getMessage();
            System.out.println(e.getMessage());
            return err;
        }
    }

    public User session(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var user = resultSet.getString("name");
                var role = resultSet.getString("role");
                preparedStatement.close();
                resultSet.close();
                connection.close();
                return new User(user, role);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
