package grafika.cafe.grafikacafe.controller.update;

import grafika.cafe.grafikacafe.models.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class UserUpdate {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void setUpdateUser(UserData user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", user.id);
        preferences.put("username", user.username);
        preferences.put("password", user.password);
        preferences.put("name", user.name);
        preferences.put("role", user.role);
    }

    public static UserData getUpdateUser() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var username = preferences.get("username", "String");
        var password = preferences.get("password", "String");
        var name = preferences.get("name", "String");
        var role = preferences.get("role", "String");
        return new UserData(id, username, password, name, role);
    }
}
