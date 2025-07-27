package GestorEjercicios;

import GestorEjercicios.integracion.IGestorEjercicios;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.model.GestorLecciones;
import GestorEjercicios.model.GestorProgresoUsuario;
import GestorEjercicios.model.ResultadoEvaluacion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.LenguajeProgramacion;
import Modulo_Usuario.Clases.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Implementación principal del GestorEjercicios
 * Proporciona funcionalidad completa para gestionar lecciones y ejercicios
 */
public class GestorEjerciciosPrincipal implements IGestorEjercicios {

    private static GestorEjerciciosPrincipal instancia;
    private final GestorLecciones gestorLecciones;

    private GestorEjerciciosPrincipal() {
        this.gestorLecciones = new GestorLecciones();
    }

    /**
     * Obtiene la instancia singleton del GestorEjercicios
     */
    public static synchronized GestorEjerciciosPrincipal obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorEjerciciosPrincipal();
        }
        return instancia;
    }

    @Override
    public Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la lección no puede estar vacío");
        }

        if (ejercicios == null || ejercicios.isEmpty()) {
            throw new IllegalArgumentException("La lección debe contener al menos un ejercicio");
        }

        // Generar ID único para la lección
        int id = generarIdUnico();

        // Crear la lección
        Leccion leccion = new Leccion(id, nombre, ejercicios, tipo, experiencia, conocimiento);

        // Agregar la lección al gestor
        gestorLecciones.agregarLeccion(leccion);

        System.out.println("Lección creada: " + leccion.obtenerResumen());
        return leccion;
    }

    @Override
    public Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento,
                                NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la lección no puede estar vacío");
        }

        if (ejercicios == null || ejercicios.isEmpty()) {
            throw new IllegalArgumentException("La lección debe contener al menos un ejercicio");
        }

        if (dificultad == null) {
            throw new IllegalArgumentException("El nivel de dificultad no puede ser nulo");
        }

        if (lenguaje == null) {
            throw new IllegalArgumentException("El lenguaje de programación no puede ser nulo");
        }

        // Generar ID único para la lección
        int id = generarIdUnico();

        // Crear la lección con todos los parámetros
        Leccion leccion = new Leccion(id, nombre, ejercicios, tipo, experiencia, conocimiento, dificultad, lenguaje);

        // Agregar la lección al gestor
        gestorLecciones.agregarLeccion(leccion);

        System.out.println("Lección creada: " + leccion.obtenerResumen());
        return leccion;
    }

    @Override
    public Leccion obtenerLeccion(int id) {
        Optional<Leccion> leccion = gestorLecciones.obtenerLeccionPorId(id);
        return leccion.orElse(null);
    }

    @Override
    public List<Leccion> obtenerTodasLasLecciones() {
        return gestorLecciones.obtenerLecciones();
    }

    @Override
    public ResultadoEvaluacion ejecutarLeccion(Leccion leccion, Usuario usuario) {
        if (leccion == null || usuario == null) {
            throw new IllegalArgumentException("La lección y el usuario no pueden ser nulos");
        }
        System.out.println("Ejecutando lección '" + leccion.getNombre() + "' para usuario " + usuario.getUsername());
        // Crear un resultado de evaluación temporal
        // En una implementación real, esto se manejaría a través de la interfaz de usuario
        return new ResultadoEvaluacion(false, 0, 0, "Lección iniciada", false);
    }

    @Override
    public void marcarLeccionCompletada(Leccion leccion, Usuario usuario, int aciertos) {
        if (leccion == null || usuario == null) {
            throw new IllegalArgumentException("La lección y el usuario no pueden ser nulos");
        }

        if (aciertos < 0 || aciertos > leccion.getNumeroEjercicios()) {
            throw new IllegalArgumentException("El número de aciertos debe estar entre 0 y " + leccion.getNumeroEjercicios());
        }

        // Marcar la lección como completada usando el gestor de progreso
        GestorProgresoUsuario.marcarLeccionCompletada(usuario, leccion, aciertos);

        // Marcar la lección como completada internamente
        leccion.setCompletada(true);

        System.out.println("Lección '" + leccion.getNombre() + "' marcada como completada para " + usuario.getUsername());
    }

    @Override
    public double obtenerProgresoUsuario(Leccion leccion, Usuario usuario) {
        if (leccion == null || usuario == null) {
            return 0.0;
        }

        return GestorProgresoUsuario.obtenerProgresoLeccion(usuario, leccion);
    }

    @Override
    public EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario) {
        if (usuario == null) {
            return new EstadisticasUsuario(0, 0, 0, 0, 0);
        }
        GestorProgresoUsuario.EstadisticasUsuario stats = GestorProgresoUsuario.obtenerEstadisticasUsuario(usuario);
        return new EstadisticasUsuario(
                stats.getExperienciaTotal(),
                stats.getConocimientoTotal(),
                stats.getLeccionesCompletadas(),
                stats.getEjerciciosCorrectos(),
                stats.getEjerciciosTotales()
        );
    }

    /**
     * Genera un ID único para las lecciones
     */
    private int generarIdUnico() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    /**
     * Obtiene el gestor de lecciones interno
     */
    public GestorLecciones getGestorLecciones() {
        return gestorLecciones;
    }

    /**
     * Crea una lección de prueba con ejercicios específicos
     */
    public Leccion crearLeccionPrueba(String nombre, List<?> ejercicios) {
        return crearLeccion(nombre, ejercicios, TipoLeccion.PRUEBA, 30, 0);
    }

    /**
     * Crea una lección normal con ejercicios específicos
     */
    public Leccion crearLeccionNormal(String nombre, List<?> ejercicios) {
        return crearLeccion(nombre, ejercicios, TipoLeccion.NORMAL, 15, 5);
    }

    /**
     * Obtiene lecciones por tipo
     */
    public List<Leccion> obtenerLeccionesPorTipo(TipoLeccion tipo) {
        return gestorLecciones.obtenerLecciones().stream()
                .filter(leccion -> leccion.getTipo() == tipo)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene lecciones disponibles para un usuario (no completadas)
     */
    public List<Leccion> obtenerLeccionesDisponibles(Usuario usuario) {
        return gestorLecciones.obtenerLecciones().stream()
                .filter(leccion -> !leccion.isCompletada())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene lecciones completadas por un usuario
     */
    public List<Leccion> obtenerLeccionesCompletadas(Usuario usuario) {
        return gestorLecciones.obtenerLecciones().stream()
                .filter(leccion -> leccion.isCompletada())
                .collect(java.util.stream.Collectors.toList());
    }
}