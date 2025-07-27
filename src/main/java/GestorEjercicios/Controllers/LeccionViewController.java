package GestorEjercicios.Controllers;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoEjercicio;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.model.ResultadoEvaluacion;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la vista de lección
 * Maneja la presentación y evaluación de ejercicios en una lección
 */
public class LeccionViewController {
    
    @FXML private Label lblLenguaje;
    @FXML private Label lblDificultad;
    @FXML private Label lblProgreso;
    @FXML private ProgressBar progressBar;
    
    @FXML private Label lblTituloEjercicio;
    @FXML private Label lblEnunciado;
    
    @FXML private VBox vboxCompletarCodigo;
    @FXML private TextArea txtRespuesta;
    
    @FXML private VBox vboxSeleccionMultiple;
    @FXML private RadioButton rbOpcion1;
    @FXML private RadioButton rbOpcion2;
    @FXML private RadioButton rbOpcion3;
    @FXML private RadioButton rbOpcion4;
    @FXML private ToggleGroup toggleGroup;
    
    @FXML private VBox vboxResultado;
    @FXML private Label lblResultado;
    @FXML private Label lblMensaje;
    
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnEnviar;
    
    // Variables de estado
    private Leccion leccion;
    private List<EjercicioSeleccion> ejercicios;
    private int ejercicioActual = 0;
    private List<ResultadoEvaluacion> resultados = new ArrayList<>();
    private boolean respuestaEnviada = false;
    
    // Configuración de la lección
    private LenguajeProgramacion lenguaje;
    private NivelDificultad dificultad;
    private TipoEjercicio tipoEjercicio;
    
    @FXML
    public void initialize() {
        configurarBotones();
        configurarToggleGroup();
    }
    
    /**
     * Configura las acciones de los botones
     */
    private void configurarBotones() {
        btnAnterior.setOnAction(e -> ejercicioAnterior());
        btnSiguiente.setOnAction(e -> ejercicioSiguiente());
        btnEnviar.setOnAction(e -> enviarRespuesta());
    }
    
    /**
     * Configura el grupo de botones de radio para selección múltiple
     */
    private void configurarToggleGroup() {
        toggleGroup = new ToggleGroup();
        rbOpcion1.setToggleGroup(toggleGroup);
        rbOpcion2.setToggleGroup(toggleGroup);
        rbOpcion3.setToggleGroup(toggleGroup);
        rbOpcion4.setToggleGroup(toggleGroup);
    }
    
