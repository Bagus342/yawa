package grafika.cafe.grafikacafe.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConnection {
    public static Connection Connector() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe_pbo", "bagus", "poallo342");
            return connection;
        } catch (Exception e) {
            System.out.println(e.getCause());
            return null;
        }
    }
}
