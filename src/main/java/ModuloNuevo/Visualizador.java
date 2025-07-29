package ModuloNuevo;

import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualizador extends Application {
    private static String rutaFXML;

    public static void mostrarLeccion(Leccion leccion) {
        for(int i =0; i < leccion.getNumeroEjercicios(); i++){
            if(leccion.getEjercicio(i) instanceof EjercicioSeleccion){
                rutaFXML = "/Modulo_Ejercicios/views/CompletarCodigo.fxml";

            }
            if(leccion.getEjercicio(i) instanceof EjercicioCompletarCodigo){
                rutaFXML = "/Modulo_Ejercicios/views/CompletarCodigo.fxml";
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(rutaFXML));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EntradaAplicativo");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

    }
}
