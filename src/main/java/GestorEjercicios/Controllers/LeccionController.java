package GestorEjercicios.Controllers;

import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.Lenguaje;
import Modulo_Ejercicios.exercises.NivelDificultad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class LeccionController {
    @FXML private Button btnPython;
    @FXML private Button btnJava;
    @FXML private Button btnCpp; // Si tienes botón para C
    @FXML private Button btnPhp; // Si tienes botón para PHP
    @FXML private Button btnBasico;
    @FXML private Button btnIntermedio;
    @FXML private Button btnAvanzado;
    @FXML private Button btnContinuar;

    private Lenguaje lenguajeSeleccionado;
    private NivelDificultad dificultadSeleccionada;

    @FXML
    public void initialize() {
        configurarBotonesLenguaje();
        configurarBotonesDificultad();
        configurarBotonContinuar();
    }

    private void configurarBotonesLenguaje() {
        btnPython.setOnAction(e -> seleccionarLenguaje(Lenguaje.PYTHON));
        btnJava.setOnAction(e -> seleccionarLenguaje(Lenguaje.JAVA));
        if (btnCpp != null) btnCpp.setOnAction(e -> seleccionarLenguaje(Lenguaje.C));
        if (btnPhp != null) btnPhp.setOnAction(e -> seleccionarLenguaje(Lenguaje.PHP));
    }

    private void configurarBotonesDificultad() {
        btnBasico.setOnAction(e -> seleccionarDificultad(NivelDificultad.BASICO));
        btnIntermedio.setOnAction(e -> seleccionarDificultad(NivelDificultad.INTERMEDIO));
        btnAvanzado.setOnAction(e -> seleccionarDificultad(NivelDificultad.AVANZADO));
    }

    private void configurarBotonContinuar() {
        btnContinuar.setOnAction(e -> crearYMostrarLeccion());
    }

    private void seleccionarLenguaje(Lenguaje lenguaje) {
        this.lenguajeSeleccionado = lenguaje;
        resetearEstilosBotonesLenguaje();
        resaltarBotonSeleccionado(lenguaje);
        System.out.println("Lenguaje seleccionado: " + lenguaje);
    }

    private void seleccionarDificultad(NivelDificultad dificultad) {
        this.dificultadSeleccionada = dificultad;
        resetearEstilosBotonesDificultad();
        resaltarBotonDificultadSeleccionado(dificultad);
        System.out.println("Dificultad seleccionada: " + dificultad);
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

    private void cerrarVentanaActual() {
        Stage currentStage = (Stage) btnContinuar.getScene().getWindow();
        currentStage.close();
    }

    // Método público para configurar lección con valores predefinidos
    public void configurarLeccionConValores(Lenguaje lenguaje, NivelDificultad nivel) {
        this.lenguajeSeleccionado = lenguaje;
        this.dificultadSeleccionada = nivel;
        actualizarEstilosBotones();
        crearYMostrarLeccion();
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
