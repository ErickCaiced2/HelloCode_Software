package Comunidad_Modulo.Controladores_GUI;

import MetodosGlobales.MetodosFrecuentes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EntradaComunidad_Controller {

    @FXML
    private Button buttonGestionarComunidad;

    @FXML
    private void gestionarComunidad() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonGestionarComunidad.getScene().getWindow(),
                "/Modulo_Comunidad/Views/GestionComunidad.fxml", "GESTIONAR COMUNIDAD");
    }

    @FXML
    private Button buttonGestionarUsuarios;
    @FXML
    private void gestionarUsuarios() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonGestionarUsuarios.getScene().getWindow(),
                "/Modulo_Comunidad/Views/GestionUsuarios.fxml", "GESTIONAR USUARIOS");
    }

    @FXML
    private Button buttonGestionarForo;
    @FXML
    private void gestionarForo() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonGestionarForo.getScene().getWindow(),
                "/Modulo_Comunidad/Views/GestionForo.fxml", "GESTIONAR FORO");
    }

    @FXML
    private Button buttonGestionarChatsPrivados;
    @FXML
    private void gestionarChatsPrivados() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonGestionarChatsPrivados.getScene().getWindow(),
                "/Modulo_Comunidad/Views/GestionChatsPrivados.fxml", "GESTIONAR CHATS PRIVADOS");
    }

    @FXML
    private Button buttonSalir;
    @FXML
    private void salir() {
        Stage stage = (Stage) buttonSalir.getScene().getWindow();
        stage.close();
    }




}
