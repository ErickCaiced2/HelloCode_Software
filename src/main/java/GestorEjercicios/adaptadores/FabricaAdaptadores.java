package GestorEjercicios.adaptadores;

import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;

/**
 * Fábrica para crear adaptadores de ejercicios
 * Permite manejar diferentes tipos de ejercicios de manera uniforme
 */
public class FabricaAdaptadores {
    
    /**
     * Crea un adaptador para un ejercicio de selección múltiple
     * @param ejercicio Ejercicio de selección múltiple
     * @return Adaptador para el ejercicio
     */
    public static AdaptadorEjercicios crearAdaptador(EjercicioSeleccion ejercicio) {
        return new AdaptadorEjercicioSeleccion(ejercicio);
    }
    
    /**
     * Crea un adaptador para un ejercicio de completar código
     * @param ejercicio Ejercicio de completar código
     * @return Adaptador para el ejercicio
     */
    public static AdaptadorEjercicios crearAdaptador(EjercicioCompletarCodigo ejercicio) {
        return new AdaptadorEjercicioCompletarCodigo(ejercicio);
    }
    
    /**
     * Crea un adaptador basado en el tipo de objeto
     * @param ejercicio Objeto ejercicio (puede ser EjercicioSeleccion, EjercicioCompletarCodigo, etc.)
     * @return Adaptador para el ejercicio
     * @throws IllegalArgumentException si el tipo de ejercicio no es soportado
     */
    public static AdaptadorEjercicios crearAdaptador(Object ejercicio) {
        if (ejercicio instanceof EjercicioSeleccion) {
            return crearAdaptador((EjercicioSeleccion) ejercicio);
        } else if (ejercicio instanceof EjercicioCompletarCodigo) {
            return crearAdaptador((EjercicioCompletarCodigo) ejercicio);
        } else if (ejercicio instanceof AdaptadorEjercicios) {
            return (AdaptadorEjercicios) ejercicio;
        } else {
            throw new IllegalArgumentException("Tipo de ejercicio no soportado: " + 
                (ejercicio != null ? ejercicio.getClass().getSimpleName() : "null"));
        }
    }
} 