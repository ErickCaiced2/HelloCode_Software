package GestorEjercicios.strategy;

import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;  // Importar EjercicioSeleccion

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionDiagnostico implements EstrategiaLeccion {
    private List<EjercicioSeleccion> ejerciciosPendientes = new ArrayList<>();
    private int ejerciciosCorrectos = 0;
    private int totalEjercicios = 0;
    private static final int MAX_EJERCICIOS_POR_DIAGNOSTICO = 8;

    @Override
    public Leccion crearLeccion(String nombre, List<EjercicioSeleccion> ejercicios) {
        // Limitamos la cantidad de ejercicios para diagnóstico
        List<EjercicioSeleccion> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_DIAGNOSTICO) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_DIAGNOSTICO);
        }

        // Aquí se está creando la lección con los ejercicios limitados
        List<EjercicioSeleccion> ejerciciosSeleccionados = new ArrayList<>(ejerciciosLimitados);

        totalEjercicios = ejerciciosSeleccionados.size();

        // Crear la lección con los ejercicios seleccionados
        return new Leccion((int) (Math.random() * 1000), nombre, ejerciciosSeleccionados, GestorEjercicios.enums.TipoLeccion.DIAGNOSTICO, 0, 0);
    }



    @Override
    public List<EjercicioSeleccion> obtenerEjerciciosPendientes() {
        return new ArrayList<>(ejerciciosPendientes);
    }

    @Override
    public boolean tieneEjerciciosPendientes() {
        return !ejerciciosPendientes.isEmpty();
    }

    public double obtenerPorcentajeAcierto() {
        if (totalEjercicios == 0) return 0.0;
        return (ejerciciosCorrectos * 100.0) / totalEjercicios;
    }

    public String obtenerNivelUsuario() {
        double porcentaje = obtenerPorcentajeAcierto();
        if (porcentaje >= 80) return "Avanzado";
        else if (porcentaje >= 60) return "Intermedio";
        else return "Principiante";
    }
}
