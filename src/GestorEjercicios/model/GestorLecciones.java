package model;

import filtros.FiltroEjercicio;
import java.util.List;
import java.util.stream.Collectors;

public class GestorLecciones {
    public Leccion generarLeccionPersonalizada(String nombre, List<FiltroEjercicio> filtros, List<Ejercicio> disponibles) {
        GestorEjercicios gestor = new GestorEjercicios();
        disponibles.forEach(gestor::agregarEjercicio);
        List<Ejercicio> filtrados = gestor.aplicarFiltros(filtros);

        List<DetalleLeccion> detalles = filtrados.stream()
                .map(e -> new DetalleLeccion(e, new ResultadoEvaluacion(true, 100, 10, "Bien hecho", true)))
                .collect(Collectors.toList());

        return new Leccion((int) (Math.random() * 1000), nombre, detalles);
    }
}