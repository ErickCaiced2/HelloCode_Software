package GestorEjercicios.strategy;

import GestorEjercicios.model.DetalleLeccion;
import GestorEjercicios.model.Ejercicio;
import GestorEjercicios.model.Leccion;

import java.util.List;

public interface EstrategiaLeccion {
    Leccion crearLeccion(String nombre, List<Ejercicio> ejercicios);
    void procesarRespuesta(DetalleLeccion detalle, String respuestaUsuario);
    List<Ejercicio> obtenerEjerciciosPendientes();
    boolean tieneEjerciciosPendientes();
} 