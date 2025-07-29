package GestorEjercicios.services;

import GestorEjercicios.config.ConfiguracionSistema;

/**
 * 📝 Servicio de logging profesional para el módulo GestorEjercicios
 * Reemplaza los System.out.println dispersos con logging estructurado
 */
public class LoggingService {
    
    public enum LogLevel {
        DEBUG("🐛"), INFO("ℹ️"), WARN("⚠️"), ERROR("❌"), SUCCESS("✅");
        
        private final String emoji;
        LogLevel(String emoji) { this.emoji = emoji; }
        public String getEmoji() { return emoji; }
    }
    
    /**
     * Log de debug (solo si está habilitado)
     */
    public static void debug(String mensaje) {
        if (ConfiguracionSistema.isDebugEnabled()) {
            log(LogLevel.DEBUG, mensaje);
        }
    }
    
    /**
     * Log de información general
     */
    public static void info(String mensaje) {
        log(LogLevel.INFO, mensaje);
    }
    
    /**
     * Log de advertencia
     */
    public static void warn(String mensaje) {
        log(LogLevel.WARN, mensaje);
    }
    
    /**
     * Log de error
     */
    public static void error(String mensaje) {
        log(LogLevel.ERROR, mensaje);
    }
    
    /**
     * Log de error con excepción
     */
    public static void error(String mensaje, Throwable throwable) {
        error(mensaje + ": " + throwable.getMessage());
        if (ConfiguracionSistema.isDebugEnabled()) {
            throwable.printStackTrace();
        }
    }
    
    /**
     * Log de éxito
     */
    public static void success(String mensaje) {
        log(LogLevel.SUCCESS, mensaje);
    }
    
    /**
     * Log estructurado de progreso de usuario
     */
    public static void logProgresoUsuario(String usuario, String accion, String detalle) {
        if (ConfiguracionSistema.isDebugEnabled()) {
            debug("👤 USUARIO: " + usuario + " | " + accion + " | " + detalle);
        }
    }
    
    /**
     * Log de lección
     */
    public static void logLeccion(String accion, String titulo, String detalle) {
        info("📚 LECCIÓN: " + accion + " | " + titulo + " | " + detalle);
    }
    
    /**
     * Log de ejercicio
     */
    public static void logEjercicio(String accion, String tipo, String detalle) {
        debug("🎯 EJERCICIO: " + accion + " | " + tipo + " | " + detalle);
    }
    
    /**
     * Log de persistencia
     */
    public static void logPersistencia(String accion, String archivo, boolean exito) {
        if (exito) {
            success("💾 PERSISTENCIA: " + accion + " | " + archivo);
        } else {
            error("💾 PERSISTENCIA: ERROR en " + accion + " | " + archivo);
        }
    }
    
    /**
     * Log de integración con otros módulos
     */
    public static void logIntegracion(String modulo, String accion, String resultado) {
        info("🔗 INTEGRACIÓN: " + modulo + " | " + accion + " | " + resultado);
    }
    
    /**
     * Método base de logging
     */
    private static void log(LogLevel level, String mensaje) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + level.getEmoji() + " " + mensaje);
    }
    
    /**
     * Log de inicio de sesión con información del sistema
     */
    public static void logInicioSistema() {
        info("🚀 === GESTOR EJERCICIOS INICIADO ===");
        ConfiguracionSistema.mostrarConfiguracion();
    }
    
    /**
     * Log de cierre del sistema
     */
    public static void logCierreSistema() {
        info("🛑 === GESTOR EJERCICIOS FINALIZADO ===");
    }
    
    /**
     * Log de estadísticas del sistema
     */
    public static void logEstadisticas(String titulo, java.util.Map<String, Object> stats) {
        info("📊 === " + titulo + " ===");
        if (ConfiguracionSistema.isDebugEnabled()) {
            stats.forEach((key, value) -> 
                debug("   " + key + ": " + value));
        }
    }
}
