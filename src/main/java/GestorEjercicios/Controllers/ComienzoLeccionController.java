package GestorEjercicios.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ComienzoLeccionController {
    @FXML
    private Button btnContinuar;

    private Runnable onContinuar;

    @FXML
    public void initialize() {
        btnContinuar.setOnAction(this::handleContinuar);
    }

    private void handleContinuar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/CrearLeccion.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Configurar Lección");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            centrarVentana(stage);
            stage.show();

            cerrarVentanaActual();

            System.out.println("Navegando a configuración de lección");

        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla de crear lección: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Permite setear una acción personalizada al continuar.
     */
    public void setOnContinuar(Runnable onContinuar) {
        this.onContinuar = onContinuar;
    }


private void mostrarError(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}

    private void centrarVentana(Stage stage) {
        stage.centerOnScreen();
    }

    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnContinuar.getScene().getWindow();
        currentStage.close();
    }

}