package Modulo_Ejercicios.Controladores;

import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.otrosModulos.Leccion;
import javafx.stage.StageStyle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EjercicioSeleccionController implements Initializable {

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
    private Label lblCantidadR;

    @FXML
    private Button btnComprobar;

    // Contenedor dinámico para los botones de opciones
    @FXML
    private VBox opcionesContainer;

    // Lista para almacenar los botones generados dinámicamente
    private List<Button> botonesOpciones = new ArrayList<>();

    // Lista para almacenar las opciones seleccionadas
    private List<String> opcionesSeleccionadas = new ArrayList<>();

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

        System.out.println("Opciones seleccionadas: " + opcionesSeleccionadas);
    }

    // Método para obtener las opciones seleccionadas
    public List<String> getOpcionesSeleccionadas() {
        return new ArrayList<>(opcionesSeleccionadas);
    }

    // Método para limpiar selecciones
    public void limpiarSelecciones() {
        opcionesSeleccionadas.clear();
        for (Button btn : botonesOpciones) {
            btn.getStyleClass().clear();
            btn.getStyleClass().add("boton-opcion");
        }
    }

    // Método para permitir solo selección única
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
                });
            }
        }
    }


    // Método para inicializar la lección y ejercicios
    private void inicializarLeccion() {
        // Crear instancia de la lección
        leccionTiposDatos = new Leccion("Tipos de Datos");

        // Cargar ejercicios desde el repositorio
        ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion();

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

            // Mostrar información sobre respuestas correctas (opcional)
            int numRespuestasCorrectas = ejercicio.obtenerRespuestasCorrectas().size();
            lblCantidadR.setText("Respuestas correctas: " + numRespuestasCorrectas);

            // Resetear el modo a comprobar
            modoComprobar = true;
            btnComprobar.setText("Comprobar");

            System.out.println("Ejercicio " + (indice + 1) + " cargado. Opciones: " + opciones.size() +
                    ", Respuestas correctas: " + numRespuestasCorrectas);
        }
    }

    // Método para manejar el click del botón Comprobar/Siguiente
    @FXML
    private void handleComprobar() {
        if (modoComprobar) {
            // Modo comprobar: validar respuestas
            validarRespuestas();
        } else {
            // Modo siguiente: avanzar al siguiente ejercicio
            avanzarSiguienteEjercicio();
        }
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

            // Verificar si las opciones seleccionadas coinciden con las correctas
            boolean esCorrecta = validarSeleccion(opcionesSeleccionadas, respuestasCorrectas);

            // Actualizar colores de todos los botones según el resultado
            actualizarColoresBotones(respuestasCorrectas);

            if (esCorrecta) {
                System.out.println("¡Respuesta correcta!");
            } else {
                System.out.println("Respuesta incorrecta. Respuestas correctas: " + respuestasCorrectas);
            }

            // Cambiar el botón a modo "Siguiente"
            modoComprobar = false;
            btnComprobar.setText("Siguiente");

            actualizarProgressBar();
        }
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
        }
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


        // Configurar el evento del botón comprobar
        btnComprobar.setOnAction(event -> handleComprobar());
    }
}