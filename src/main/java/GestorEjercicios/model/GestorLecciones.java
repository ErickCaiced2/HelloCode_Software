package GestorEjercicios.model;

import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.filtros.FiltroEjercicio;
import GestorEjercicios.strategy.EstrategiaLeccion;
import GestorEjercicios.strategy.FabricaEstrategiasLeccion;

import java.util.List;
import java.util.stream.Collectors;

public class GestorLecciones {
    
    public Leccion generarLeccionPersonalizada(String nombre, List<FiltroEjercicio> filtros, List<Ejercicio> disponibles) {
        return generarLeccionPersonalizada(nombre, filtros, disponibles, TipoLeccion.NORMAL);
    }
    
    public Leccion generarLeccionPersonalizada(String nombre, List<FiltroEjercicio> filtros, List<Ejercicio> disponibles, TipoLeccion tipo) {
        GestorEjercicios gestor = new GestorEjercicios();
        disponibles.forEach(gestor::agregarEjercicio);
        List<Ejercicio> filtrados = gestor.aplicarFiltros(filtros);

        EstrategiaLeccion estrategia = FabricaEstrategiasLeccion.crearEstrategia(tipo);
        Leccion leccion = estrategia.crearLeccion(nombre, filtrados);
        
        // Convertir los ejercicios filtrados a detalles de lección
        List<DetalleLeccion> detalles = filtrados.stream()
                .map(e -> new DetalleLeccion(e, null))
                .collect(Collectors.toList());
        
        return new Leccion((int) (Math.random() * 1000), nombre, detalles, tipo);
    }
    
    public Leccion generarLeccionDiagnostico(String nombre, List<Ejercicio> ejercicios) {
        return generarLeccionPersonalizada(nombre, List.of(), ejercicios, TipoLeccion.DIAGNOSTICO);
    }
    
    public Leccion generarLeccionPrueba(String nombre, List<Ejercicio> ejercicios) {
        return generarLeccionPersonalizada(nombre, List.of(), ejercicios, TipoLeccion.PRUEBA);
    }
    
    public Leccion generarLeccionNormal(String nombre, List<Ejercicio> ejercicios) {
        return generarLeccionPersonalizada(nombre, List.of(), ejercicios, TipoLeccion.NORMAL);
    }
}