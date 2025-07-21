package GestorEjercicios.strategy;

import GestorEjercicios.model.DetalleLeccion;
import GestorEjercicios.model.Ejercicio;
import GestorEjercicios.model.Leccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaLeccionDiagnostico implements EstrategiaLeccion {
    private List<Ejercicio> ejerciciosPendientes = new ArrayList<>();
    private int ejerciciosCorrectos = 0;
    private int totalEjercicios = 0;
    private static final int MAX_EJERCICIOS_POR_DIAGNOSTICO = 8;

    @Override
    public Leccion crearLeccion(String nombre, List<Ejercicio> ejercicios) {
        List<DetalleLeccion> detalles = new ArrayList<>();
        
        // Limitar la cantidad de ejercicios para diagnóstico
        List<Ejercicio> ejerciciosLimitados = new ArrayList<>(ejercicios);
        if (ejerciciosLimitados.size() > MAX_EJERCICIOS_POR_DIAGNOSTICO) {
            // Mezclar los ejercicios para que no siempre sean los mismos
            Collections.shuffle(ejerciciosLimitados);
            ejerciciosLimitados = ejerciciosLimitados.subList(0, MAX_EJERCICIOS_POR_DIAGNOSTICO);
        }
        
        for (Ejercicio ejercicio : ejerciciosLimitados) {
            detalles.add(new DetalleLeccion(ejercicio, null));
        }
        
        totalEjercicios = detalles.size();
        System.out.println("Diagnóstico '" + nombre + "' creado con " + totalEjercicios + " ejercicios");
        return new Leccion((int) (Math.random() * 1000), nombre, detalles);
    }

    @Override
    public void procesarRespuesta(DetalleLeccion detalle, String respuestaUsuario) {
        detalle.resolverEjercicio(respuestaUsuario);
        if (detalle.getResultado().isCorrecto()) {
            ejerciciosCorrectos++;
        }
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPendientes() {
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