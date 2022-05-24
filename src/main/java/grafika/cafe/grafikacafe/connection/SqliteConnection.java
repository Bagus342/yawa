package grafika.cafe.grafikacafe.connection;

import java.sql.*;

public class SqliteConnection {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:cafe.db");
            return connection;
        } catch (Exception e) {
            return null;
        }
    }
}
