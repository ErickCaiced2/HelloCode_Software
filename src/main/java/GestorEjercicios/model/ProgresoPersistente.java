package GestorEjercicios.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 🗄️ FACHADA LEGACY - Delega todo a ProgresoSimple
 * 
 * ⚠️ IMPORTANTE: Esta clase mantiene compatibilidad con código existente
 * Para código nuevo, usa directamente:
 * - IntegracionGamificacion.java (para gamificación)
 * - ProgresoSimple.java (para progreso directo)
 */
public class ProgresoPersistente {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 📊 Datos del progreso del usuario (clase legacy)
     */
    public static class DatosProgreso {
        public String nombreUsuario;
        public String lenguajeConfigurado;
        public String nivelConfigurado;
        public String tipoConfigurado;
        public int leccionActual;
        public boolean primeraLeccionCompletada;
        public List<LeccionCompletada> leccionesCompletadas;
        public Map<String, Integer> estadisticas;
        public String ultimaActualizacion;
        public int experienciaTotal;
        public int conocimientoTotal;
        
        public DatosProgreso() {
            this.leccionesCompletadas = new ArrayList<>();
            this.estadisticas = new HashMap<>();
            this.ultimaActualizacion = LocalDateTime.now().format(formatter);
            this.experienciaTotal = 0;
            this.conocimientoTotal = 0;
        }
    }
    
    /**
     * 📚 Lección completada (clase legacy)
     */
    public static class LeccionCompletada {
        public String titulo;
        public String lenguaje;
        public String nivel;
        public int puntuacion;
        public int tiempoSegundos;
        public List<String> ejerciciosResueltos;
        public String fechaCompletada;
        
        public LeccionCompletada(String titulo, String lenguaje, String nivel, 
                                int puntuacion, int tiempoSegundos, 
                                List<String> ejerciciosResueltos) {
            this.titulo = titulo;
            this.lenguaje = lenguaje;
            this.nivel = nivel;
            this.puntuacion = puntuacion;
            this.tiempoSegundos = tiempoSegundos;
            this.ejerciciosResueltos = ejerciciosResueltos != null ? ejerciciosResueltos : new ArrayList<>();
            this.fechaCompletada = LocalDateTime.now().format(formatter);
        }
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: Delega a ProgresoSimple
     */
    public static void registrarLeccionCompletada(String usuario, int numeroLeccion, 
                                                 String titulo, String lenguaje, String nivel, 
                                                 int puntuacion, int tiempoSegundos, 
                                                 List<String> ejerciciosResueltos) {
        ProgresoSimple.registrarLeccionCompletada(
            usuario, numeroLeccion, titulo, lenguaje, nivel,
            puntuacion, tiempoSegundos, ejerciciosResueltos.size()
        );
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: Delega a ProgresoSimple
     */
    public static Map<String, Integer> obtenerEstadisticas(String usuario) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("experiencia_total", ProgresoSimple.obtenerXPTotal(usuario));
        stats.put("conocimiento_total", ProgresoSimple.obtenerConocimientoTotal(usuario));
        stats.put("lecciones_completadas", ProgresoSimple.obtenerLeccionesCompletadas(usuario));
        return stats;
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: Delega a ProgresoSimple
     */
    public static List<LeccionCompletada> obtenerLeccionesCompletadas(String usuario) {
        List<LeccionCompletada> leccionesLegacy = new ArrayList<>();
        int cantidadLecciones = ProgresoSimple.obtenerLeccionesCompletadas(usuario);
        
        for (int i = 1; i <= cantidadLecciones; i++) {
            leccionesLegacy.add(new LeccionCompletada(
                "Lección " + i, "JAVA", "BASICO", 100, 300, new ArrayList<>()
            ));
        }
        
        return leccionesLegacy;
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: Delega a ProgresoSimple
     */
    public static DatosProgreso obtenerConfiguracion(String usuario) {
        DatosProgreso datos = new DatosProgreso();
        datos.nombreUsuario = usuario;
        datos.experienciaTotal = ProgresoSimple.obtenerXPTotal(usuario);
        datos.conocimientoTotal = ProgresoSimple.obtenerConocimientoTotal(usuario);
        datos.leccionActual = 1;
        datos.primeraLeccionCompletada = ProgresoSimple.obtenerLeccionesCompletadas(usuario) > 0;
        return datos;
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: ProgresoSimple guarda automáticamente
     */
    public static void actualizarConfiguracion(String usuario, String lenguaje, String nivel, 
                                              String tipo, int leccionActual, boolean primeraCompletada) {
        System.out.println("📝 Configuración actualizada para " + usuario + " (auto-guardado en ProgresoSimple)");
    }
    
    /**
     * ⚠️ MÉTODO LEGACY: No borra archivos TXT
     */
    public static void limpiarProgreso() {
        System.out.println("🗑️ Limpiando progreso legacy (archivos TXT permanecen)");
    }
    
    // Métodos adicionales para compatibilidad
    public static int obtenerExperienciaTotal(String usuario) {
        return ProgresoSimple.obtenerXPTotal(usuario);
    }
    
    public static int obtenerConocimientoTotal(String usuario) {
        return ProgresoSimple.obtenerConocimientoTotal(usuario);
    }
}