    /**
     * Muestra un ejercicio específico en la interfaz
     */
    private void mostrarEjercicio(int indice) {
        if (indice < 0 || indice >= ejercicios.size()) {
            return;
        }
        
        ejercicioActual = indice;
        EjercicioSeleccion ejercicio = ejercicios.get(indice);
        
        // Actualizar información del ejercicio
        lblTituloEjercicio.setText("Ejercicio " + (indice + 1));
        lblEnunciado.setText(ejercicio.getInstruccion());
        
        // Actualizar progreso
        lblProgreso.setText((indice + 1) + "/" + ejercicios.size());
        progressBar.setProgress((double) (indice + 1) / ejercicios.size());
        
        // Mostrar interfaz para selección múltiple (todos los ejercicios son de este tipo)
        mostrarInterfazEjercicio(ejercicio);
        
        // Limpiar respuesta anterior
        limpiarRespuesta();
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    /**
     * Muestra la interfaz apropiada para el tipo de ejercicio
     */
    private void mostrarInterfazEjercicio(EjercicioSeleccion ejercicio) {
        // Ocultar todas las interfaces
        vboxCompletarCodigo.setVisible(false);
        vboxSeleccionMultiple.setVisible(true);
        vboxResultado.setVisible(false);
        
        // Configurar opciones para selección múltiple
        configurarOpcionesMultipleChoice(ejercicio);
    }
    
    /**
     * Configura las opciones para un ejercicio de selección múltiple
     */
    private void configurarOpcionesMultipleChoice(EjercicioSeleccion ejercicio) {
        // Obtener las opciones del ejercicio
        ArrayList<String> opciones = ejercicio.getListOpciones();
        
        if (opciones.size() >= 4) {
            rbOpcion1.setText(opciones.get(0));
            rbOpcion2.setText(opciones.get(1));
            rbOpcion3.setText(opciones.get(2));
            rbOpcion4.setText(opciones.get(3));
        }
    }
    
    /**
     * Procesa la respuesta del usuario
     */
    private void enviarRespuesta() {
        if (respuestaEnviada) {
            return;
        }
        
        EjercicioSeleccion ejercicio = ejercicios.get(ejercicioActual);
        String respuestaUsuario = obtenerRespuestaUsuario();
        
        // Evaluar respuesta - verificar si la respuesta está en las respuestas correctas
        boolean esCorrecta = ejercicio.obtenerRespuestasCorrectas().contains(respuestaUsuario);
        ResultadoEvaluacion resultado = new ResultadoEvaluacion(
            esCorrecta, 
            esCorrecta ? 100 : 0, 
            30, 
            esCorrecta ? "¡Correcto!" : "Incorrecto", 
            false
        );
        resultados.add(resultado);
        
        // Mostrar resultado
        mostrarResultado(resultado);
        respuestaEnviada = true;
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    /**
     * Obtiene la respuesta del usuario según el tipo de ejercicio
     */
    private String obtenerRespuestaUsuario() {
        // Para ejercicios de selección múltiple
        RadioButton seleccionado = (RadioButton) toggleGroup.getSelectedToggle();
        return seleccionado != null ? seleccionado.getText() : "";
    }
    
    /**
     * Muestra el resultado de la evaluación
     */
    private void mostrarResultado(ResultadoEvaluacion resultado) {
        vboxResultado.setVisible(true);
        
        if (resultado.isCorrecto()) {
            lblResultado.setText("¡Correcto!");
            lblResultado.setTextFill(Color.GREEN);
        } else {
            lblResultado.setText("Incorrecto");
            lblResultado.setTextFill(Color.RED);
        }
        
        lblMensaje.setText(resultado.getObtenerMensaje());
    }
    
    /**
     * Navega al ejercicio anterior
     */
    private void ejercicioAnterior() {
        if (ejercicioActual > 0) {
            mostrarEjercicio(ejercicioActual - 1);
        }
    }
    
    /**
     * Navega al siguiente ejercicio o completa la lección
     */
    private void ejercicioSiguiente() {
        if (ejercicioActual < ejercicios.size() - 1) {
            mostrarEjercicio(ejercicioActual + 1);
        } else {
            // Último ejercicio - completar lección
            completarLeccion();
        }
    }
    
    /**
     * Completa la lección y muestra los resultados
     */
    private void completarLeccion() {
        // Calcular puntuación final
        int correctos = (int) resultados.stream().filter(ResultadoEvaluacion::isCorrecto).count();
        double puntuacion = (double) correctos / ejercicios.size() * 100.0;
        
        // Mostrar pantalla de lección completada
        mostrarLeccionCompletada(puntuacion);
    }
    
    /**
     * Muestra la pantalla de lección completada
     */
    private void mostrarLeccionCompletada(double puntuacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/LeccionCompletada.fxml"));
            Parent root = loader.load();
            
            LeccionCompletadaController controller = loader.getController();
            int ejerciciosCorrectos = (int) resultados.stream().filter(ResultadoEvaluacion::isCorrecto).count();
            controller.configurarResultados((int)puntuacion, ejerciciosCorrectos, ejercicios.size());
            
            Stage stage = new Stage();
            stage.setTitle("Lección Completada");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            centrarVentana(stage);
            stage.show();
            
            cerrarVentanaActual();
            
        } catch (IOException e) {
            System.err.println("Error al mostrar lección completada: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpia la respuesta actual
     */
    private void limpiarRespuesta() {
        txtRespuesta.clear();
        toggleGroup.selectToggle(null);
        vboxResultado.setVisible(false);
        respuestaEnviada = false;
    }
    
    /**
     * Actualiza el estado de los botones según el progreso
     */
    private void actualizarEstadoBotones() {
        btnAnterior.setDisable(ejercicioActual == 0);
        btnSiguiente.setDisable(ejercicioActual == ejercicios.size() - 1);
        btnEnviar.setDisable(respuestaEnviada);
    }
    
    /**
     * Muestra un mensaje al usuario
     */
    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(esError ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Centra la ventana en la pantalla
     */
    private void centrarVentana(Stage stage) {
        stage.centerOnScreen();
    }
    
    /**
     * Cierra la ventana actual
     */
    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnEnviar.getScene().getWindow();
        currentStage.close();
    }
} 