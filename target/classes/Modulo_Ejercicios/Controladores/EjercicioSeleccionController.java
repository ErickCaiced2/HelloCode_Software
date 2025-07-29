package Modulo_Ejercicios.Controladores;

import MetodosGlobales.MetodosFrecuentes;
import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.otrosModulos.Leccion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EjercicioSeleccionController implements Initializable {

    // Enum para tipos de respuesta
    public enum TipoRespuesta {
        CORRECTO, INCORRECTO, PARCIALMENTE_CORRECTO
    }

    // Variables para el sistema de vidas
    private int vidasActuales = 20;
    private final int VIDAS_MAXIMAS = 3;

    @FXML
    private ImageView imgFondo;

    @FXML
    private Text txtLiveCount;

    @FXML
    private Button btnPista;

    @FXML
    private ImageView imgPista;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lblInstruccion;

    @FXML
    private Label lblPregunta;

    @FXML
    private Button btnComprobar;

    // Contenedor dinámico para los botones de opciones
    @FXML
    private VBox opcionesContainer;

    // Elementos del panel de feedback (Duolingo-style)
    @FXML
    private AnchorPane feedbackPanel;

    // Paneles específicos para cada tipo de respuesta
    @FXML
    private VBox panelCorrecto;

    @FXML
    private VBox panelParcial;

    @FXML
    private VBox panelIncorrecto;

    @FXML
    private VBox panelGameOver;

    // Textos para respuestas correctas
    @FXML
    private Text textCorrecto;

    @FXML
    private Text textCorrectoDetalle;

    // Textos para respuestas parciales
    @FXML
    private Text textParcial;

    @FXML
    private Text textParcialDetalle;

    @FXML
    private Text textRespuestasCorrectas;

    // Textos para respuestas incorrectas
    @FXML
    private Text textIncorrecto;

    @FXML
    private Text textIncorrectoDetalle;

    @FXML
    private Text textRespuestasCorrectasError;

    // Textos para Game Over
    @FXML
    private Text textGameOver;

    @FXML
    private Text textGameOverDetalle;

    // Botón siguiente
    @FXML
    private Button btnSiguiente;

    // Lista para almacenar los botones generados dinámicamente
    private List<Button> botonesOpciones = new ArrayList<>();

    // Lista para almacenar las opciones seleccionadas
    private List<String> opcionesSeleccionadas = new ArrayList<>();

    // Lista para almacenar si cada respuesta fue correcta
    private List<Boolean> respuestasCorrectasUsuario = new ArrayList<>();

    // Variables para manejar la lección y ejercicios
    private Leccion leccionTiposDatos;
    private List<EjercicioSeleccion> ejerciciosSeleccion;
    private int ejercicioActual = 0;
    private int totalEjercicios = 0;
    private boolean modoComprobar = true; // true = "Comprobar", false = "Siguiente"

    List<EjercicioSeleccion> ejerciciosSeleccionRepository = EjercicioRepository.cargarEjerciciosSeleccion();

    public EjercicioSeleccionController(String instruccion, ArrayList<String> listOpciones,
                                        ArrayList<String> obtenerRespuestasCorrectas) {
        // Constructor logic aquí
    }

    public EjercicioSeleccionController() {
        // Constructor requerido por FXMLLoader
    }

    // Que la instruccion vaya al lblInstruccion
    public void setInstruccion(String instruccion) {
        lblInstruccion.setText(instruccion);
    }

    // Método para generar botones dinámicamente según la cantidad de opciones
    public void setOpciones(List<String> opciones) {
        // Limpiar el contenedor y la lista de botones
        opcionesContainer.getChildren().clear();
        botonesOpciones.clear();

        // Determinar el layout según la cantidad de opciones
        if (opciones.size() <= 3) {
            // Para 1-3 opciones, usar layout vertical
            crearBotonesVerticales(opciones);
        } else {
            // Para 4+ opciones, usar layout mixto (2 columnas)
            crearBotonesMixtos(opciones);
        }
    }

    private void crearBotonesVerticales(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            Button btn = crearBoton(opciones.get(i), i);
            opcionesContainer.getChildren().add(btn);
            botonesOpciones.add(btn);
        }
    }

    private void crearBotonesMixtos(List<String> opciones) {
        // Crear filas con máximo 2 botones por fila
        for (int i = 0; i < opciones.size(); i += 2) {
            HBox fila = new HBox(20); // 20 px de spacing
            fila.setAlignment(javafx.geometry.Pos.CENTER);

            // Primer botón de la fila
            Button btn1 = crearBoton(opciones.get(i), i);
            fila.getChildren().add(btn1);
            botonesOpciones.add(btn1);

            // Segundo botón de la fila (si existe)
            if (i + 1 < opciones.size()) {
                Button btn2 = crearBoton(opciones.get(i + 1), i + 1);
                fila.getChildren().add(btn2);
                botonesOpciones.add(btn2);
            }

            opcionesContainer.getChildren().add(fila);
        }
    }

    private Button crearBoton(String texto, int indice) {
        Button btn = new Button(texto);
        btn.setPrefHeight(40.0);
        btn.setPrefWidth(140.0);
        btn.setWrapText(true); // Permite que el texto se ajuste si es muy largo
        btn.getStyleClass().add("boton-opcion");

        // Agregar evento de click
        btn.setOnAction(event -> manejarSeleccionOpcion(btn, indice, texto));

        return btn;
    }

    // Método para manejar la selección de una opción
    private void manejarSeleccionOpcion(Button botonSeleccionado, int indice, String texto) {
        // Verificar si ya está seleccionado
        if (opcionesSeleccionadas.contains(texto)) {
            // Deseleccionar - volver al estilo original
            opcionesSeleccionadas.remove(texto);
            botonSeleccionado.getStyleClass().clear();
            botonSeleccionado.getStyleClass().add("boton-opcion");
        } else {
            // Seleccionar - cambiar a estilo seleccionado
            opcionesSeleccionadas.add(texto);
            botonSeleccionado.getStyleClass().clear();
            botonSeleccionado.getStyleClass().add("boton-seleccionado");
        }

        // Mostrar u ocultar el botón comprobar según si hay selecciones
        actualizarVisibilidadBotonComprobar();

        System.out.println("Opciones seleccionadas: " + opcionesSeleccionadas);
    }

    // Método para obtener las opciones seleccionadas
    public List<String> getOpcionesSeleccionadas() {
        return new ArrayList<>(opcionesSeleccionadas);
    }

    // Método para actualizar la visibilidad del botón comprobar
    private void actualizarVisibilidadBotonComprobar() {
        if (btnComprobar != null) {
            btnComprobar.setVisible(!opcionesSeleccionadas.isEmpty());
        }
    }

    public void limpiarSelecciones() {
        opcionesSeleccionadas.clear();
        for (Button btn : botonesOpciones) {
            btn.getStyleClass().clear();
            btn.getStyleClass().add("boton-opcion");
        }
        // Ocultar el botón comprobar cuando no hay selecciones
        actualizarVisibilidadBotonComprobar();
    }

    public void setSeleccionUnica(boolean seleccionUnica) {
        if (seleccionUnica) {
            // Modificar el comportamiento para permitir solo una selección
            for (Button btn : botonesOpciones) {
                btn.setOnAction(event -> {
                    // Limpiar todas las selecciones
                    limpiarSelecciones();

                    // Seleccionar solo esta opción
                    opcionesSeleccionadas.add(btn.getText());
                    btn.getStyleClass().clear();
                    btn.getStyleClass().add("boton-seleccionado");
                    
                    // Actualizar visibilidad del botón comprobar
                    actualizarVisibilidadBotonComprobar();
                });
            }
        }
    }

    public void setEjercicios(List<EjercicioSeleccion> ejercicios) {
        this.ejerciciosSeleccion = ejercicios;
        this.totalEjercicios = ejercicios != null ? ejercicios.size() : 0;
        this.ejercicioActual = 0;
        if (totalEjercicios > 0) {
            cargarEjercicio(0);
        }
    }

    // Método para inicializar la lección y ejercicios
    private void inicializarLeccion() {
        // Crear instancia de la lección
        leccionTiposDatos = new Leccion("Tipos de Datos");

        // Cargar ejercicios desde el repositorio
        List<EjercicioSeleccion> aux;
        aux = EjercicioRepository.cargarEjerciciosSeleccion();
        ejerciciosSeleccion = new ArrayList<>();
        ejerciciosSeleccion.add(aux.get(0));
        ejerciciosSeleccion.add(aux.get(1));
        ejerciciosSeleccion.add(aux.get(2));
        // Agregar ejercicios a la lección
        for (EjercicioSeleccion ejercicio : ejerciciosSeleccion) {
            leccionTiposDatos.addEjercicio(ejercicio);
        }

        // Contar el número total de ejercicios
        totalEjercicios = leccionTiposDatos.getNumEjercicios();

        // Establecer el nombre de la lección en lblPregunta
        lblPregunta.setText(leccionTiposDatos.getNombre());

        // Inicializar el progress bar
        progressBar.setProgress(0.0);

        // Cargar el primer ejercicio
        if (totalEjercicios > 0) {
            cargarEjercicio(0);
        }
    }

    // Método para cargar un ejercicio específico
    private void cargarEjercicio(int indice) {
        if (indice >= 0 && indice < ejerciciosSeleccion.size()) {
            EjercicioSeleccion ejercicio = ejerciciosSeleccion.get(indice);

            // Establecer la instrucción
            lblInstruccion.setText(ejercicio.getInstruccion());

            // Limpiar selecciones anteriores
            limpiarSelecciones();

            // Crear botones según las opciones disponibles
            List<String> opciones = ejercicio.getListOpciones();
            setOpciones(opciones);

            // Resetear estado de UI
            btnComprobar.setVisible(false); // Ocultar inicialmente hasta que se haga una selección
            btnComprobar.setText("COMPROBAR");
            if (feedbackPanel != null) {
                feedbackPanel.setVisible(false);
            }

            System.out.println("Ejercicio " + (indice + 1) + " cargado. Opciones: " + opciones.size() +
                    ", Respuestas correctas: " + ejercicio.obtenerRespuestasCorrectas().size());
        }
    }

    // Método para manejar el click del botón Comprobar
    @FXML
    private void handleComprobar() {
        if (opcionesSeleccionadas.isEmpty()) {
            // Mostrar advertencia si no hay selecciones
            mostrarFeedback(TipoRespuesta.INCORRECTO, "Por favor, selecciona al menos una opción antes de comprobar.", null);
            btnComprobar.setVisible(false);
            feedbackPanel.setVisible(true);
            return;
        }
        
        // Validar respuestas
        validarRespuestas();
    }

    // Nuevo método para actualizar los colores de los botones después de comprobar
    private void actualizarColoresBotones(List<String> respuestasCorrectas) {
        for (Button btn : botonesOpciones) {
            String textoBoton = btn.getText();
            boolean esCorrecta = respuestasCorrectas.contains(textoBoton);
            boolean fueSeleccionada = opcionesSeleccionadas.contains(textoBoton);

            btn.getStyleClass().clear();

            if (fueSeleccionada && esCorrecta) {
                // Seleccionada y correcta -> Verde
                btn.getStyleClass().clear();
                btn.getStyleClass().add("boton-correcto");
            } else if (fueSeleccionada && !esCorrecta) {
                // Seleccionada pero incorrecta -> Rojo
                btn.getStyleClass().add("boton-incorrecto");
            } else if (!fueSeleccionada && esCorrecta) {
                // No seleccionada pero era correcta -> Naranja
                btn.getStyleClass().add("boton-no-seleccionado-correcto");
            } else {
                // No seleccionada y no era correcta -> Mantener color original
                btn.getStyleClass().add("boton-opcion");
            }
        }
    }

    private void validarRespuestas() {
        if (ejercicioActual < ejerciciosSeleccion.size()) {
            EjercicioSeleccion ejercicio = ejerciciosSeleccion.get(ejercicioActual);
            List<String> respuestasCorrectas = ejercicio.obtenerRespuestasCorrectas();

            // Analizar la respuesta del usuario
            TipoRespuesta tipoRespuesta = analizarRespuesta(opcionesSeleccionadas, respuestasCorrectas);

            // Actualizar colores de todos los botones según el resultado
            actualizarColoresBotones(respuestasCorrectas);

            // Manejar según el tipo de respuesta
            switch (tipoRespuesta) {
                case CORRECTO:
                    respuestasCorrectasUsuario.add(true);
                    mostrarFeedback(tipoRespuesta, "¡Excelente trabajo! Tu respuesta es completamente correcta.", null);
                    break;
                case PARCIALMENTE_CORRECTO:
                    respuestasCorrectasUsuario.add(false);
                    int correctas = contarRespuestasCorrectas(opcionesSeleccionadas, respuestasCorrectas);
                    String respuestasCorrectasStr = String.join(", ", respuestasCorrectas);
                    mostrarFeedback(tipoRespuesta, "Bien, pero incompleto. Tienes " + correctas + " respuesta(s) correcta(s) de " + respuestasCorrectas.size() + ".", 
                                   respuestasCorrectasStr);
                    reducirVida();
                    break;
                case INCORRECTO:
                    respuestasCorrectasUsuario.add(false);
                    String respuestasCorrectasError = String.join(", ", respuestasCorrectas);
                    mostrarFeedback(tipoRespuesta, "Respuesta incorrecta. No te preocupes, inténtalo de nuevo.", 
                                   respuestasCorrectasError);
                    reducirVida();
                    break;
            }

            // Ocultar el botón comprobar y mostrar el feedback
            btnComprobar.setVisible(false);
            feedbackPanel.setVisible(true);

            actualizarProgressBar();
        }
    }

    // Método para analizar el tipo de respuesta
    private TipoRespuesta analizarRespuesta(List<String> seleccionadas, List<String> correctas) {
        if (seleccionadas.isEmpty()) {
            return TipoRespuesta.INCORRECTO;
        }

        int respuestasCorrectasUsuario = 0;
        int respuestasIncorrectasUsuario = 0;

        // Contar respuestas correctas e incorrectas del usuario
        for (String seleccionada : seleccionadas) {
            if (correctas.contains(seleccionada)) {
                respuestasCorrectasUsuario++;
            } else {
                respuestasIncorrectasUsuario++;
            }
        }

        // Determinar el tipo de respuesta
        if (respuestasCorrectasUsuario == correctas.size() && respuestasIncorrectasUsuario == 0) {
            return TipoRespuesta.CORRECTO;
        } else if (respuestasCorrectasUsuario > 0 && respuestasIncorrectasUsuario == 0) {
            return TipoRespuesta.PARCIALMENTE_CORRECTO;
        } else {
            return TipoRespuesta.INCORRECTO;
        }
    }

    // Método para contar respuestas correctas del usuario
    private int contarRespuestasCorrectas(List<String> seleccionadas, List<String> correctas) {
        int contador = 0;
        for (String seleccionada : seleccionadas) {
            if (correctas.contains(seleccionada)) {
                contador++;
            }
        }
        return contador;
    }

    // Método para mostrar feedback estilo Duolingo
    private void mostrarFeedback(TipoRespuesta tipo, String mensaje, String explicacion) {
        // Ocultar todos los paneles primero
        ocultarTodosPanelesFeedback();
        
        // Mostrar el panel correspondiente según el tipo de respuesta
        switch (tipo) {
            case CORRECTO:
                mostrarPanelCorrecto(mensaje, explicacion);
                break;
            case INCORRECTO:
                if (vidasActuales <= 0) {
                    mostrarPanelGameOver();
                } else {
                    mostrarPanelIncorrecto(mensaje, explicacion);
                }
                break;
            case PARCIALMENTE_CORRECTO:
                mostrarPanelParcial(mensaje, explicacion);
                break;
        }
        
        // Configurar y mostrar el botón siguiente
        configurarBotonSiguiente();
        
        // Mostrar el panel de feedback
        feedbackPanel.setVisible(true);
    }

    // Método para ocultar todos los paneles de feedback
    private void ocultarTodosPanelesFeedback() {
        if (panelCorrecto != null) panelCorrecto.setVisible(false);
        if (panelParcial != null) panelParcial.setVisible(false);
        if (panelIncorrecto != null) panelIncorrecto.setVisible(false);
        if (panelGameOver != null) panelGameOver.setVisible(false);
    }

    // Método para mostrar panel correcto
    private void mostrarPanelCorrecto(String mensaje, String explicacion) {
        if (panelCorrecto != null) {
            panelCorrecto.setVisible(true);
            if (textCorrecto != null) {
                textCorrecto.setText("¡Perfecto!");
            }
            if (textCorrectoDetalle != null) {
                textCorrectoDetalle.setText(mensaje != null ? mensaje : "¡Has respondido todas las opciones correctamente!");
            }
        }
    }

    // Método para mostrar panel parcial
    private void mostrarPanelParcial(String mensaje, String respuestasCorrectas) {
        if (panelParcial != null) {
            panelParcial.setVisible(true);
            if (textParcial != null) {
                textParcial.setText("¡Casi!");
            }
            if (textParcialDetalle != null) {
                textParcialDetalle.setText(mensaje != null ? mensaje : "Respuesta incompleta. Te faltan algunas opciones correctas.");
            }
            if (textRespuestasCorrectas != null && respuestasCorrectas != null) {
                textRespuestasCorrectas.setText("Respuestas correctas: " + respuestasCorrectas);
            }
        }
    }

    // Método para mostrar panel incorrecto
    private void mostrarPanelIncorrecto(String mensaje, String respuestasCorrectas) {
        if (panelIncorrecto != null) {
            panelIncorrecto.setVisible(true);
            if (textIncorrecto != null) {
                textIncorrecto.setText("¡Inténtalo de nuevo!");
            }
            if (textIncorrectoDetalle != null) {
                textIncorrectoDetalle.setText(mensaje != null ? mensaje : "Respuesta incorrecta. Revisa las opciones.");
            }
            if (textRespuestasCorrectasError != null && respuestasCorrectas != null) {
                textRespuestasCorrectasError.setText("Las respuestas correctas son: " + respuestasCorrectas);
            }
        }
    }

    // Método para mostrar panel Game Over
    private void mostrarPanelGameOver() {
        if (panelGameOver != null) {
            panelGameOver.setVisible(true);
            if (textGameOver != null) {
                textGameOver.setText("¡Se agotaron las vidas!");
            }
            if (textGameOverDetalle != null) {
                textGameOverDetalle.setText("No te preocupes, puedes intentarlo nuevamente.");
            }
        }
    }

    // Método para configurar el botón siguiente
    private void configurarBotonSiguiente() {
        if (btnSiguiente != null) {
            btnSiguiente.setDisable(false);
            btnSiguiente.setOpacity(1.0);
            
            if (vidasActuales <= 0) {
                btnSiguiente.setText("REINICIAR");
            } else {
                btnSiguiente.setText("SIGUIENTE");
            }
        }
    }

    // Método para reducir vidas
    private void reducirVida() {
        vidasActuales--;
        actualizarVidasUI();
        
        if (vidasActuales <= 0) {
            manejarGameOver();
        }
    }

    // Método para actualizar la UI de las vidas
    private void actualizarVidasUI() {
        if (txtLiveCount != null) {
            txtLiveCount.setText(String.valueOf(vidasActuales));
        }
    }

    // Método para manejar Game Over
    private void manejarGameOver() {
        // El manejo de Game Over ahora se hace en mostrarFeedback
        // Este método se puede usar para lógica adicional si es necesario
    }

    // Método para validar si las respuestas seleccionadas son correctas
    private boolean validarSeleccion(List<String> seleccionadas, List<String> correctas) {
        if (seleccionadas.size() != correctas.size()) {
            return false;
        }
        for (String seleccionada : seleccionadas) {
            if (!correctas.contains(seleccionada)) {
                return false;
            }
        }

        return true;
    }

    // Método para avanzar al siguiente ejercicio
    private void avanzarSiguienteEjercicio() {
        ejercicioActual++;

        if (ejercicioActual < totalEjercicios) {
            // Cargar el siguiente ejercicio
            cargarEjercicio(ejercicioActual);
        } else {
            // Todos los ejercicios completados
            System.out.println("¡Lección completada!");
            btnComprobar.setText("Finalizado");
            btnComprobar.setDisable(true);
            progressBar.setProgress(1.0);
            mostrarLeccionCompletada();
        }
    }

    private void mostrarLeccionCompletada() {
        try {
            int aciertos = contarAciertos();
            int xp = aciertos * 10;
            int total = totalEjercicios;
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Modulo_GestorEjercicios/Views/LeccionCompletada.fxml"));
            javafx.scene.Parent root = loader.load();
            // Pasar los datos al controller de la pantalla completada
            GestorEjercicios.Controllers.LeccionCompletadaController controller = loader.getController();
            controller.configurarResultados(xp, aciertos, total);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Lección Completada");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            // Cerrar la ventana actual
            btnComprobar.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cuenta la cantidad de respuestas correctas
    private int contarAciertos() {
        int aciertos = 0;
        for (boolean correcto : respuestasCorrectasUsuario) {
            if (correcto) aciertos++;
        }
        return aciertos;
    }

    // Método para actualizar el progress bar
    private void actualizarProgressBar() {
        double progreso = (double) (ejercicioActual + 1) / totalEjercicios;
        progressBar.setProgress(progreso);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar la lección al cargar el controller
        inicializarLeccion();

        // Inicializar el sistema de vidas
        actualizarVidasUI();

        // Configurar el panel de feedback inicialmente oculto
        if (feedbackPanel != null) {
            feedbackPanel.setVisible(false);
        }

        // Configurar el evento del botón comprobar
        btnComprobar.setOnAction(event -> handleComprobar());

        // Configurar el evento del botón siguiente
        if (btnSiguiente != null) {
            btnSiguiente.setOnAction(event -> handleContinuar());
        }
    }

    // Método para manejar el botón continuar
    @FXML
    private void handleContinuar() {
        if (vidasActuales <= 0) {
            // Mostrar alerta de Game Over y regresar a la pantalla de ruta
            MetodosFrecuentes.mostrarAlerta("Game Over", "Se han agotado las vidas. Regresando a la pantalla de ruta.");
            // Aquí se podría agregar lógica para cerrar la ventana actual o navegar a la ruta
            // No reiniciamos el juego, simplemente mostramos la alerta
        } else {
            // Continuar al siguiente ejercicio
            feedbackPanel.setVisible(false);
            btnComprobar.setVisible(false); // Ocultar hasta que se haga una nueva selección
            avanzarSiguienteEjercicio();
        }
    }

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        vidasActuales = VIDAS_MAXIMAS;
        ejercicioActual = 0;
        respuestasCorrectasUsuario.clear();
        actualizarVidasUI();
        
        // Ocultar feedback y mostrar comprobar
        feedbackPanel.setVisible(false);
        btnComprobar.setVisible(true);
        if (btnSiguiente != null) {
            btnSiguiente.setText("SIGUIENTE");
            btnSiguiente.setDisable(true);
            btnSiguiente.setOpacity(0.0);
        }
        
        // Resetear modo comprobar
        btnComprobar.setText("COMPROBAR");
        
        // Cargar el primer ejercicio
        if (totalEjercicios > 0) {
            cargarEjercicio(0);
        }
        
        // Resetear progress bar
        progressBar.setProgress(0.0);
    }
}