package ModuloNuevo;

import Modulo_Ejercicios.exercises.EjercicioBase;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Usuario.Clases.Usuario;

import java.util.ArrayList;

public class Leccion {

    private final Usuario usuario;
    private final ArrayList<EjercicioBase> ejercicios;

    public Leccion(Usuario usuario1, ArrayList<EjercicioSeleccion> ejerciciosSeleccion, ArrayList<EjercicioCompletarCodigo> ejerciciosCompletar) {
        this.usuario = usuario1;
        this.ejercicios = new ArrayList<>();
        this.ejercicios.addAll(ejerciciosSeleccion);
        this.ejercicios.addAll(ejerciciosCompletar);
    }

    public int getNumeroEjercicios() {
        return ejercicios.size();
    }

    public EjercicioBase getEjercicio(int i) {
        return ejercicios.get(i);
    }
}
