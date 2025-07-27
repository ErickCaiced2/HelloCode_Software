package GestorEjercicios.strategy;

import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;  // Importar EjercicioSeleccion

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionNormal implements EstrategiaLeccion {
    private static final int MAX_EJERCICIOS_POR_LECCION = 5;
    private List<EjercicioSeleccion> ejerciciosPendientes = new ArrayList<>();

    @Override
    public Leccion crearLeccion(String nombre, List<EjercicioSeleccion> ejercicios) {
        // Limitar la cantidad de ejercicios a un máximo de 5
        List<EjercicioSeleccion> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_LECCION) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_LECCION);
        }

        // Crear la lección directamente con los ejercicios seleccionados
        return new Leccion((int) (Math.random() * 1000), nombre, ejerciciosLimitados, GestorEjercicios.enums.TipoLeccion.NORMAL, 15, 5);
    }


    @Override
    public List<EjercicioSeleccion> obtenerEjerciciosPendientes() {
        return new ArrayList<>(ejerciciosPendientes);
    }

    @Override
    public boolean tieneEjerciciosPendientes() {
        return !ejerciciosPendientes.isEmpty();
    }
}
