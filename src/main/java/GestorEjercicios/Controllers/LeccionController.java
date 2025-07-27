package GestorEjercicios.Controllers;

import GestorEjercicios.model.GestorLecciones;
import GestorEjercicios.strategy.EstrategiaLeccion;
import GestorEjercicios.strategy.FabricaEstrategiasLeccion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.Lenguaje;
import Modulo_Ejercicios.exercises.NivelDificultad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class LeccionController {
    @FXML private Button btnPython;
    @FXML private Button btnJava;
    @FXML private Button btnCpp;
    @FXML private Button btnPhp;
    @FXML private Button btnBasico;
    @FXML private Button btnIntermedio;
    @FXML private Button btnAvanzado;
    @FXML private Button btnContinuar;
    @FXML private ComboBox<String> comboTipoLeccion;

    private Lenguaje lenguajeSeleccionado;
    private NivelDificultad dificultadSeleccionada;

    // Instancia del GestorLecciones
    private GestorLecciones gestorLecciones;

    @FXML
    public void initialize() {
        // Inicializar el gestor de lecciones
        gestorLecciones = new GestorLecciones();

        configurarBotonesLenguaje();
        configurarBotonesDificultad();
        configurarComboBoxTipoLeccion();
        configurarBotonContinuar();
    }

    private void configurarBotonesLenguaje() {
        btnPython.setOnAction(e -> seleccionarLenguaje(Lenguaje.PYTHON));
        btnJava.setOnAction(e -> seleccionarLenguaje(Lenguaje.JAVA));
        if (btnCpp != null) btnCpp.setOnAction(e -> seleccionarLenguaje(Lenguaje.C));
        if (btnPhp != null) btnPhp.setOnAction(e -> seleccionarLenguaje(Lenguaje.PHP));
    }

    private void configurarBotonContinuar() {
        btnContinuar.setOnAction(e -> crearYMostrarLeccion());
    }

    private void configurarBotonesDificultad() {
        btnBasico.setOnAction(e -> seleccionarDificultad(NivelDificultad.BASICO));
        btnIntermedio.setOnAction(e -> seleccionarDificultad(NivelDificultad.INTERMEDIO));
        btnAvanzado.setOnAction(e -> seleccionarDificultad(NivelDificultad.AVANZADO));
    }

    private void configurarComboBoxTipoLeccion() {
        comboTipoLeccion.getItems().addAll("Normal", "Diagnóstico", "Prueba");
        comboTipoLeccion.setOnAction(e -> seleccionarTipoLeccion());
    }

    private void seleccionarTipoLeccion() {
        String tipoSeleccionado = comboTipoLeccion.getValue();
        TipoLeccion tipoLeccion = null;

        switch (tipoSeleccionado) {
            case "Normal":
                tipoLeccion = TipoLeccion.NORMAL;
                break;
            case "Diagnóstico":
                tipoLeccion = TipoLeccion.DIAGNOSTICO;
                break;
            case "Prueba":
                tipoLeccion = TipoLeccion.PRUEBA;
                break;
            default:
                System.out.println("Tipo de lección no válido");
                return;
        }

        // Crear la estrategia correspondiente usando la fábrica
        EstrategiaLeccion estrategia = FabricaEstrategiasLeccion.crearEstrategia(tipoLeccion);

        // Obtener los ejercicios de la base de datos (List<EjercicioSeleccion>)
        List<EjercicioSeleccion> ejercicios = EjercicioRepository.cargarEjerciciosSeleccion().stream()
                .filter(e -> e.getLenguaje() == lenguajeSeleccionado && e.getNivel() == dificultadSeleccionada)
                .collect(Collectors.toList());

        // Crear la lección con la lista de EjercicioSeleccion y valores de experiencia/conocimiento
        int experiencia = 0, conocimiento = 0;
        switch (tipoLeccion) {
            case NORMAL:
                experiencia = 15;
                conocimiento = 5;
                break;
            case PRUEBA:
                experiencia = 30;
                conocimiento = 0;
                break;
            case DIAGNOSTICO:
                experiencia = 0;
                conocimiento = 0; // Se calcula al finalizar
                break;
        }
        Leccion leccion = new Leccion((int) (Math.random() * 1000), "Lección de " + tipoSeleccionado + " - " + dificultadSeleccionada + " - " + lenguajeSeleccionado, ejercicios, tipoLeccion, experiencia, conocimiento);

        // Agregar la lección al gestor
        gestorLecciones.agregarLeccion(leccion);

        // Imprimir todas las lecciones para verificar que la lección se agregó correctamente
        gestorLecciones.imprimirLecciones();
        // Mostrar la lección
        mostrarLeccion(leccion);
    }

    private void seleccionarLenguaje(Lenguaje lenguaje) {
        this.lenguajeSeleccionado = lenguaje;
        resetearEstilosBotonesLenguaje();
        resaltarBotonSeleccionado(lenguaje);
    }

    private void seleccionarDificultad(NivelDificultad dificultad) {
        this.dificultadSeleccionada = dificultad;
        resetearEstilosBotonesDificultad();
        resaltarBotonDificultadSeleccionado(dificultad);
    }

    private void resetearEstilosBotonesLenguaje() {
        btnPython.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnJava.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        if (btnCpp != null) btnCpp.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        if (btnPhp != null) btnPhp.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
    }

    private void resetearEstilosBotonesDificultad() {
        btnBasico.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnIntermedio.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnAvanzado.setStyle("-fx-background-color: white; -fx-text-fill: #1a237e; -fx-font-weight: bold; -fx-background-radius: 10;");
    }

    private void resaltarBotonSeleccionado(Lenguaje lenguaje) {
        String estiloSeleccionado = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;";
        switch (lenguaje) {
            case PYTHON:
                btnPython.setStyle(estiloSeleccionado);
                break;
            case JAVA:
                btnJava.setStyle(estiloSeleccionado);
                break;
            case C:
                if (btnCpp != null) btnCpp.setStyle(estiloSeleccionado);
                break;
            case PHP:
                if (btnPhp != null) btnPhp.setStyle(estiloSeleccionado);
                break;
        }
    }

    private void resaltarBotonDificultadSeleccionado(NivelDificultad dificultad) {
        String estiloSeleccionado = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;";
        switch (dificultad) {
            case BASICO:
                btnBasico.setStyle(estiloSeleccionado);
                break;
            case INTERMEDIO:
                btnIntermedio.setStyle(estiloSeleccionado);
                break;
            case AVANZADO:
                btnAvanzado.setStyle(estiloSeleccionado);
                break;
        }
    }

    private void mostrarLeccion(Leccion leccion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));
            Parent root = loader.load();
            Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller = loader.getController();
            // Convertir la lista de AdaptadorEjercicios a EjercicioSeleccion
            List<EjercicioSeleccion> ejerciciosSeleccion = leccion.getEjercicios().stream()
                    .filter(e -> e instanceof GestorEjercicios.adaptadores.AdaptadorEjercicioSeleccion)
                    .map(e -> ((GestorEjercicios.adaptadores.AdaptadorEjercicioSeleccion) e).obtenerEjercicioOriginal())
                    .collect(Collectors.toList());
            controller.setEjercicios(ejerciciosSeleccion);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ejercicios Selección Múltiple");
            stage.show();
            cerrarVentanaActual();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnContinuar.getScene().getWindow();
        currentStage.close();
    }

    public void configurarLeccionConValores(Lenguaje lenguaje, NivelDificultad nivel) {
        this.lenguajeSeleccionado = lenguaje;
        this.dificultadSeleccionada = nivel;
        actualizarEstilosBotones();
        crearYMostrarLeccion();
    }

    // Método para continuar y crear la lección
    private void crearYMostrarLeccion() {
        if (lenguajeSeleccionado == null || dificultadSeleccionada == null) {
            System.out.println("Por favor selecciona lenguaje y dificultad.");
            return;
        }

        List<EjercicioSeleccion> ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion().stream()
                .filter(e -> e.getLenguaje() == lenguajeSeleccionado && e.getNivel() == dificultadSeleccionada)
                .collect(Collectors.toList());

        if (ejerciciosSeleccion.isEmpty()) {
            System.out.println("No hay ejercicios de selección múltiple para los criterios seleccionados.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));
            Parent root = loader.load();
            Modulo_Ejercicios.Controladores.EjercicioSeleccionController controller = loader.getController();
            controller.setEjercicios(ejerciciosSeleccion);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ejercicios Selección Múltiple");
            stage.show();
            cerrarVentanaActual();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void actualizarEstilosBotones() {
        limpiarEstilosBotones();
        if (lenguajeSeleccionado != null) {
            resaltarBotonSeleccionado(lenguajeSeleccionado);
        }
        if (dificultadSeleccionada != null) {
            resaltarBotonDificultadSeleccionado(dificultadSeleccionada);
        }
    }

    private void limpiarEstilosBotones() {
        resetearEstilosBotonesLenguaje();
        resetearEstilosBotonesDificultad();
    }
}
