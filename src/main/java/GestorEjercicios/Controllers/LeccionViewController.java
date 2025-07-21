package GestorEjercicios.Controllers;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoEjercicio;
import GestorEjercicios.model.DetalleLeccion;
import GestorEjercicios.model.Ejercicio;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.model.ResultadoEvaluacion;
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
    private List<Ejercicio> ejercicios;
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
    
    private void configurarBotones() {
        btnAnterior.setOnAction(e -> ejercicioAnterior());
        btnSiguiente.setOnAction(e -> ejercicioSiguiente());
        btnEnviar.setOnAction(e -> enviarRespuesta());
    }
    
    private void configurarToggleGroup() {
        toggleGroup = new ToggleGroup();
        rbOpcion1.setToggleGroup(toggleGroup);
        rbOpcion2.setToggleGroup(toggleGroup);
        rbOpcion3.setToggleGroup(toggleGroup);
        rbOpcion4.setToggleGroup(toggleGroup);
    }
    
    public void configurarLeccion(Leccion leccion, LenguajeProgramacion lenguaje, NivelDificultad dificultad) {
        this.leccion = leccion;
        // Convertir DetalleLeccion a Ejercicio
        this.ejercicios = leccion.getEjerciciosResueltos().stream()
                .map(DetalleLeccion::getEjercicio)
                .collect(java.util.stream.Collectors.toList());
        this.lenguaje = lenguaje;
        this.dificultad = dificultad;
        
        // Configurar información de la lección
        lblLenguaje.setText(lenguaje.toString());
        lblDificultad.setText(dificultad.toString());
        
        // Mostrar el primer ejercicio
        mostrarEjercicio(0);
    }
    
    private void mostrarEjercicio(int indice) {
        if (indice < 0 || indice >= ejercicios.size()) {
            return;
        }
        
        ejercicioActual = indice;
        Ejercicio ejercicio = ejercicios.get(indice);
        
        // Actualizar información del ejercicio
        lblTituloEjercicio.setText("Ejercicio " + (indice + 1));
        lblEnunciado.setText(ejercicio.getEnunciado());
        
        // Actualizar progreso
        lblProgreso.setText((indice + 1) + "/" + ejercicios.size());
        progressBar.setProgress((double) (indice + 1) / ejercicios.size());
        
        // Determinar tipo de ejercicio y mostrar interfaz correspondiente
        mostrarInterfazEjercicio(ejercicio);
        
        // Limpiar respuesta anterior
        limpiarRespuesta();
        
        // Actualizar estado de botones
        actualizarEstadoBotones();
    }
    
    private void mostrarInterfazEjercicio(Ejercicio ejercicio) {
        // Ocultar todas las interfaces
        vboxCompletarCodigo.setVisible(false);
        vboxSeleccionMultiple.setVisible(false);
        
        // Mostrar interfaz según el tipo de ejercicio
        if (ejercicio.getTipo() == TipoEjercicio.COMPLETAR_CODIGO) {
            vboxCompletarCodigo.setVisible(true);
            txtRespuesta.clear();
        } else if (ejercicio.getTipo() == TipoEjercicio.MULTIPLE_CHOICE) {
            vboxSeleccionMultiple.setVisible(true);
            configurarOpcionesMultipleChoice(ejercicio);
        } else {
            // Para otros tipos, usar texto libre
            vboxCompletarCodigo.setVisible(true);
            txtRespuesta.clear();
        }
    }
    
    private void configurarOpcionesMultipleChoice(Ejercicio ejercicio) {
        // Parsear las opciones del enunciado (formato: pregunta|opcion1,opcion2,opcion3,opcion4|respuesta)
        String[] partes = ejercicio.getEnunciado().split("\\|");
        if (partes.length >= 2) {
            String opcionesStr = partes[1];
            String[] opciones = opcionesStr.split(",");
            
            if (opciones.length >= 4) {
                rbOpcion1.setText(opciones[0].trim());
                rbOpcion2.setText(opciones[1].trim());
                rbOpcion3.setText(opciones[2].trim());
                rbOpcion4.setText(opciones[3].trim());
            }
        }
        
        toggleGroup.selectToggle(null);
    }
    
    private void enviarRespuesta() {
        if (respuestaEnviada) {
            return;
        }
        
        Ejercicio ejercicio = ejercicios.get(ejercicioActual);
        String respuestaUsuario = obtenerRespuestaUsuario();
        
        if (respuestaUsuario == null || respuestaUsuario.trim().isEmpty()) {
            mostrarMensaje("Por favor, proporciona una respuesta", false);
            return;
        }
        
        // Evaluar respuesta
        ResultadoEvaluacion resultado = ejercicio.resolver(respuestaUsuario);
        resultados.add(resultado);
        
        // Mostrar resultado
        mostrarResultado(resultado);
        
        respuestaEnviada = true;
        btnEnviar.setDisable(true);
        
        // Si es el último ejercicio, mostrar opción de completar lección
        if (ejercicioActual == ejercicios.size() - 1) {
            btnSiguiente.setText("COMPLETAR LECCIÓN");
        }
    }
    
    private String obtenerRespuestaUsuario() {
        Ejercicio ejercicio = ejercicios.get(ejercicioActual);
        
        if (ejercicio.getTipo() == TipoEjercicio.MULTIPLE_CHOICE) {
            RadioButton seleccionado = (RadioButton) toggleGroup.getSelectedToggle();
            return seleccionado != null ? seleccionado.getText() : null;
        } else {
            return txtRespuesta.getText();
        }
    }
    
    private void mostrarResultado(ResultadoEvaluacion resultado) {
        vboxResultado.setVisible(true);
        
        if (resultado.isCorrecto()) {
            lblResultado.setText("¡CORRECTO!");
            lblResultado.setTextFill(Color.GREEN);
            lblMensaje.setText(resultado.getObtenerMensaje());
        } else {
            lblResultado.setText("INCORRECTO");
            lblResultado.setTextFill(Color.RED);
            lblMensaje.setText(resultado.getObtenerMensaje());
        }
    }
    
    private void ejercicioAnterior() {
        if (ejercicioActual > 0) {
            mostrarEjercicio(ejercicioActual - 1);
        }
    }
    
    private void ejercicioSiguiente() {
        if (ejercicioActual < ejercicios.size() - 1) {
            mostrarEjercicio(ejercicioActual + 1);
        } else {
            // Último ejercicio - completar lección
            completarLeccion();
        }
    }
    
    private void completarLeccion() {
        // Calcular puntuación total
        double puntuacionTotal = resultados.stream()
                .mapToDouble(ResultadoEvaluacion::getPuntuacionObtenida)
                .average()
                .orElse(0.0);
        
        // Guardar progreso
        PantallaPrincipalController.marcarLeccionCompletada();
        
        // Mostrar pantalla de lección completada
        mostrarLeccionCompletada(puntuacionTotal);
    }
    
    private void mostrarLeccionCompletada(double puntuacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/LeccionCompletada.fxml"));
            Parent root = loader.load();
            
            LeccionCompletadaController controller = loader.getController();
            controller.configurarResultados((int) puntuacion, resultados.size(), ejercicios.size());
            
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
    
    private void limpiarRespuesta() {
        txtRespuesta.clear();
        toggleGroup.selectToggle(null);
        vboxResultado.setVisible(false);
        respuestaEnviada = false;
        btnEnviar.setDisable(false);
    }
    
    private void actualizarEstadoBotones() {
        btnAnterior.setDisable(ejercicioActual == 0);
        btnSiguiente.setDisable(false);
        
        if (ejercicioActual == ejercicios.size() - 1) {
            btnSiguiente.setText("COMPLETAR LECCIÓN");
        } else {
            btnSiguiente.setText("Siguiente →");
        }
    }
    
    private void mostrarMensaje(String mensaje, boolean esError) {
        Alert alert = new Alert(esError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void centrarVentana(Stage stage) {
        stage.centerOnScreen();
    }
    
    private void cerrarVentanaActual() {
        Stage stage = (Stage) btnEnviar.getScene().getWindow();
        stage.close();
    }
} 