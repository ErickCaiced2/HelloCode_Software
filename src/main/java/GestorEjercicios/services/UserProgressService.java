package GestorEjercicios.services;

import GestorEjercicios.config.ConfiguracionSistema;
import GestorEjercicios.model.ProgresoPersistente;
import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoLeccion;

import java.util.List;
import java.util.Map;

/**
 * 👤 Servicio de gestión de progreso del usuario
 * Separa la lógica de persistencia de los controladores
 * 
 * REFACTORIZADO: Aplica Single Responsibility Principle
 */
public class UserProgressService {
    
    /**
     * 📊 Información del progreso del usuario
     */
    public static class ProgresoInfo {
        public final String nombreUsuario;
        public final LenguajeProgramacion lenguajeConfigurado;
        public final NivelDificultad nivelConfigurado;
        public final TipoLeccion tipoConfigurado;
        public final int leccionActual;
        public final boolean primeraLeccionCompletada;
        
        public ProgresoInfo(String nombreUsuario, LenguajeProgramacion lenguajeConfigurado,
                           NivelDificultad nivelConfigurado, TipoLeccion tipoConfigurado,
                           int leccionActual, boolean primeraLeccionCompletada) {
            this.nombreUsuario = nombreUsuario;
            this.lenguajeConfigurado = lenguajeConfigurado;
            this.nivelConfigurado = nivelConfigurado;
            this.tipoConfigurado = tipoConfigurado;
            this.leccionActual = leccionActual;
            this.primeraLeccionCompletada = primeraLeccionCompletada;
        }
    }
    
    /**
     * 📖 Carga el progreso de un usuario
     */
    public static ProgresoInfo cargarProgresoUsuario(String nombreUsuario) {
        try {
            ProgresoPersistente.DatosProgreso progreso = ProgresoPersistente.obtenerConfiguracion(nombreUsuario);
            
            LenguajeProgramacion lenguaje = null;
            NivelDificultad nivel = null;
            TipoLeccion tipo = null;
            
            if (progreso.lenguajeConfigurado != null) {
                try {
                    lenguaje = LenguajeProgramacion.valueOf(progreso.lenguajeConfigurado);
                } catch (IllegalArgumentException e) {
                    LoggingService.warn("Lenguaje inválido en progreso: " + progreso.lenguajeConfigurado);
                }
            }
            
            if (progreso.nivelConfigurado != null) {
                try {
                    nivel = NivelDificultad.valueOf(progreso.nivelConfigurado);
                } catch (IllegalArgumentException e) {
                    LoggingService.warn("Nivel inválido en progreso: " + progreso.nivelConfigurado);
                }
            }
            
            if (progreso.tipoConfigurado != null) {
                try {
                    tipo = TipoLeccion.valueOf(progreso.tipoConfigurado);
                } catch (IllegalArgumentException e) {
                    LoggingService.warn("Tipo inválido en progreso: " + progreso.tipoConfigurado);
                }
            }
            
            ProgresoInfo info = new ProgresoInfo(
                nombreUsuario, lenguaje, nivel, tipo,
                progreso.leccionActual, progreso.primeraLeccionCompletada
            );
            
            LoggingService.logProgresoUsuario(nombreUsuario, "PROGRESO_CARGADO", 
                                            "Lección " + progreso.leccionActual);
            
            return info;
            
        } catch (Exception e) {
            LoggingService.error("Error cargando progreso del usuario: " + nombreUsuario, e);
            return new ProgresoInfo(nombreUsuario, null, null, null, 1, false);
        }
    }
    
