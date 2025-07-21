//package GestionAprendizaje_Modulo.Repositorio;
//
//import GestionAprendizaje_Modulo.Modelo.Leccion;
//import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
//import GestionAprendizaje_Modulo.Ruta.NodoRuta;
//import GestionAprendizaje_Modulo.Ruta.Ruta;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//// Simula la API de los otros equipos. Es de SOLO LECTURA para nosotros.
//public class RepositorioExterno {
//
//    private static final RepositorioExterno INSTANCIA = new RepositorioExterno();
//
//    private final List<Ruta> rutasDisponibles = new ArrayList<>();
//    private final List<ModuloEducativo> modulosDisponibles = new ArrayList<>();
//    private final List<Leccion> leccionesDisponibles = new ArrayList<>();
//
//    private RepositorioExterno() {
//        System.out.println(">>> API Externa: Simulando datos disponibles...");
//        // Lecciones
//        Leccion l1 = new Leccion(UUID.randomUUID().toString(), "Variables en Java", "...", "PUBLICADO");
//        Leccion l2 = new Leccion(UUID.randomUUID().toString(), "Bucles en Java", "...", "PUBLICADO");
//        Leccion l3 = new Leccion(UUID.randomUUID().toString(), "Clases y Objetos", "...", "PUBLICADO");
//
//        // Módulos
//        ModuloEducativo m1 = new ModuloEducativo(UUID.randomUUID().toString(), "Fundamentos", "1.0", "","");
//        m1.getLecciones().add(l1);
//        m1.getLecciones().add(l2);
//        modulosDisponibles.add(m1);
//
//        // Rutas
//        Ruta r1 = new Ruta(UUID.randomUUID().toString(), "Ruta de Inicio", "");
//        r1.agregarNodo(new NodoRuta(1, l1));
//        r1.agregarNodo(new NodoRuta(2, l2));
//        rutasDisponibles.add(r1);
//    }
//
//    public static RepositorioExterno getInstancia() {
//        return INSTANCIA;
//    }
//
//    // MÉTODOS PÚBLICOS DE SOLO LECTURA
//    public List<Ruta> getTodasLasRutas() {
//        return new ArrayList<>(rutasDisponibles); // Devuelve una copia para evitar modificación externa
//    }
//
//    public List<ModuloEducativo> getTodosLosModulos() {
//        return new ArrayList<>(modulosDisponibles); // Devuelve una copia
//    }
//    public List<Leccion> getTodasLasLecciones() {
//        return new ArrayList<>(leccionesDisponibles);
//    }
//}
package GestionAprendizaje_Modulo.Repositorio;

import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Simula la API de los otros equipos. Es de SOLO LECTURA para nosotros.
public class RepositorioExterno {

    private static final RepositorioExterno INSTANCIA = new RepositorioExterno();

    // --- COLECCIONES QUE ALMACENAN LOS DATOS "QUEMADOS" ---
    private final List<Leccion> leccionesDisponibles = new ArrayList<>();
    private final List<ModuloEducativo> modulosDisponibles = new ArrayList<>();
    private final List<Ruta> rutasDisponibles = new ArrayList<>();

    private RepositorioExterno() {
        System.out.println(">>> API Externa: Quemando datos de ejemplo...");

        // --- AQUÍ ES DONDE QUEMAMOS LAS LECCIONES ---
        quemarLecciones();

        // El resto de los datos se puede quemar aquí también, usando las lecciones ya creadas.
        quemarModulosYRutas();
    }

    public static RepositorioExterno getInstancia() {
        return INSTANCIA;
    }

    // --- MÉTODO PÚBLICO PARA "JALAR" LAS LECCIONES ---
    /**
     * Devuelve una lista de todas las lecciones disponibles en el sistema.
     * En un sistema real, esto sería una llamada a una API REST.
     * @return Una copia de la lista de lecciones.
     */
    public List<Leccion> getTodasLasLecciones() {
        return new ArrayList<>(leccionesDisponibles); // Devuelve una copia para proteger la lista original
    }

    public List<ModuloEducativo> getTodosLosModulos() {
        return new ArrayList<>(modulosDisponibles);
    }

    public List<Ruta> getTodasLasRutas() {
        return new ArrayList<>(rutasDisponibles);
    }


    // --- MÉTODOS PRIVADOS PARA ORGANIZAR LA CREACIÓN DE DATOS ---

    private void quemarLecciones() {
        // Lección 1:
        Leccion leccion1 = new Leccion(
                UUID.randomUUID().toString(),
                "Introducción a las Variables",
                "Aprende qué es una variable, cómo declararla en Java y los diferentes tipos de datos primitivos como int, double, y boolean.",
                "PUBLICADO"
        );
        leccionesDisponibles.add(leccion1);

        // Lección 2:
        Leccion leccion2 = new Leccion(
                UUID.randomUUID().toString(),
                "Estructuras de Control: if-else",
                "Domina la toma de decisiones en tu código. Aprende a usar las sentencias if, else if, y else para crear programas más inteligentes.",
                "PUBLICADO"
        );
        leccionesDisponibles.add(leccion2);

        // Lección 3:
        Leccion leccion3 = new Leccion(
                UUID.randomUUID().toString(),
                "Bucles con 'for'",
                "Descubre cómo repetir tareas un número determinado de veces utilizando el bucle 'for', una de las herramientas más poderosas de la programación.",
                "PUBLICADO"
        );
        leccionesDisponibles.add(leccion3);

        System.out.println(" -> " + leccionesDisponibles.size() + " lecciones quemadas exitosamente.");
    }

    private void quemarModulosYRutas() {
        // Para que el resto de la app funcione, también quemamos módulos y rutas
        // que USAN las lecciones que acabamos de crear.

        // Módulo que agrupa las lecciones
        ModuloEducativo moduloBasico = new ModuloEducativo(UUID.randomUUID().toString(), "Fundamentos de Programación Java", "1.0", "","");
        moduloBasico.getLecciones().add(leccionesDisponibles.get(0)); // Añade "Variables"
        moduloBasico.getLecciones().add(leccionesDisponibles.get(1)); // Añade "if-else"
        moduloBasico.getLecciones().add(leccionesDisponibles.get(2)); // Añade "for"
        modulosDisponibles.add(moduloBasico);

        // Ruta que ordena las lecciones
        Ruta rutaInicial = new Ruta(UUID.randomUUID().toString(), "Camino del Programador Novato", "Tu primer viaje en el mundo de Java.");
        rutaInicial.agregarNodo(new NodoRuta(1, leccionesDisponibles.get(0))); // Nodo 1 -> Variables
        rutaInicial.agregarNodo(new NodoRuta(2, leccionesDisponibles.get(1))); // Nodo 2 -> if-else
        rutasDisponibles.add(rutaInicial);
    }
}