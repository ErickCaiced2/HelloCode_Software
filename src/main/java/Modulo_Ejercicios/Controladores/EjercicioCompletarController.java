package Modulo_Ejercicios.Controladores;

import MetodosGlobales.MetodosFrecuentes;
import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.Respuesta;
import Modulo_Ejercicios.exercises.RespuestaString;
import Modulo_Ejercicios.exercises.ResultadoDeEvaluacion;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EjercicioCompletarController implements Initializable {

    @FXML
    private Text TexTipo;

    @FXML
    private Text TexVida;

    @FXML
    private ProgressBar ProgressBar;

    @FXML
    private Button btnComprobar;

    @FXML
    private TextField textEntrada;

    @FXML
    private TextArea Ejercicio;

    @FXML
    private Text TextInstruccion;

    @FXML
    private Label AvisoCorrecto;

    @FXML
    private Label AvisoIncorrecto;

    @FXML TextArea Avisos;

    @FXML Button btnSiguiente;

    private int vidas = 5;
    private int ejercicioActual = 0;
    private List<EjercicioCompletarCodigo> ejerciciosCompletarCodigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar ejercicios desde el repositorio
        ejerciciosCompletarCodigo = EjercicioRepository.cargarEjerciciosCompletarCodigo();
        cargarEjercicioActual();

        // Inicializamos el progreso en 0
        ProgressBar.setProgress(0);

        // Configurar la acción del botón Comprobar
        btnComprobar.setOnAction(event -> comprobarCodigo());

        // Configurar la acción del botón "Siguiente"
        btnSiguiente.setOnAction(event -> avanzarSiguienteEjercicio());
    }


    private void cargarEjercicioActual() {
        if (ejercicioActual < ejerciciosCompletarCodigo.size()) {
            EjercicioCompletarCodigo ejercicio = ejerciciosCompletarCodigo.get(ejercicioActual);

            // Mostrar instrucciones y código incompleto
            TextInstruccion.setText(ejercicio.getInstruccion());
            Ejercicio.setText(ejercicio.obtenerCodigoIncompleto());

            // Restablecer vidas para cada ejercicio
            //TexVida.setText(String.valueOf(vidas));
        }
    }

    // Método para comprobar el código ingresado
    private void comprobarCodigo() {
        String input = textEntrada.getText();
        ArrayList<Respuesta> respuestasUsuario = new ArrayList<>();

        // Añadir la respuesta del usuario
        respuestasUsuario.add(new RespuestaString(input));

        // Evaluar las respuestas
        EjercicioCompletarCodigo ejercicio = ejerciciosCompletarCodigo.get(ejercicioActual);
        ResultadoDeEvaluacion resultado = ejercicio.evaluarRespuestas(respuestasUsuario);

        if (resultado.getPorcentajeDeAcerto() == 100) {
            // Si la respuesta es correcta
            TexVida.setText(String.valueOf(vidas));
            AvisoCorrecto.setVisible(true);



        } else {
            // Si la respuesta es incorrecta
            if (vidas > 1) {
                vidas--;
                TexVida.setText(String.valueOf(vidas));
            } else {
                TexVida.setText("0");
                Avisos.setText("¡Se han agotado tus vidas!");
                Avisos.setVisible(true);
                PauseTransition pauseAvisos = new PauseTransition(Duration.seconds(2));
                pauseAvisos.setOnFinished(event -> Avisos.setVisible(false));
                pauseAvisos.play();
                terminarEjecucion();
                return;
            }

            AvisoIncorrecto.setVisible(true);


        }

        // Limpiar el TextField
        textEntrada.clear();
        //avanzarSiguienteEjercicio();

        btnSiguiente.setDisable(false);
        btnSiguiente.setOpacity(1.0);
    }

    // Método para avanzar al siguiente ejercicio
    private void avanzarSiguienteEjercicio() {

        AvisoCorrecto.setVisible(false);
        AvisoIncorrecto.setVisible(false);

        ejercicioActual++;

        // Actualizar la barra de progreso en función de los ejercicios completados
        double progreso = (double) ejercicioActual / ejerciciosCompletarCodigo.size();
        ProgressBar.setProgress(progreso);

        if (ejercicioActual < ejerciciosCompletarCodigo.size()) {
            cargarEjercicioActual();
        } else {
            // Si se completaron todos los ejercicios
            System.out.println("¡Todos los ejercicios completados!");
            btnComprobar.setDisable(true);

            MetodosFrecuentes.cambiarVentana((Stage) btnSiguiente.getScene().getWindow(), "/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml", "Menu Principal");
            //terminarEjecucion();
        }

        btnSiguiente.setDisable(true);
        btnSiguiente.setOpacity(0.0);

    }

    // Método para finalizar la ejecución
    private void terminarEjecucion() {
        btnComprobar.setDisable(true);


    }
}
