package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_novo/Menu.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        scene.getStylesheets().add(getClass().getResource("/css/Menu.css").toExternalForm());

        stage.setTitle("Sistema da Clínica");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
