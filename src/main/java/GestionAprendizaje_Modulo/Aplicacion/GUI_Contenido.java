package GestionAprendizaje_Modulo.Aplicacion;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

import static javafx.application.Application.launch;

public class GUI_Contenido extends Application {
    @Override
    public void start(Stage stage)throws Exception{
    FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml"));
    Scene scene=new Scene(fxmlLoader.load());
    stage.setTitle("Rol");//Titulo
    stage.setScene(scene);
    stage.show();
    }
    public static void main (String[] args){
        launch(args);
    }

}