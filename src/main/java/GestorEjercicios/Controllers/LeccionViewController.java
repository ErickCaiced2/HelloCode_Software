package GestorEjercicios.Controllers;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoEjercicio;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.model.ResultadoEvaluacion;
import GestorEjercicios.GestorEjerciciosEntry;
import GestorEjercicios.integracion.IGestorEjercicios;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Usuario.Clases.Usuario;
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
    private Usuario usuarioActual; // Usuario que está tomando la lección
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
     * Configura la lección y el usuario para el controlador
     * MÉTODO PRINCIPAL DE INTEGRACIÓN CON EL SISTEMA 🎯
     */
    public void configurarLeccion(Leccion leccion, Usuario usuario, List<EjercicioSeleccion> ejercicios) {
        // 🚨 EJECUTAR DIAGNÓSTICO ANTES DE EMPEZAR
        System.out.println("\n🔍 ANTES DE EMPEZAR LA LECCIÓN - Ejecutando diagnóstico...");
        GestorEjerciciosEntry.diagnosticoRapido();
        
        this.leccion = leccion;
        
        // 🔄 NUEVO: Obtener usuario actual desde el sistema en lugar de usar el parámetro
        Usuario usuarioSistema = GestorEjerciciosEntry.obtenerUsuarioActual();
        if (usuarioSistema != null) {
            this.usuarioActual = usuarioSistema;
            System.out.println("✅ Usuario obtenido desde sistema: " + usuarioSistema.getUsername());
        } else {
            // Fallback: usar el usuario pasado como parámetro
            this.usuarioActual = usuario;
            System.out.println("⚠️ Usando usuario del parámetro: " + (usuario != null ? usuario.getUsername() : "null"));
        }
        
        this.ejercicios = ejercicios;
        
        // Configurar información de la lección
        if (lblLenguaje != null) {
            lblLenguaje.setText("Lenguaje: " + leccion.getLenguaje());
        }
        if (lblDificultad != null) {
            lblDificultad.setText("Dificultad: " + leccion.getDificultad());
        }
        
        // Mostrar primer ejercicio
        if (!ejercicios.isEmpty()) {
            mostrarEjercicio(0);
        }
        
        System.out.println("🎮 LECCIÓN CONFIGURADA:");
        System.out.println("   📚 " + leccion.getNombre());
        System.out.println("   👤 Usuario activo: " + (this.usuarioActual != null ? this.usuarioActual.getUsername() : "Sin usuario"));
        System.out.println("   🔢 Ejercicios: " + ejercicios.size());
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
        
        // 🔄 NUEVO: Asegurar que tenemos el usuario actual del sistema
        if (this.usuarioActual == null) {
            this.usuarioActual = GestorEjerciciosEntry.obtenerUsuarioActual();
            System.out.println("🔄 Usuario obtenido en completarLeccion: " + 
                (this.usuarioActual != null ? this.usuarioActual.getUsername() : "null"));
        }
        
        // **INTEGRACIÓN CON SISTEMA DE PROGRESO** 🎯
        if (leccion != null && usuarioActual != null) {
            // Marcar lección como completada en el sistema
            GestorEjerciciosEntry.marcarLeccionCompletada(leccion, usuarioActual, correctos);
            
            // Obtener estadísticas actualizadas del usuario
            IGestorEjercicios.EstadisticasUsuario estadisticas = 
                GestorEjerciciosEntry.obtenerEstadisticasUsuario(usuarioActual);
            
            System.out.println("🎉 LECCIÓN COMPLETADA:");
            System.out.println("   👤 Usuario: " + usuarioActual.getUsername());
            System.out.println("   📚 Lección: " + leccion.getNombre());
            System.out.println("   ✅ Aciertos: " + correctos + "/" + ejercicios.size());
            System.out.println("   🌟 Experiencia total: " + estadisticas.getExperienciaTotal());
            System.out.println("   🧠 Conocimiento total: " + estadisticas.getConocimientoTotal());
            System.out.println("   📈 Lecciones completadas: " + estadisticas.getLeccionesCompletadas());
            
            // 🔍 VERIFICAR QUE SE GUARDÓ CON EL USUARIO CORRECTO
            System.out.println("🔍 VERIFICACIÓN DE GUARDADO:");
            System.out.println("   🆔 Username del usuario: " + usuarioActual.getUsername());
            System.out.println("   📊 Datos guardados para: " + usuarioActual.getUsername());
        } else {
            System.err.println("❌ Error: No se pudo completar la lección");
            System.err.println("   📚 Lección: " + (leccion != null ? leccion.getNombre() : "null"));
            System.err.println("   👤 Usuario: " + (usuarioActual != null ? usuarioActual.getUsername() : "null"));
        }
        
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
    
    // ============================================
    // 🎯 MÉTODOS ESTÁTICOS PARA OTROS MÓDULOS
    // ============================================
    
    /**
     * Método estático para que otros módulos obtengan estadísticas de usuario
     * PUNTO DE INTEGRACIÓN PRINCIPAL 🔗
     */
    public static IGestorEjercicios.EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario) {
        return GestorEjerciciosEntry.obtenerEstadisticasUsuario(usuario);
    }
    
    /**
     * 🎮 MÉTODO ESPECÍFICO PARA GAMIFICACIÓN: Obtener número de lecciones completadas
     * Integración directa con DesafioSemanal y DesafioMensual
     */
    public static int obtenerNumeroLeccionesCompletadas(Usuario usuario) {
        IGestorEjercicios.EstadisticasUsuario estadisticas = obtenerEstadisticasUsuario(usuario);
        return estadisticas.getLeccionesCompletadas();
    }
    
    /**
     * 🎮 MÉTODO PARA GAMIFICACIÓN: Verificar si el usuario ha completado al menos N lecciones
     * Útil para desbloquear logros basados en cantidad de lecciones
     */
    public static boolean haCompletadoMinLecciones(Usuario usuario, int numeroMinimo) {
        return obtenerNumeroLeccionesCompletadas(usuario) >= numeroMinimo;
    }
    
    /**
     * 🎮 MÉTODO PARA GAMIFICACIÓN: Obtener lista detallada de lecciones completadas
     * Información completa para análisis y estadísticas avanzadas
     */
    public static java.util.Collection<String> obtenerNombresLeccionesCompletadas(Usuario usuario) {
        try {
            // Acceder al progreso del usuario
            var progreso = GestorEjercicios.model.GestorProgresoUsuario.obtenerProgresoUsuario(usuario);
            return progreso.getLeccionesCompletadas().values().stream()
                .map(leccion -> leccion.getNombreLeccion())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error al obtener nombres de lecciones completadas: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * 🎮 MÉTODO PARA GAMIFICACIÓN: Obtener progreso de lecciones como porcentaje
     * Retorna el porcentaje de lecciones completadas respecto al total disponible
     */
    public static double obtenerPorcentajeLeccionesCompletadas(Usuario usuario) {
        try {
            int completadas = obtenerNumeroLeccionesCompletadas(usuario);
            int totalDisponibles = GestorEjerciciosEntry.obtenerTodasLasLecciones().size();
            
            if (totalDisponibles == 0) return 0.0;
            return (double) completadas / totalDisponibles * 100.0;
        } catch (Exception e) {
            System.err.println("Error al calcular porcentaje de lecciones: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * 🔧 MÉTODO AUXILIAR: Obtener usuario actual del sistema
     * Wrapper para acceder fácilmente al usuario actual desde cualquier parte del controlador
     */
    public static Usuario obtenerUsuarioActualDelSistema() {
        return GestorEjerciciosEntry.obtenerUsuarioActual();
    }
    
    /**
     * Método estático para que otros módulos marquen lecciones completadas
     * PUNTO DE INTEGRACIÓN PARA GAMIFICACIÓN 🎮
     */
    public static void registrarLeccionCompletada(Leccion leccion, Usuario usuario, int aciertos) {
        GestorEjerciciosEntry.marcarLeccionCompletada(leccion, usuario, aciertos);
    }
    
    /**
     * Método estático para obtener progreso del usuario en una lección específica
     */
    public static double obtenerProgresoLeccion(Leccion leccion, Usuario usuario) {
        return GestorEjerciciosEntry.obtenerProgresoUsuario(leccion, usuario);
    }
} 