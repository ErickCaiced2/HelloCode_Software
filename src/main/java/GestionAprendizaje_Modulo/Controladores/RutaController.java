package GestionAprendizaje_Modulo.Controladores;

import MetodosGlobales.MetodosFrecuentes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RutaController {
    @FXML
    private Button btnBack;

    @FXML
    protected void clickBtnLvl1() {
    }

    @FXML
    protected void clickBtnLvl2() {
    }

    @FXML
    protected void clickBtnLvl3() {
    }

    @FXML
    protected void clickBtnLvl4() {
    }

    @FXML
    protected void clickBtnLesson() {
    }

    @FXML
    protected void clickBtnBack(ActionEvent event) {
        MetodosFrecuentes.cambiarVentana((Stage) btnBack.getScene().getWindow(), "/com/example/myappfxapp/menu-view.fxml", "Menu Modulos" );
    }
}