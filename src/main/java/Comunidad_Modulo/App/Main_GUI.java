package Comunidad_Modulo.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main_GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Comunidad/Views/Comunidad.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Comunidad de Aprendizaje");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
