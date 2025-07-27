package Gamificacion_Modulo.GUI.admin;

import Gamificacion_Modulo.Main;
import Gamificacion_Modulo.ProgresoEstudiante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminMainController {
    
    @FXML
    private Button btnVolver;
    
    @FXML
    private Button btnCrearDesafioSemanal;
    
    @FXML
    private Button btnCrearDesafioMensual;
    
    @FXML
    private Button btnCrearLogro;
    
    @FXML
    private Button btnAsignarDesafio;
    
    @FXML
    private Button btnSimularActividad;
    
    @FXML
    private Button btnActualizarStats;
    
    @FXML
    private Label lblEstadisticas;
    
    @FXML
    public void initialize() {
        actualizarEstadisticas();
    }
    
    @FXML
    private void onVolverClicked(ActionEvent event) {
        System.out.println(">>> Volviendo al menú principal");
        // Cerrar ventana de administración
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void onCrearDesafioSemanalClicked(ActionEvent event) {
        System.out.println(">>> Abriendo creador de desafío semanal");
        abrirVentana("/Gamificacion_Modulo/fxml/admin/CrearDesafioSemanal.fxml", "Crear Desafío Semanal");
    }
    
    @FXML
    private void onCrearDesafioMensualClicked(ActionEvent event) {
        System.out.println(">>> Abriendo creador de desafío mensual");
        abrirVentana("/Gamificacion_Modulo/fxml/admin/CrearDesafioMensual.fxml", "Crear Desafío Mensual");
    }
    
    @FXML
    private void onCrearLogroClicked(ActionEvent event) {
        System.out.println(">>> Abriendo creador de logro");
        abrirVentana("/Gamificacion_Modulo/fxml/admin/CrearLogro.fxml", "Crear Logro Personalizado");
    }
    
    @FXML
    private void onAsignarDesafioClicked(ActionEvent event) {
        System.out.println(">>> Abriendo asignador de desafío");
        abrirVentana("/Gamificacion_Modulo/fxml/admin/AsignarDesafio.fxml", "Asignar Desafío a Usuario");
    }
    
    @FXML
    private void onSimularActividadClicked(ActionEvent event) {
        System.out.println(">>> Abriendo simulador de actividad");
        abrirVentana("/Gamificacion_Modulo/fxml/admin/SimularActividad.fxml", "Simular Actividad");
    }
    
    @FXML
    private void onActualizarStatsClicked(ActionEvent event) {
        System.out.println(">>> Actualizando estadísticas del sistema");
        actualizarEstadisticas();
    }
    
    private void actualizarEstadisticas() {
        try {
            int totalUsuarios = Main.getUsuarios().size();
            int totalLogros = Main.getLogrosDisponibles().size();
            int totalProgresos = Main.getProgresos().size();
            
            // Contar desafíos activos
            int desafiosActivos = 0;
            for (ProgresoEstudiante progreso : Main.getProgresos()) {
                desafiosActivos += progreso.getDesafiosActivos().size();
            }
            
            String estadisticas = String.format(
                "Usuarios: %d | Logros: %d | Progreso: %d | Desafíos Activos: %d",
                totalUsuarios, totalLogros, totalProgresos, desafiosActivos
            );
            
            lblEstadisticas.setText(estadisticas);
            System.out.println(">>> Estadísticas actualizadas: " + estadisticas);
            
        } catch (Exception e) {
            lblEstadisticas.setText("Error al cargar estadísticas");
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
        }
    }
    
    private void abrirVentana(String fxmlFile, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Refrescar datos si es la ventana de asignar desafío
            if (fxmlFile.contains("AsignarDesafio")) {
                Object controller = loader.getController();
                if (controller instanceof AsignarDesafioController) {
                    ((AsignarDesafioController) controller).refrescarDatos();
                }
            }
            
            Stage stage = new Stage();
            stage.setTitle(titulo);
            // Forzar dimensiones específicas para todas las ventanas admin
            Scene scene = new Scene(root, 360, 720);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            
            // Centrar la ventana en pantalla
            stage.centerOnScreen();
            
            // Actualizar estadísticas cuando se cierre la ventana
            stage.setOnHidden(e -> actualizarEstadisticas());
            
            stage.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Error al abrir ventana " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método estático para abrir la ventana de administración desde cualquier lugar
    public static void mostrarVentanaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(AdminMainController.class.getResource("/Gamificacion_Modulo/fxml/admin/AdminMain.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Panel de Administración - HelloCode");
            Scene scene = new Scene(root, 360, 720);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            
            System.out.println(">>> Ventana de administración abierta exitosamente (400x852)");
            
        } catch (Exception e) {
            System.err.println("Error al abrir ventana de administración: " + e.getMessage());
            System.err.println("Verifica que JavaFX esté configurado correctamente");
            e.printStackTrace();
        }
    }
} 