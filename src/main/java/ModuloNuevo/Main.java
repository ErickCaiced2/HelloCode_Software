package ModuloNuevo;

import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Usuario.Clases.Usuario;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

    }

    public static void main(String[] args) {

        //Acoplamiento LOGICA Ejercicio __________________________

        List<EjercicioSeleccion> listaEjercicio = EjercicioRepository.cargarEjerciciosSeleccion(); //Generalizar

        ArrayList<EjercicioSeleccion> ejerciciosSeleccion = new ArrayList<>();
        ejerciciosSeleccion.add(listaEjercicio.get(1));
        ejerciciosSeleccion.add(listaEjercicio.get(2));

        List<EjercicioCompletarCodigo> listaEjercicioCompletar = EjercicioRepository.cargarEjerciciosCompletarCodigo();
        ArrayList<EjercicioCompletarCodigo> ejerciciosCompletar = new ArrayList<>();
        ejerciciosCompletar.add(listaEjercicioCompletar.get(1));
        ejerciciosCompletar.add(listaEjercicioCompletar.get(2));



        //_______________________________________________________



        //Acoplamiento GRAFICA Ejercicio SELECCION __________________________
        Usuario usuario1 = new Usuario("Mateo", "123456");
        Leccion leccion = new Leccion(usuario1, ejerciciosSeleccion, ejerciciosCompletar);
        //Visualizador.mostrarLeccion(leccion);

        //javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Modulo_Ejercicios/views/SeleccionMultiple-view.fxml"));





        //Acoplamiento GRAFICA Ejercicio SELECCION __________________________


    }

}
