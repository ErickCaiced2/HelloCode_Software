package GestionAprendizaje_Modulo.Ruta;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private String id;
    private String nombre;
    private String descripcion;
    private List<NodoRuta> nodos;

    public Ruta(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nodos = new ArrayList<>();
    }

    public void agregarNodo(NodoRuta nodo) {
        nodos.add(nodo);
    }

    public double obtenerProgreso() {
        if (nodos.isEmpty()) return 0.0;
        long completados = nodos.stream().filter(NodoRuta::estaCompletado).count();
        return (completados * 100.0) / nodos.size();
    }

    public List<NodoRuta> getNodos() {
        return nodos;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "nombre='" + nombre + '\'' +
                ", progreso=" + obtenerProgreso() + "%" +
                '}';
    }

    public String getNombre() {
        return nombre;
    }
}
