package GestionAprendizaje_Modulo.Controladores;

import MetodosGlobales.MetodosFrecuentes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EntradaController {
    @FXML
    private Button buttonModulo;
    @FXML
    private void elegirMosulo(){
        System.out.println("Elija su modulo");
        MetodosFrecuentes.cambiarVentana((Stage)buttonModulo.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/Modulos.fxml","Modulo");
    }

}
