package Gamificacion_Modulo.GUI.admin;

import java.util.ArrayList;
import java.util.List;

import Gamificacion_Modulo.DesafioMensual;
import Gamificacion_Modulo.Logro;
import Gamificacion_Modulo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CrearDesafioMensualController {
    
    @FXML
    private Slider sliderObjetivo;
    
    @FXML
    private Label lblObjetivo;
    
    @FXML
    private VBox vboxLogros;
    
    @FXML
    private Label lblLogrosSeleccionados;
    
    @FXML
    private VBox vboxVistaPrevia;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private Button btnCrear;
    
    private List<CheckBox> checkBoxesLogros = new ArrayList<>();
    private List<Logro> logrosSeleccionados = new ArrayList<>();
    
    @FXML
    public void initialize() {
        configurarSlider();
        cargarLogrosDisponibles();
        actualizarVistaPrevia();
        
        // Listener para actualización en tiempo real del slider
        sliderObjetivo.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblObjetivo.setText(String.valueOf(newVal.intValue()));
            actualizarVistaPrevia();
        });
    }
    
    private void configurarSlider() {
        sliderObjetivo.setValue(20); // Valor por defecto
        lblObjetivo.setText("20");
    }
    
    private void cargarLogrosDisponibles() {
        try {
            vboxLogros.getChildren().clear();
            checkBoxesLogros.clear();
            
            List<Logro> logrosDisponibles = Main.getLogrosDisponibles();
            
            for (Logro logro : logrosDisponibles) {
                CheckBox checkBox = new CheckBox();
                checkBox.setText(logro.getNombre() + " - " + logro.getDescripcion() + " (+" + logro.getPuntos() + " pts)");
                checkBox.setUserData(logro);
                checkBox.setWrapText(true);
                
                // Listener para actualizar lista de logros seleccionados
                checkBox.setOnAction(e -> {
                    actualizarLogrosSeleccionados();
                    actualizarVistaPrevia();
                });
                
                checkBoxesLogros.add(checkBox);
                vboxLogros.getChildren().add(checkBox);
            }
            
            System.out.println(">>> Logros disponibles cargados: " + logrosDisponibles.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar logros: " + e.getMessage());
        }
    }
    
    private void actualizarLogrosSeleccionados() {
        logrosSeleccionados.clear();
        
        for (CheckBox checkBox : checkBoxesLogros) {
            if (checkBox.isSelected()) {
                Logro logro = (Logro) checkBox.getUserData();
                logrosSeleccionados.add(logro);
            }
        }
        
        lblLogrosSeleccionados.setText(logrosSeleccionados.size() + " logros seleccionados");
    }
    
    private void actualizarVistaPrevia() {
        vboxVistaPrevia.getChildren().clear();
        
        int objetivo = (int) sliderObjetivo.getValue();
        
        Label lblNombre = new Label("📋 Desafío Mensual");
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label lblDescripcion = new Label("📝 Descripción: Completar " + objetivo + " actividades en el mes");
        Label lblObjetivoInfo = new Label("🎯 Objetivo: " + objetivo + " actividades mensuales");
        Label lblLogros = new Label("🏆 Logros asociados: " + logrosSeleccionados.size());
        Label lblEstado = new Label("📊 Estado: Disponible para asignar");
        
        vboxVistaPrevia.getChildren().addAll(lblNombre, lblDescripcion, lblObjetivoInfo, lblLogros, lblEstado);
        
        // Mostrar logros seleccionados
        for (Logro logro : logrosSeleccionados) {
            Label lblLogro = new Label("   • " + logro.getNombre() + " (+" + logro.getPuntos() + " pts)");
            lblLogro.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");
            vboxVistaPrevia.getChildren().add(lblLogro);
        }
        
        if (logrosSeleccionados.isEmpty()) {
            Label lblSinLogros = new Label("   (Sin logros asociados)");
            lblSinLogros.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12px;");
            vboxVistaPrevia.getChildren().add(lblSinLogros);
        }
    }
    
    @FXML
    private void onCancelarClicked(ActionEvent event) {
        System.out.println(">>> Cancelando creación de desafío mensual");
        cerrarVentana();
    }
    
    @FXML
    private void onCrearClicked(ActionEvent event) {
        try {
            // Validaciones básicas
            int objetivo = (int) sliderObjetivo.getValue();
            
            if (objetivo < 10 || objetivo > 100) {
                mostrarAlerta("Error", "El objetivo debe estar entre 10 y 100 actividades");
                return;
            }
            
            if (logrosSeleccionados.isEmpty()) {
                boolean continuar = mostrarConfirmacion("Advertencia", 
                    "¿Estás seguro de crear un desafío sin logros asociados?");
                if (!continuar) {
                    return;
                }
            }
            
            // Crear el desafío mensual
            DesafioMensual desafio = new DesafioMensual(objetivo, new ArrayList<>(logrosSeleccionados));
            
            // Agregar a la lista central de desafíos
            Main.agregarDesafio(desafio);
            
            // Mensaje de éxito
            String mensaje = String.format(
                "¡Desafío mensual creado exitosamente!\n\n" +
                "Objetivo: %d actividades mensuales\n" +
                "Logros asociados: %d\n" +
                "Estado: Disponible para asignar\n\n" +
                "Usa 'Asignar Desafío a Usuario' para asignarlo.",
                objetivo,
                logrosSeleccionados.size()
            );
            
            mostrarAlerta("Éxito", mensaje);
            
            System.out.println(">>> DESAFÍO MENSUAL CREADO EXITOSAMENTE");
            System.out.println("ID del desafío: " + desafio.getId());
            System.out.println("Objetivo: " + objetivo + " actividades mensuales");
            System.out.println("Descripción: " + desafio.getDescripcion());
            System.out.println("Logros asociados (" + logrosSeleccionados.size() + "):");
            for (Logro logro : logrosSeleccionados) {
                System.out.println("   * " + logro.getNombre() + " (+" + logro.getPuntos() + " pts)");
            }
            System.out.println("Estado: Disponible para asignar a usuarios");
            
            cerrarVentana();
            
        } catch (Exception e) {
            System.err.println("Error al crear desafío mensual: " + e.getMessage());
            mostrarAlerta("Error", "Error al crear el desafío: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(titulo.equals("Error") ? Alert.AlertType.ERROR : 
                               titulo.equals("Advertencia") ? Alert.AlertType.WARNING : 
                               Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
} 