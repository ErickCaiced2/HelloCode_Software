package Gamificacion_Modulo.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtils {
    public static void mostrarErrorModoConsola(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "=== MODO CONSOLA ACTIVADO ===\n" +
                "La interfaz gráfica no está disponible.\n" +
                "Cierre esta ventana y ejecute el programa desde la consola.",
                ButtonType.OK);
        alert.setTitle("Error de Modo");
        alert.setHeaderText("Modo Consola Activo");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (stage != null) {
                    stage.close();
                }
                System.exit(0);
            }
        });
    }
}
