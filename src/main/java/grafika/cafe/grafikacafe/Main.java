package grafika.cafe.grafikacafe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        Parent root = FXMLLoader.load(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Grafika Cafe");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // function change to other scene
    public void changeScene(String fxml) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxml+".fxml"));
            stg.getScene().setRoot(pane);
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}