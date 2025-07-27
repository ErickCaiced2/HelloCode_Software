package GestorEjercicios;

import GestorEjercicios.integracion.IGestorEjercicios;
import GestorEjercicios.model.Leccion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.LenguajeProgramacion;
import Modulo_Usuario.Clases.Usuario;

import java.util.List;

/**
 * Punto de entrada para el módulo GestorEjercicios
 * Proporciona acceso fácil a la funcionalidad del gestor para otros módulos
 */
public class GestorEjerciciosEntry {
    
    private static final IGestorEjercicios gestor = GestorEjerciciosPrincipal.obtenerInstancia();
    
    /**
     * Obtiene la instancia del gestor de ejercicios
     */
    public static IGestorEjercicios obtenerGestor() {
        return gestor;
    }
    
    /**
     * Crea una nueva lección normal
     */
    public static Leccion crearLeccionNormal(String nombre, List<?> ejercicios) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.NORMAL, 15, 5);
    }
    
    /**
     * Crea una nueva lección de prueba
     */
    public static Leccion crearLeccionPrueba(String nombre, List<?> ejercicios) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.PRUEBA, 30, 0);
    }
    
    /**
     * Crea una nueva lección personalizada
     */
    public static Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento) {
        return gestor.crearLeccion(nombre, ejercicios, tipo, experiencia, conocimiento);
    }

    /**
     * Crea una nueva lección con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento, 
                                      NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, tipo, experiencia, conocimiento, dificultad, lenguaje);
    }

    /**
     * Crea una nueva lección normal con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccionNormal(String nombre, List<?> ejercicios, NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.NORMAL, 15, 5, dificultad, lenguaje);
    }

    /**
     * Crea una nueva lección de prueba con dificultad y lenguaje específicos
     */
    public static Leccion crearLeccionPrueba(String nombre, List<?> ejercicios, NivelDificultad dificultad, LenguajeProgramacion lenguaje) {
        return gestor.crearLeccion(nombre, ejercicios, TipoLeccion.PRUEBA, 30, 0, dificultad, lenguaje);
    }
    
    /**
     * Obtiene una lección por su ID
     */
    public static Leccion obtenerLeccion(int id) {
        return gestor.obtenerLeccion(id);
    }
    
    /**
     * Obtiene todas las lecciones disponibles
     */
    public static List<Leccion> obtenerTodasLasLecciones() {
        return gestor.obtenerTodasLasLecciones();
    }
    
    /**
     * Marca una lección como completada para un usuario
     */
    public static void marcarLeccionCompletada(Leccion leccion, Usuario usuario, int aciertos) {
        gestor.marcarLeccionCompletada(leccion, usuario, aciertos);
    }
    
    /**
     * Obtiene el progreso de un usuario en una lección específica
     */
    public static double obtenerProgresoUsuario(Leccion leccion, Usuario usuario) {
        return gestor.obtenerProgresoUsuario(leccion, usuario);
    }
    
    /**
     * Obtiene las estadísticas de un usuario
     */
    public static IGestorEjercicios.EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario) {
        return gestor.obtenerEstadisticasUsuario(usuario);
    }
    
    /**
     * Ejecuta una lección para un usuario
     */
    public static GestorEjercicios.model.ResultadoEvaluacion ejecutarLeccion(Leccion leccion, Usuario usuario) {
        return gestor.ejecutarLeccion(leccion, usuario);
    }
    
    /**
     * Inicializa el módulo GestorEjercicios
     * Debe ser llamado al inicio de la aplicación
     */
    public static void inicializar() {
        System.out.println("=== Módulo GestorEjercicios inicializado ===");
        System.out.println("Gestor de ejercicios listo para recibir lecciones");
        System.out.println("Tipos de lecciones soportados: NORMAL, PRUEBA");
        System.out.println("Tipos de ejercicios soportados: Selección múltiple, Completar código");
        System.out.println("=============================================");
    }
    
    /**
     * Obtiene información del estado del módulo
     */
    public static String obtenerEstadoModulo() {
        List<Leccion> lecciones = gestor.obtenerTodasLasLecciones();
        int leccionesNormales = (int) lecciones.stream()
            .filter(l -> l.getTipo() == TipoLeccion.NORMAL).count();
        int leccionesPrueba = (int) lecciones.stream()
            .filter(l -> l.getTipo() == TipoLeccion.PRUEBA).count();
        
        return String.format("GestorEjercicios - Total lecciones: %d (Normales: %d, Pruebas: %d)", 
                           lecciones.size(), leccionesNormales, leccionesPrueba);
    }
} 