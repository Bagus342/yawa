package grafika.cafe.grafikacafe.controller.update;

import grafika.cafe.grafikacafe.models.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class MenuUpdate {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void setUpdateMenu(Menu menu) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", menu.id);
        preferences.put("menu", menu.menu);
        preferences.put("category", menu.category);
        preferences.put("price", String.valueOf(menu.price));
        preferences.put("stock", String.valueOf(menu.stock));
    }

    public static Menu getUpdateMenu() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var name = preferences.get("menu", "String");
        var category = preferences.get("category", "String");
        var price = preferences.getDouble("price", 0);
        var stock = preferences.getInt("stock", 0);
        return new Menu(id, name, category, price, stock);
    }
}