    /**
     * 💾 Guarda la configuración del usuario
     */
    public static void guardarConfiguracion(String nombreUsuario, LenguajeProgramacion lenguaje,
                                          NivelDificultad nivel, TipoLeccion tipo,
                                          int leccionActual, boolean primeraLeccionCompletada) {
        try {
            if (ConfiguracionSistema.isAutoGuardarEnabled()) {
                ProgresoPersistente.actualizarConfiguracion(
                    nombreUsuario,
                    lenguaje != null ? lenguaje.name() : null,
                    nivel != null ? nivel.name() : null,
                    tipo != null ? tipo.name() : null,
                    leccionActual,
                    primeraLeccionCompletada
                );
                
                LoggingService.logProgresoUsuario(nombreUsuario, "CONFIGURACIÓN_GUARDADA",
                                                String.format("%s | %s | Lección %d", 
                                                            lenguaje, nivel, leccionActual));
            }
        } catch (Exception e) {
            LoggingService.error("Error guardando configuración del usuario: " + nombreUsuario, e);
        }
    }
    
    /**
     * 🏆 Registra una lección completada
     */
    public static void registrarLeccionCompletada(String nombreUsuario, int numeroLeccion,
                                                String titulo, String lenguaje, String nivel,
                                                int puntuacion, int tiempoSegundos,
                                                List<String> ejerciciosResueltos) {
        try {
            ProgresoPersistente.registrarLeccionCompletada(
                nombreUsuario, numeroLeccion, titulo, lenguaje, nivel,
                puntuacion, tiempoSegundos, ejerciciosResueltos
            );
            
            LoggingService.logProgresoUsuario(nombreUsuario, "LECCIÓN_COMPLETADA",
                                            String.format("%s - Puntuación: %d", titulo, puntuacion));
            
        } catch (Exception e) {
            LoggingService.error("Error registrando lección completada para: " + nombreUsuario, e);
        }
    }
    
    /**
     * 📊 Obtiene estadísticas del usuario
     */
    public static Map<String, Integer> obtenerEstadisticas(String nombreUsuario) {
        try {
            Map<String, Integer> stats = ProgresoPersistente.obtenerEstadisticas(nombreUsuario);
            LoggingService.logEstadisticas("ESTADÍSTICAS USUARIO: " + nombreUsuario, 
                                         Map.copyOf(stats));
            return stats;
        } catch (Exception e) {
            LoggingService.error("Error obteniendo estadísticas para: " + nombreUsuario, e);
            return Map.of();
        }
    }
    
    /**
     * 📋 Obtiene lecciones completadas del usuario
     */
    public static List<ProgresoPersistente.LeccionCompletada> obtenerLeccionesCompletadas(String nombreUsuario) {
        try {
            return ProgresoPersistente.obtenerLeccionesCompletadas(nombreUsuario);
        } catch (Exception e) {
            LoggingService.error("Error obteniendo lecciones completadas para: " + nombreUsuario, e);
            return List.of();
        }
    }
    
    /**
     * 🗑️ Reinicia el progreso del usuario
     */
    public static void reiniciarProgreso() {
        try {
            ProgresoPersistente.limpiarProgreso();
            LoggingService.info("Progreso del sistema reiniciado");
        } catch (Exception e) {
            LoggingService.error("Error reiniciando progreso", e);
        }
    }
    
    /**
     * ✅ Valida si la configuración del usuario es válida
     */
    public static boolean esConfiguracionValida(ProgresoInfo progreso) {
        return progreso.lenguajeConfigurado != null &&
               progreso.nivelConfigurado != null &&
               progreso.tipoConfigurado != null;
    }
    
    /**
     * 📈 Calcula el progreso general del usuario (0-100%)
     */
    public static double calcularProgresoGeneral(String nombreUsuario) {
        try {
            Map<String, Integer> stats = obtenerEstadisticas(nombreUsuario);
            int leccionesCompletadas = stats.getOrDefault("leccionesCompletadas", 0);
            
            // Considerar que hay aproximadamente 20 lecciones totales
            int leccionesTotales = 20;
            double progreso = Math.min(100.0, (leccionesCompletadas * 100.0) / leccionesTotales);
            
            LoggingService.debug("Progreso general " + nombreUsuario + ": " + 
                               String.format("%.1f%% (%d/%d lecciones)", 
                                           progreso, leccionesCompletadas, leccionesTotales));
            
            return progreso;
            
        } catch (Exception e) {
            LoggingService.error("Error calculando progreso general para: " + nombreUsuario, e);
            return 0.0;
        }
    }
}
