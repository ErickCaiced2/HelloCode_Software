package GestorEjercicios.strategy;

import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;  // Importar EjercicioSeleccion

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionPrueba implements EstrategiaLeccion {
    private List<EjercicioSeleccion> ejerciciosPendientes = new ArrayList<>();
    private List<EjercicioSeleccion> ejerciciosFallidos = new ArrayList<>();
    private boolean leccionCompletada = false;
    private static final int MAX_EJERCICIOS_POR_PRUEBA = 10;

    @Override
    public Leccion crearLeccion(String nombre, List<EjercicioSeleccion> ejercicios) {
        // Limitar la cantidad de ejercicios para pruebas
        List<EjercicioSeleccion> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_PRUEBA) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_PRUEBA);
        }

        // Crear la lección directamente con EjercicioSeleccion
        return new Leccion((int) (Math.random() * 1000), nombre, ejerciciosLimitados, GestorEjercicios.enums.TipoLeccion.PRUEBA, 30, 0);
    }

    @Override
    public List<EjercicioSeleccion> obtenerEjerciciosPendientes() {
        if (!leccionCompletada) {
            return new ArrayList<>(ejerciciosPendientes);
        } else {
            // Al final de la lección, devolvemos los ejercicios fallidos
            return new ArrayList<>(ejerciciosFallidos);
        }
    }

    @Override
    public boolean tieneEjerciciosPendientes() {
        if (!leccionCompletada) {
            return !ejerciciosPendientes.isEmpty();
        } else {
            return !ejerciciosFallidos.isEmpty();
        }
    }

    public void marcarLeccionCompletada() {
        leccionCompletada = true;
        ejerciciosPendientes.clear(); // Limpiamos los ejercicios originales
    }

    public int obtenerCantidadEjerciciosFallidos() {
        return ejerciciosFallidos.size();
    }

    public void limpiarEjerciciosFallidos() {
        ejerciciosFallidos.clear();
    }

}
