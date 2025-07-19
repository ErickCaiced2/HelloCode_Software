package GestionAprendizaje_Modulo.Ruta;

import java.util.ArrayList;
import java.util.List;

import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;

public class NodoRuta {
    private int orden;
    private boolean completado;
    private Leccion leccion;

    // --- AÑADIDO: El contenedor para tu material de apoyo ---
    // Cada nodo ahora puede tener su propia lista de recursos.
    // La hacemos 'final' porque la lista en sí no cambiará, solo su contenido.
    private final List<RecursoAprendizaje> materialDeApoyo;

    public NodoRuta(int orden, Leccion leccion) {
        this.orden = orden;
        this.leccion = leccion;
        this.completado = false;

        // --- AÑADIDO: Inicializar la lista en el constructor ---
        // Esto es crucial. Cada vez que se crea un NodoRuta,
        // se crea también su lista de materiales, vacía y lista para usar.
        this.materialDeApoyo = new ArrayList<>();
    }

    // --- AÑADIDO: Método para que tu módulo pueda AGREGAR recursos ---
    // Este es el método que tu 'main' necesita llamar.
    public void agregarMaterialDeApoyo(RecursoAprendizaje recurso) {
        this.materialDeApoyo.add(recurso);
    }

    // --- AÑADIDO: Método para que puedas LEER qué material ya existe ---
    // Este es el método 'getMaterialDeApoyo()' que faltaba.
    public List<RecursoAprendizaje> getMaterialDeApoyo() {
        return this.materialDeApoyo;
    }


    // ===================================================
    // El resto de tus métodos se mantienen exactamente igual
    // ===================================================

    public void marcarComoCompletado() {
        this.completado = true;
    }

    public Leccion getLeccion() {
        return leccion;
    }

    public boolean estaCompletado() {
        return completado;
    }

    public int getOrden() {
        return orden;
    }

    @Override
    public String toString() {
        return "NodoRuta{" +
                "orden=" + orden +
                ", completado=" + completado +
                ", leccionTest=" + leccion.getTitulo() +
                '}';
    }
}