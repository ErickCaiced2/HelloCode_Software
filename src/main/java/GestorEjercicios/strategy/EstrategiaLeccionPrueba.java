package GestorEjercicios.strategy;

import GestorEjercicios.model.DetalleLeccion;
import GestorEjercicios.model.Ejercicio;
import GestorEjercicios.model.Leccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionPrueba implements EstrategiaLeccion {
    private List<Ejercicio> ejerciciosPendientes = new ArrayList<>();
    private List<Ejercicio> ejerciciosFallidos = new ArrayList<>();
    private boolean leccionCompletada = false;
    private static final int MAX_EJERCICIOS_POR_PRUEBA = 10;

    @Override
    public Leccion crearLeccion(String nombre, List<Ejercicio> ejercicios) {
        List<DetalleLeccion> detalles = new ArrayList<>();
        
        // Limitar la cantidad de ejercicios para pruebas
        List<Ejercicio> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_PRUEBA) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_PRUEBA);
        }
        
        for (Ejercicio ejercicio : ejerciciosLimitados) {
            detalles.add(new DetalleLeccion(ejercicio, null));
        }
        
        System.out.println("Prueba '" + nombre + "' creada con " + detalles.size() + " ejercicios");
        return new Leccion((int) (Math.random() * 1000), nombre, detalles);
    }

    @Override
    public void procesarRespuesta(DetalleLeccion detalle, String respuestaUsuario) {
        detalle.resolverEjercicio(respuestaUsuario);
        
        // Si la respuesta es incorrecta, agregamos el ejercicio a la lista de fallidos
        if (!detalle.getResultado().isCorrecto()) {
            ejerciciosFallidos.add(detalle.getEjercicio());
        }
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPendientes() {
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

    public Leccion crearLeccionRepaso(String nombre) {
        if (ejerciciosFallidos.isEmpty()) {
            return null; // No hay ejercicios para repasar
        }
        
        List<DetalleLeccion> detalles = new ArrayList<>();
        for (Ejercicio ejercicio : ejerciciosFallidos) {
            detalles.add(new DetalleLeccion(ejercicio, null));
        }
        
        return new Leccion((int) (Math.random() * 1000), nombre + " - Repaso", detalles);
    }
} 