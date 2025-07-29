package GestorEjercicios.strategy;

import GestorEjercicios.config.ConfiguracionSistema;
import GestorEjercicios.services.LoggingService;
import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estrategia para lecciones normales de aprendizaje
 * REFACTORIZADO: Usa configuración centralizada y logging profesional
 */
public class EstrategiaLeccionNormal implements EstrategiaLeccion {
    private List<EjercicioSeleccion> ejerciciosPendientes = new ArrayList<>();

    @Override
    public Leccion crearLeccion(String nombre, List<EjercicioSeleccion> ejercicios) {
        int maxEjercicios = ConfiguracionSistema.getMaxEjerciciosPorLeccion();
        
        // Limitar la cantidad de ejercicios según configuración
        List<EjercicioSeleccion> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > maxEjercicios) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, maxEjercicios);
        }

        LoggingService.logLeccion("CREANDO", nombre, 
                                "Ejercicios: " + ejerciciosLimitados.size() + "/" + ejercicios.size());

        // Crear la lección directamente con los ejercicios seleccionados
        return new Leccion((int) (Math.random() * 1000), nombre, ejerciciosLimitados, 
                          GestorEjercicios.enums.TipoLeccion.NORMAL, 15, 5);
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
