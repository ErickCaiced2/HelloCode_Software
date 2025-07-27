package Modulo_Usuario.Controladores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGeneralGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGeneralGUI.class.getResource("/Modulo_Usuario/views/Splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 720);
        stage.setTitle("Hello Code Software - Cargando...");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}