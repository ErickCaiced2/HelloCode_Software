package GestionAprendizaje_Modulo.Modelo;
// Importaciones necesarias para listas y generación de IDs únicos.
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import GestionAprendizaje_Modulo.Ruta.Ruta;
/**
 * Representa un curso, el contenedor principal gestionado por nuestro módulo.
 * Un curso agrupa una biblioteca de módulos temáticos y ofrece una o más
 * rutas de aprendizaje, las cuales son "jaladas" de módulos externos.
 */
public class Curso {

    // Identificador único del curso, generado automáticamente.
    private final String id;

    // Nombre del curso (por ejemplo: "Curso de Java Básico").
    private final String nombre;

    // Breve descripción del curso para mostrar al usuario.
    private final String descripcion;

    // Lista de módulos temáticos asociados a este curso (contenido "jalado").
    // Cada módulo puede contener lecciones y recursos propios.
    private final List<ModuloEducativo> modulos;

    // Lista de rutas de aprendizaje disponibles para este curso (contenido "jalado").
    // Una ruta es una secuencia de lecciones organizadas de forma visual.
    private final List<Ruta> rutas;

    // Constructor que recibe nombre y descripción, genera ID único,
    // y crea listas vacías para módulos y rutas.
    public Curso(String nombre, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modulos = new ArrayList<>();
        this.rutas = new ArrayList<>();
    }

    // Getter que devuelve el identificador único del curso.
    public String getId() { return id; }

    // Getter que devuelve el nombre del curso.
    public String getNombre() { return nombre; }

    // Getter que devuelve la descripción del curso.
    public String getDescripcion() { return descripcion; }

    /**
     * Devuelve la lista de módulos temáticos asociados.
     * El contenido de esta lista es gestionado por otro equipo.
     */
    public List<ModuloEducativo> getModulos() {
        return modulos;
    }

    /**
     * Devuelve las rutas de aprendizaje disponibles en el curso.
     * El contenido de esta lista es gestionado por otro equipo.
     */
    public List<Ruta> getRutas() {
        return rutas;
    }

    // Método sobrescrito que devuelve el nombre del curso como representación en texto.
    @Override
    public String toString() {
        return nombre;
    }
}
