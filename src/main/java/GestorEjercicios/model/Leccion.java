package GestorEjercicios.model;

import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.strategy.EstrategiaLeccion;
import GestorEjercicios.strategy.EstrategiaLeccionPrueba;
import GestorEjercicios.strategy.FabricaEstrategiasLeccion;

import java.util.List;

public class Leccion {
    private int id;
    private String nombre;
    private List<DetalleLeccion> ejerciciosResueltos;
    private TipoLeccion tipo;
    private EstrategiaLeccion estrategia;

    public Leccion(int id, String nombre, List<DetalleLeccion> ejerciciosResueltos) {
        this.id = id;
        this.nombre = nombre;
        this.ejerciciosResueltos = ejerciciosResueltos;
        this.tipo = TipoLeccion.NORMAL; // Por defecto
        this.estrategia = FabricaEstrategiasLeccion.crearEstrategia(this.tipo);
    }

    public Leccion(int id, String nombre, List<DetalleLeccion> ejerciciosResueltos, TipoLeccion tipo) {
        this.id = id;
        this.nombre = nombre;
        this.ejerciciosResueltos = ejerciciosResueltos;
        this.tipo = tipo;
        this.estrategia = FabricaEstrategiasLeccion.crearEstrategia(this.tipo);
    }

    public List<DetalleLeccion> getEjerciciosResueltos() {
        return ejerciciosResueltos;
    }

    public String obtenerResumen() {
        return "Lección '" + nombre + "' (" + tipo + ") con " + ejerciciosResueltos.size() + " ejercicios.";
    }

    public void procesarRespuesta(int indiceEjercicio, String respuestaUsuario) {
        if (indiceEjercicio >= 0 && indiceEjercicio < ejerciciosResueltos.size()) {
            DetalleLeccion detalle = ejerciciosResueltos.get(indiceEjercicio);
            estrategia.procesarRespuesta(detalle, respuestaUsuario);
        }
    }

    public TipoLeccion getTipo() {
        return tipo;
    }

    public EstrategiaLeccion getEstrategia() {
        return estrategia;
    }

    public boolean tieneEjerciciosPendientes() {
        return estrategia.tieneEjerciciosPendientes();
    }

    public List<Ejercicio> obtenerEjerciciosPendientes() {
        return estrategia.obtenerEjerciciosPendientes();
    }

    public void marcarCompletada() {
        if (tipo == TipoLeccion.PRUEBA) {
            ((EstrategiaLeccionPrueba) estrategia).marcarLeccionCompletada();
        }
    }

    public Leccion crearLeccionRepaso(String nombreRepaso) {
        if (tipo == TipoLeccion.PRUEBA) {
            return ((EstrategiaLeccionPrueba) estrategia).crearLeccionRepaso(nombreRepaso);
        }
        return null;
    }
}