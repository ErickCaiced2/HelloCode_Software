package GestorEjercicios.integracion;

import GestorEjercicios.model.ProgresoSimple;
import java.util.*;

/**
 * 🎮 CLASE DE INTEGRACIÓN PARA MÓDULO DE GAMIFICACIÓN
 * 
 * Esta clase proporciona una interfaz limpia y pública para que el módulo
 * de Gamificación pueda acceder a los datos de progreso de lecciones.
 * 
 * PARA DESARROLLADORES DE GAMIFICACIÓN:
 * - Usen estos métodos para obtener datos de progreso de usuarios
 * - Todos los métodos son estáticos, no necesitan instanciar la clase
 * - Los datos se actualizan en tiempo real desde el sistema de lecciones
 */
public class IntegracionGamificacion {
    
    /**
     * 📈 Obtiene el número total de lecciones completadas por un usuario
     * @param usuario Nombre de usuario
     * @return Número de lecciones completadas (históricamente)
     */
    public static int getLeccionesTotales(String usuario) {
        return ProgresoSimple.obtenerLeccionesCompletadas(usuario);
    }
    
    /**
     * 📅 Obtiene lecciones completadas en los últimos 7 días
     * @param usuario Nombre de usuario  
     * @return Número de lecciones completadas esta semana
     */
    public static int getLeccionesSemanaActual(String usuario) {
        return ProgresoSimple.obtenerLeccionesUltimaSemana(usuario);
    }
    
    /**
     * 📆 Obtiene lecciones completadas en los últimos 30 días
     * @param usuario Nombre de usuario
     * @return Número de lecciones completadas este mes
     */
    public static int getLeccionesMesActual(String usuario) {
        return ProgresoSimple.obtenerLeccionesUltimoMes(usuario);
    }
    
    /**
     * 👥 Obtiene lista de todos los usuarios que han completado al menos una lección
     * @return Lista de nombres de usuario activos
     */
    public static List<String> getUsuariosActivos() {
        return ProgresoSimple.obtenerUsuariosActivos();
    }
    
    /**
     * 💎 Obtiene puntos XP totales de un usuario
     * @param usuario Nombre de usuario
     * @return Total de puntos XP acumulados
     */
    public static int getXPTotal(String usuario) {
        return ProgresoSimple.obtenerXPTotal(usuario);
    }
    
    /**
     * 🧠 Obtiene puntos de conocimiento totales de un usuario
     * @param usuario Nombre de usuario
     * @return Total de puntos de conocimiento acumulados
     */
    public static int getConocimientoTotal(String usuario) {
        return ProgresoSimple.obtenerConocimientoTotal(usuario);
    }
    
    /**
     * 📊 Obtiene todas las estadísticas de un usuario en un solo llamado
     * @param usuario Nombre de usuario
     * @return Map con todas las estadísticas del usuario
     * 
     * Claves del Map:
     * - "xp_total": Puntos XP totales
     * - "conocimiento_total": Puntos de conocimiento totales  
     * - "lecciones_totales": Lecciones completadas históricamente
     * - "lecciones_ultima_semana": Lecciones completadas en últimos 7 días
     * - "lecciones_ultimo_mes": Lecciones completadas en últimos 30 días
     */
    public static Map<String, Integer> getEstadisticasCompletas(String usuario) {
        return ProgresoSimple.obtenerEstadisticasUsuario(usuario);
    }
    
    /**
     * 🎯 Verifica si un usuario ha alcanzado una meta de lecciones semanales
     * @param usuario Nombre de usuario
     * @param metaSemanal Número objetivo de lecciones por semana
     * @return true si cumplió o superó la meta
     */
    public static boolean cumplioMetaSemanal(String usuario, int metaSemanal) {
        return getLeccionesSemanaActual(usuario) >= metaSemanal;
    }
    
    /**
     * 🎯 Verifica si un usuario ha alcanzado una meta de lecciones mensuales
     * @param usuario Nombre de usuario
     * @param metaMensual Número objetivo de lecciones por mes
     * @return true si cumplió o superó la meta
     */
    public static boolean cumplioMetaMensual(String usuario, int metaMensual) {
        return getLeccionesMesActual(usuario) >= metaMensual;
    }
    
    /**
     * 📋 Obtiene un reporte completo para debugging o logging
     * @param usuario Nombre de usuario
     * @return String con reporte formateado de estadísticas
     */
    public static String getReporteUsuario(String usuario) {
        Map<String, Integer> stats = getEstadisticasCompletas(usuario);
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE ").append(usuario.toUpperCase()).append(" ===\n");
        reporte.append("💎 XP Total: ").append(stats.get("xp_total")).append("\n");
        reporte.append("🧠 Conocimiento Total: ").append(stats.get("conocimiento_total")).append("\n");
        reporte.append("📚 Lecciones Totales: ").append(stats.get("lecciones_totales")).append("\n");
        reporte.append("📅 Lecciones Esta Semana: ").append(stats.get("lecciones_ultima_semana")).append("\n");
        reporte.append("📆 Lecciones Este Mes: ").append(stats.get("lecciones_ultimo_mes")).append("\n");
        reporte.append("===========================================");
        
        return reporte.toString();
    }
    
    /**
     * 🔔 Método para que Gamificación notifique cuando necesite datos actualizados
     * Este método fuerza una re-lectura de los archivos de progreso
     */
    public static void solicitarActualizacionDatos() {
        System.out.println("🎮 Gamificación solicita actualización de datos de progreso");
        // Los métodos de ProgresoSimple ya leen en tiempo real desde archivos
        // Esta llamada es principalmente para logging/debugging
    }
}
