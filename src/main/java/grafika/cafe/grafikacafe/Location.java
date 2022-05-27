package grafika.cafe.grafikacafe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Location {
    public Parent getLocation(String fxml) throws IOException {
        return FXMLLoader.load(getClass().getResource(fxml));
    }
}
