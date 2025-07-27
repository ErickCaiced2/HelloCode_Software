package Gamificacion_Modulo.GUI.admin;

import Gamificacion_Modulo.Logro;
import Gamificacion_Modulo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CrearLogroController {
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private RadioButton rbDesafios;
    
    @FXML
    private RadioButton rbPuntos;
    
    @FXML
    private RadioButton rbLogros;
    
    @FXML
    private Spinner<Integer> spinnerDesafios;
    
    @FXML
    private Spinner<Integer> spinnerPuntos;
    
    @FXML
    private Spinner<Integer> spinnerLogros;
    
    @FXML
    private Slider sliderPuntos;
    
    @FXML
    private Label lblPuntos;
    
    @FXML
    private VBox vboxVistaPrevia;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private Button btnCrear;
    
    private ToggleGroup criterioGroup;
    
    @FXML
    public void initialize() {
        configurarToggleGroup();
        configurarSpinners();
        configurarSlider();
        configurarListeners();
        actualizarVistaPrevia();
    }
    
    private void configurarToggleGroup() {
        criterioGroup = new ToggleGroup();
        rbDesafios.setToggleGroup(criterioGroup);
        rbPuntos.setToggleGroup(criterioGroup);
        rbLogros.setToggleGroup(criterioGroup);
        
        // Seleccionar desafíos por defecto
        rbDesafios.setSelected(true);
        spinnerDesafios.setDisable(false);
        
        // Configurar listeners para radio buttons
        rbDesafios.setOnAction(e -> {
            spinnerDesafios.setDisable(false);
            spinnerPuntos.setDisable(true);
            spinnerLogros.setDisable(true);
            actualizarVistaPrevia();
        });
        
        rbPuntos.setOnAction(e -> {
            spinnerDesafios.setDisable(true);
            spinnerPuntos.setDisable(false);
            spinnerLogros.setDisable(true);
            actualizarVistaPrevia();
        });
        
        rbLogros.setOnAction(e -> {
            spinnerDesafios.setDisable(true);
            spinnerPuntos.setDisable(true);
            spinnerLogros.setDisable(false);
            actualizarVistaPrevia();
        });
    }
    
    private void configurarSpinners() {
        // Configurar valores de los spinners
        spinnerDesafios.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));
        spinnerPuntos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 5000, 500, 100));
        spinnerLogros.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 3));
        
        // Hacer que los spinners sean editables
        spinnerDesafios.setEditable(true);
        spinnerPuntos.setEditable(true);
        spinnerLogros.setEditable(true);
    }
    
    private void configurarSlider() {
        sliderPuntos.setValue(150);
        lblPuntos.setText("150");
        
        sliderPuntos.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblPuntos.setText(String.valueOf(newVal.intValue()));
            actualizarVistaPrevia();
        });
    }
    
    private void configurarListeners() {
        // Listeners para actualización en tiempo real
        txtNombre.textProperty().addListener((obs, oldVal, newVal) -> actualizarVistaPrevia());
        txtDescripcion.textProperty().addListener((obs, oldVal, newVal) -> actualizarVistaPrevia());
        
        spinnerDesafios.valueProperty().addListener((obs, oldVal, newVal) -> actualizarVistaPrevia());
        spinnerPuntos.valueProperty().addListener((obs, oldVal, newVal) -> actualizarVistaPrevia());
        spinnerLogros.valueProperty().addListener((obs, oldVal, newVal) -> actualizarVistaPrevia());
    }
    
    private void actualizarVistaPrevia() {
        vboxVistaPrevia.getChildren().clear();
        
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        int puntosRecompensa = (int) sliderPuntos.getValue();
        
        if (nombre.isEmpty() && descripcion.isEmpty()) {
            Label lblInfo = new Label("Completa los campos para ver la vista previa");
            lblInfo.setStyle("-fx-text-fill: #6c757d;");
            vboxVistaPrevia.getChildren().add(lblInfo);
            return;
        }
        
        // Título del logro
        Label lblTitulo = new Label("🏆 " + (nombre.isEmpty() ? "Nombre del Logro" : nombre));
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");
        
        // Descripción
        Label lblDesc = new Label("📝 " + (descripcion.isEmpty() ? "Descripción del logro" : descripcion));
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");
        
        // Criterio
        String criterio = obtenerCriterioTexto();
        Label lblCriterio = new Label("🎯 Criterio: " + criterio);
        lblCriterio.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        
        // Puntos
        Label lblPuntosInfo = new Label("💎 Recompensa: +" + puntosRecompensa + " puntos");
        lblPuntosInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
        
        vboxVistaPrevia.getChildren().addAll(lblTitulo, lblDesc, lblCriterio, lblPuntosInfo);
    }
    
    private String obtenerCriterioTexto() {
        if (rbDesafios.isSelected()) {
            return "Completar " + spinnerDesafios.getValue() + " desafíos";
        } else if (rbPuntos.isSelected()) {
            return "Obtener " + spinnerPuntos.getValue() + " puntos";
        } else if (rbLogros.isSelected()) {
            return "Desbloquear " + spinnerLogros.getValue() + " logros";
        }
        return "Sin criterio definido";
    }
    
    private String obtenerCriterioSistema() {
        if (rbDesafios.isSelected()) {
            return "desafios_completados:" + spinnerDesafios.getValue();
        } else if (rbPuntos.isSelected()) {
            return "puntos_totales:" + spinnerPuntos.getValue();
        } else if (rbLogros.isSelected()) {
            return "logros_obtenidos:" + spinnerLogros.getValue();
        }
        return "desafios_completados:1"; // Valor por defecto
    }
    
    @FXML
    private void onCancelarClicked(ActionEvent event) {
        System.out.println(">>> Cancelando creación de logro");
        cerrarVentana();
    }
    
    @FXML
    private void onCrearClicked(ActionEvent event) {
        try {
            // Validaciones
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAlerta("Error", "El nombre del logro no puede estar vacío");
                return;
            }
            
            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                mostrarAlerta("Error", "La descripción del logro no puede estar vacía");
                return;
            }
            
            if (criterioGroup.getSelectedToggle() == null) {
                mostrarAlerta("Error", "Debe seleccionar un criterio de desbloqueo");
                return;
            }
            
            // Verificar que no existe un logro con el mismo nombre
            for (Logro logro : Main.getLogrosDisponibles()) {
                if (logro.getNombre().equalsIgnoreCase(nombre)) {
                    mostrarAlerta("Error", "Ya existe un logro con el nombre '" + nombre + "'");
                    return;
                }
            }
            
            // Crear el logro
            String criterio = obtenerCriterioSistema();
            int puntos = (int) sliderPuntos.getValue();
            
            Logro nuevoLogro = new Logro(nombre, descripcion, criterio, puntos);
            Main.getLogrosDisponibles().add(nuevoLogro);
            
            // Mensaje de éxito
            String mensaje = String.format(
                "¡Logro creado exitosamente!\n\n" +
                "Nombre: %s\n" +
                "Descripción: %s\n" +
                "Criterio: %s\n" +
                "Recompensa: +%d puntos",
                nombre, descripcion, obtenerCriterioTexto(), puntos
            );
            
            mostrarAlerta("Éxito", mensaje);
            
            System.out.println(">>> Logro personalizado creado!");
            System.out.println("* " + nombre + " - " + descripcion);
            System.out.println("  Criterio: " + criterio + " | Recompensa: +" + puntos + " pts");
            
            cerrarVentana();
            
        } catch (Exception e) {
            System.err.println("Error al crear logro: " + e.getMessage());
            mostrarAlerta("Error", "Error al crear el logro: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(titulo.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
} 