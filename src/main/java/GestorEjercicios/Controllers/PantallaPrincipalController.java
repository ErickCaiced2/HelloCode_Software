package GestorEjercicios.Controllers;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.exercises.Lenguaje;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador principal de la pantalla principal del Gestor de Ejercicios
 * Maneja la navegación entre lecciones y la configuración del sistema
 */
public class PantallaPrincipalController {
    
    @FXML
    private Button btnCrearLeccion; // Botón "FINAL" - Quinta lección
    
    @FXML
    private Button btnEstadisticas; // Botón "5" - Cuarta lección
    
    @FXML
    private Button btnConfiguracion; // Botón "4" - Tercera lección
    
    @FXML
    private Button btnLeccion2; // Botón "2" - Segunda lección
    
    @FXML
    private Button btnLeccion3; // Botón "3" - Tercera lección
    
    @FXML
    private Button btnSalir; // Botón "1" - Primera lección (NO salir)
    
    @FXML
    private Button btnSalirReal; // Botón real de salir en la barra inferior
    
    // Variables para el progreso de lecciones (sin persistencia)
    private static LenguajeProgramacion lenguajeConfigurado;
    private static NivelDificultad nivelConfigurado;
    private static TipoLeccion tipoConfigurado;
    private static int leccionActual = 1;
    private static boolean primeraLeccionCompletada = false;
    
    @FXML
    public void initialize() {
        configurarBotones();
        actualizarEstadoBotones();
    }
    
    /**
     * Configura las acciones de todos los botones de la interfaz
     */
    private void configurarBotones() {
        // Botón "1" - Primera lección (siempre configuración)
        btnSalir.setOnAction(e -> iniciarPrimeraLeccion());
        
        // Botón "2" - Segunda lección
        btnLeccion2.setOnAction(e -> iniciarLeccion(2));
        
        // Botón "3" - Tercera lección
        btnLeccion3.setOnAction(e -> iniciarLeccion(3));
        
        // Botón "4" - Cuarta lección
        btnConfiguracion.setOnAction(e -> iniciarLeccion(4));
        
        // Botón "5" - Quinta lección
        btnEstadisticas.setOnAction(e -> iniciarLeccion(5));
        
        // Botón "FINAL" - Quinta lección (alternativo)
        btnCrearLeccion.setOnAction(e -> iniciarLeccion(5));
        
        // Botón real de salir en la barra inferior
        btnSalirReal.setOnAction(e -> salirAplicacion());
    }
    
    /**
     * Inicia la primera lección (siempre va a configuración)
     */
    private void iniciarPrimeraLeccion() {
        navegarACrearLeccion();
    }
    
    /**
     * Inicia una lección específica verificando el progreso
     */
    private void iniciarLeccion(int numeroLeccion) {
        if (!primeraLeccionCompletada) {
            System.out.println("Debes completar la primera lección antes de continuar");
            return;
        }
        
        if (lenguajeConfigurado == null || nivelConfigurado == null || tipoConfigurado == null) {
            System.out.println("Configuración no encontrada. Redirigiendo a configuración...");
            navegarACrearLeccion();
            return;
        }
        
        leccionActual = numeroLeccion;
        crearYMostrarLeccion();
    }
    
    /**
     * Crea y muestra una lección con la configuración actual
     */
    private void crearYMostrarLeccion() {
        try {
            // Crear lección usando el controlador de lección existente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/CrearLeccion.fxml"));
            Parent root = loader.load();
            
            LeccionController controller = loader.getController();
            
            // Configurar la lección con los valores guardados
            controller.configurarLeccionConValores(
                mapearLenguaje(lenguajeConfigurado),
                mapearNivel(nivelConfigurado)
            );
            
            Stage stage = new Stage();
            stage.setTitle("Lección " + leccionActual + " - " + tipoConfigurado);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            centrarVentana(stage);
            stage.show();
            
            cerrarVentanaActual();
            
            System.out.println("Iniciando lección " + leccionActual + " con configuración: " + 
                             lenguajeConfigurado + ", " + nivelConfigurado + ", " + tipoConfigurado);
            
        } catch (IOException e) {
            System.err.println("Error al crear lección: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Navega a la pantalla de configuración de lección
     */
    private void navegarACrearLeccion() {
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
     * Guarda la configuración de la lección (sin persistencia)
     */
    public static void guardarConfiguracion(LenguajeProgramacion lenguaje, NivelDificultad nivel, TipoLeccion tipo) {
        lenguajeConfigurado = lenguaje;
        nivelConfigurado = nivel;
        tipoConfigurado = tipo;
        primeraLeccionCompletada = true;
        leccionActual = 1;
        
        System.out.println("Configuración guardada: " + lenguaje + ", " + nivel + ", " + tipo);
    }
    
    /**
     * Marca la lección actual como completada
     */
    public static void marcarLeccionCompletada() {
        leccionActual++;
        System.out.println("Lección " + (leccionActual - 1) + " completada. Siguiente: " + leccionActual);
    }
    
    /**
     * Reinicia el progreso del usuario
     */
    public static void reiniciarProgreso() {
        leccionActual = 1;
        primeraLeccionCompletada = false;
        lenguajeConfigurado = null;
        nivelConfigurado = null;
        tipoConfigurado = null;
        System.out.println("Progreso reiniciado");
    }
    
    /**
     * Actualiza el estado visual de los botones según el progreso
     */
    private void actualizarEstadoBotones() {
        // Marcar lecciones completadas según el progreso
        if (leccionActual > 1) {
            btnSalir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 2) {
            btnLeccion2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 3) {
            btnLeccion3.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 4) {
            btnConfiguracion.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
        }
        if (leccionActual > 5) {
            btnEstadisticas.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");
            btnCrearLeccion.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        }
        
        // Mostrar información de configuración si está disponible
        if (lenguajeConfigurado != null && nivelConfigurado != null && tipoConfigurado != null) {
            System.out.println("Configuración actual: " + lenguajeConfigurado + " - " + nivelConfigurado + " - " + tipoConfigurado);
        }
    }
    
    /**
     * Cierra la aplicación
     */
    private void salirAplicacion() {
        System.exit(0);
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
        Stage currentStage = (Stage) btnCrearLeccion.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Mapea el enum de lenguaje del GestorEjercicios al del Modulo_Ejercicios
     */
    private Lenguaje mapearLenguaje(LenguajeProgramacion lp) {
        switch (lp) {
            case JAVA: return Lenguaje.JAVA;
            case PYTHON: return Lenguaje.PYTHON;
            case CPP: return Lenguaje.C;
            default: return Lenguaje.JAVA;
        }
    }
    
    /**
     * Mapea el enum de nivel de dificultad del GestorEjercicios al del Modulo_Ejercicios
     */
    private Modulo_Ejercicios.exercises.NivelDificultad mapearNivel(NivelDificultad nd) {
        switch (nd) {
            case BASICO: return Modulo_Ejercicios.exercises.NivelDificultad.BASICO;
            case INTERMEDIO: return Modulo_Ejercicios.exercises.NivelDificultad.INTERMEDIO;
            case AVANZADO: return Modulo_Ejercicios.exercises.NivelDificultad.AVANZADO;
            default: return Modulo_Ejercicios.exercises.NivelDificultad.BASICO;
        }
    }
} 