package Modulo_Usuario.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUIUsuario extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/Splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EntradaAplicativo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


