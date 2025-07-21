package GestorEjercicios.strategy;

import GestorEjercicios.model.DetalleLeccion;
import GestorEjercicios.model.Ejercicio;
import GestorEjercicios.model.Leccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionNormal implements EstrategiaLeccion {
    private List<Ejercicio> ejerciciosPendientes = new ArrayList<>();
    private static final int MAX_EJERCICIOS_POR_LECCION = 5;

    @Override
    public Leccion crearLeccion(String nombre, List<Ejercicio> ejercicios) {
        List<DetalleLeccion> detalles = new ArrayList<>();
        
        // Limitar la cantidad de ejercicios a un máximo de 5
        List<Ejercicio> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_LECCION) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_LECCION);
        }
        
        for (Ejercicio ejercicio : ejerciciosLimitados) {
            detalles.add(new DetalleLeccion(ejercicio, null));
        }
        
        System.out.println("Lección '" + nombre + "' creada con " + detalles.size() + " ejercicios");
        return new Leccion((int) (Math.random() * 1000), nombre, detalles);
    }

    @Override
    public void procesarRespuesta(DetalleLeccion detalle, String respuestaUsuario) {
        detalle.resolverEjercicio(respuestaUsuario);
        // En lecciones normales, no acumulamos ejercicios pendientes
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPendientes() {
        return new ArrayList<>(ejerciciosPendientes);
    }

    @Override
    public boolean tieneEjerciciosPendientes() {
        return !ejerciciosPendientes.isEmpty();
    }
} 