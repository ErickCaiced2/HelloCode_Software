package GestorEjercicios.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorLecciones {

    // Lista que almacena todas las lecciones creadas
    private List<Leccion> lecciones;

    // Constructor
    public GestorLecciones() {
        this.lecciones = new ArrayList<>();
    }

    // Método para agregar una nueva lección al arreglo
    public void agregarLeccion(Leccion leccion) {
        if (leccion != null) {
            lecciones.add(leccion);
            System.out.println("Lección '" + leccion.obtenerResumen() + "' agregada.");
        } else {
            System.out.println("No se puede agregar una lección nula.");
        }
    }

    // Método para obtener todas las lecciones
    public List<Leccion> obtenerLecciones() {
        return lecciones;
    }

    // Método para obtener una lección por su ID
    public Optional<Leccion> obtenerLeccionPorId(int id) {
        return lecciones.stream()
                .filter(leccion -> leccion.getId() == id)
                .findFirst();
    }

    // Método para eliminar una lección por su ID
    public boolean eliminarLeccionPorId(int id) {
        Optional<Leccion> leccion = obtenerLeccionPorId(id);
        if (leccion.isPresent()) {
            lecciones.remove(leccion.get());
            System.out.println("Lección con ID " + id + " eliminada.");
            return true;
        }
        System.out.println("No se encontró la lección con ID " + id + ".");
        return false;
    }

    // Método para obtener el número total de lecciones creadas
    public int obtenerTotalLecciones() {
        return lecciones.size();
    }

    // Método para imprimir todas las lecciones
    public void imprimirLecciones() {
        if (lecciones.isEmpty()) {
            System.out.println("No hay lecciones disponibles.");
        } else {
            lecciones.forEach(leccion -> System.out.println(leccion.obtenerResumen()));
        }
    }
}
