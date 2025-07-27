package GestionAprendizaje_Modulo.Controladores;

import MetodosGlobales.MetodosFrecuentes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModuloController {
    @FXML
    private Button buttonAtras;
    @FXML
    private void volverContenido(){
        System.out.println("Regreso a contenido");
        MetodosFrecuentes.cambiarVentana((Stage) buttonAtras.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/Contenido.fxml","Contenido");
    }
}
