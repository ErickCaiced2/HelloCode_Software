package GestorEjercicios.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LeccionCompletadaController {
    
    @FXML private Label lblExpTotal;
    @FXML private Label lblAciertos;
    @FXML private Button btnContinuar;
    
    @FXML
    public void initialize() {
        configurarBotones();
    }
    
    private void configurarBotones() {
        btnContinuar.setOnAction(e -> continuarSiguienteLeccion());
    }
    
    public void configurarResultados(int xp, int ejerciciosCorrectos, int totalEjercicios) {
        // Calcular porcentaje de aciertos
        double porcentaje = (ejerciciosCorrectos * 100.0) / totalEjercicios;

        // Mostrar XP recibido directamente
        lblExpTotal.setText("⚡ " + xp);

        // Mostrar porcentaje de aciertos
        lblAciertos.setText(String.format("%.0f%%", porcentaje));

        // Cambiar color según el rendimiento
        if (porcentaje >= 80) {
            lblAciertos.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 20px; -fx-font-weight: bold;");
        } else if (porcentaje >= 60) {
            lblAciertos.setStyle("-fx-text-fill: #FF9800; -fx-font-size: 20px; -fx-font-weight: bold;");
        } else {
            lblAciertos.setStyle("-fx-text-fill: #F44336; -fx-font-size: 20px; -fx-font-weight: bold;");
        }
    }
    
    private void continuarSiguienteLeccion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 720);

            Stage stage = new Stage();
            stage.setTitle("Hello Code Software - Panel Principal");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Stage thisStage = (Stage) btnContinuar.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Mostrar error con un Alert
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error cargando pantalla de inicio: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void centrarVentana(Stage stage) {
        stage.centerOnScreen();
    }
    
    private void cerrarVentanaActual() {
        Stage stage = (Stage) btnContinuar.getScene().getWindow();
        stage.close();
    }

    
} 