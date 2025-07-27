package GestorEjercicios.model;

import Modulo_Usuario.Clases.Usuario;
import Modulo_Usuario.Clases.UsuarioComunidad;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de progreso de usuarios
 * Maneja la experiencia y conocimiento adquirido por los usuarios a través de las lecciones
 */
public class GestorProgresoUsuario {
    
    // Almacena el progreso de cada usuario
    private static final Map<String, ProgresoUsuario> progresoUsuarios = new ConcurrentHashMap<>();
    
    /**
     * Clase interna para almacenar el progreso de un usuario
     */
    public static class ProgresoUsuario {
        private String username;
        private int experienciaTotal;
        private int conocimientoTotal;
        private Map<Integer, LeccionCompletada> leccionesCompletadas;
        private Map<Integer, Double> progresoLecciones; // Progreso por lección (0.0 a 1.0)
        
        public ProgresoUsuario(String username) {
            this.username = username;
            this.experienciaTotal = 0;
            this.conocimientoTotal = 0;
            this.leccionesCompletadas = new HashMap<>();
            this.progresoLecciones = new HashMap<>();
        }
        
        // Getters y setters
        public String getUsername() { return username; }
        public int getExperienciaTotal() { return experienciaTotal; }
        public int getConocimientoTotal() { return conocimientoTotal; }
        public Map<Integer, LeccionCompletada> getLeccionesCompletadas() { return leccionesCompletadas; }
        public Map<Integer, Double> getProgresoLecciones() { return progresoLecciones; }
        
        public void agregarExperiencia(int experiencia) {
            this.experienciaTotal += experiencia;
        }
        
        public void agregarConocimiento(int conocimiento) {
            this.conocimientoTotal += conocimiento;
        }
        
        public void marcarLeccionCompletada(int leccionId, LeccionCompletada leccionCompletada) {
            this.leccionesCompletadas.put(leccionId, leccionCompletada);
            this.progresoLecciones.put(leccionId, 1.0);
        }
        
        public void actualizarProgresoLeccion(int leccionId, double progreso) {
            this.progresoLecciones.put(leccionId, Math.min(1.0, Math.max(0.0, progreso)));
        }
        
        public int getNumeroLeccionesCompletadas() {
            return leccionesCompletadas.size();
        }
        
        public double getProgresoPromedio() {
            if (progresoLecciones.isEmpty()) return 0.0;
            return progresoLecciones.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }
    
    /**
     * Clase para almacenar información de una lección completada
     */
    public static class LeccionCompletada {
        private int leccionId;
        private String nombreLeccion;
        private int aciertos;
        private int totalEjercicios;
        private int experienciaGanada;
        private int conocimientoGanado;
        private long fechaCompletada;
        
        public LeccionCompletada(int leccionId, String nombreLeccion, int aciertos, 
                               int totalEjercicios, int experienciaGanada, int conocimientoGanado) {
            this.leccionId = leccionId;
            this.nombreLeccion = nombreLeccion;
            this.aciertos = aciertos;
            this.totalEjercicios = totalEjercicios;
            this.experienciaGanada = experienciaGanada;
            this.conocimientoGanado = conocimientoGanado;
            this.fechaCompletada = System.currentTimeMillis();
        }
        
        // Getters
        public int getLeccionId() { return leccionId; }
        public String getNombreLeccion() { return nombreLeccion; }
        public int getAciertos() { return aciertos; }
        public int getTotalEjercicios() { return totalEjercicios; }
        public int getExperienciaGanada() { return experienciaGanada; }
        public int getConocimientoGanado() { return conocimientoGanado; }
        public long getFechaCompletada() { return fechaCompletada; }
        
        public double getPorcentajeAcierto() {
            return totalEjercicios > 0 ? (double) aciertos / totalEjercicios : 0.0;
        }
    }
    
    /**
     * Obtiene o crea el progreso de un usuario
     */
    public static ProgresoUsuario obtenerProgresoUsuario(Usuario usuario) {
        String username = usuario.getUsername();
        return progresoUsuarios.computeIfAbsent(username, ProgresoUsuario::new);
    }
    
    /**
     * Marca una lección como completada para un usuario
     */
    public static void marcarLeccionCompletada(Usuario usuario, Leccion leccion, int aciertos) {
        ProgresoUsuario progreso = obtenerProgresoUsuario(usuario);
        
        // Calcular experiencia y conocimiento ganados
        int experienciaGanada = calcularExperienciaGanada(leccion, aciertos);
        int conocimientoGanado = calcularConocimientoGanado(leccion, aciertos);
        
        // Crear registro de lección completada
        LeccionCompletada leccionCompletada = new LeccionCompletada(
            leccion.getId(),
            leccion.getNombre(),
            aciertos,
            leccion.getNumeroEjercicios(),
            experienciaGanada,
            conocimientoGanado
        );
        
        // Actualizar progreso del usuario
        progreso.marcarLeccionCompletada(leccion.getId(), leccionCompletada);
        progreso.agregarExperiencia(experienciaGanada);
        progreso.agregarConocimiento(conocimientoGanado);
        
        // Actualizar usuario si es UsuarioComunidad
        if (usuario instanceof UsuarioComunidad) {
            UsuarioComunidad usuarioComunidad = (UsuarioComunidad) usuario;
            usuarioComunidad.incrementarReputacion(experienciaGanada / 10); // Convertir XP a reputación
        }
        
        System.out.println("Lección completada por " + usuario.getUsername() + 
                          ": " + aciertos + "/" + leccion.getNumeroEjercicios() + 
                          " ejercicios correctos. XP: +" + experienciaGanada + 
                          ", Conocimiento: +" + conocimientoGanado);
    }
    
    /**
     * Actualiza el progreso de un usuario en una lección específica
     */
    public static void actualizarProgresoLeccion(Usuario usuario, Leccion leccion, double progreso) {
        ProgresoUsuario progresoUsuario = obtenerProgresoUsuario(usuario);
        progresoUsuario.actualizarProgresoLeccion(leccion.getId(), progreso);
    }
    
    /**
     * Obtiene el progreso de un usuario en una lección específica
     */
    public static double obtenerProgresoLeccion(Usuario usuario, Leccion leccion) {
        ProgresoUsuario progresoUsuario = obtenerProgresoUsuario(usuario);
        return progresoUsuario.getProgresoLecciones().getOrDefault(leccion.getId(), 0.0);
    }
    
    /**
     * Obtiene las estadísticas completas de un usuario
     */
    public static EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario) {
        ProgresoUsuario progreso = obtenerProgresoUsuario(usuario);
        
        int ejerciciosCorrectos = progreso.getLeccionesCompletadas().values().stream()
            .mapToInt(LeccionCompletada::getAciertos)
            .sum();
        
        int ejerciciosTotales = progreso.getLeccionesCompletadas().values().stream()
            .mapToInt(LeccionCompletada::getTotalEjercicios)
            .sum();
        
        return new EstadisticasUsuario(
            progreso.getExperienciaTotal(),
            progreso.getConocimientoTotal(),
            progreso.getNumeroLeccionesCompletadas(),
            ejerciciosCorrectos,
            ejerciciosTotales
        );
    }
    
    /**
     * Calcula la experiencia ganada basada en el rendimiento
     */
    private static int calcularExperienciaGanada(Leccion leccion, int aciertos) {
        double porcentajeAcierto = (double) aciertos / leccion.getNumeroEjercicios();
        int experienciaBase = leccion.getExperiencia();
        
        // Bonus por buen rendimiento
        if (porcentajeAcierto >= 0.8) {
            return (int) (experienciaBase * 1.2); // 20% bonus
        } else if (porcentajeAcierto >= 0.6) {
            return experienciaBase; // Experiencia normal
        } else {
            return (int) (experienciaBase * 0.5); // 50% de la experiencia
        }
    }
    
    /**
     * Calcula el conocimiento ganado basado en el rendimiento
     */
    private static int calcularConocimientoGanado(Leccion leccion, int aciertos) {
        double porcentajeAcierto = (double) aciertos / leccion.getNumeroEjercicios();
        int conocimientoBase = leccion.getConocimiento();
        
        // El conocimiento se otorga proporcionalmente al rendimiento
        return (int) (conocimientoBase * porcentajeAcierto);
    }
    
    /**
     * Clase para estadísticas del usuario (compatible con la interfaz)
     */
    public static class EstadisticasUsuario {
        private int experienciaTotal;
        private int conocimientoTotal;
        private int leccionesCompletadas;
        private int ejerciciosCorrectos;
        private int ejerciciosTotales;
        
        public EstadisticasUsuario(int experienciaTotal, int conocimientoTotal, 
                                 int leccionesCompletadas, int ejerciciosCorrectos, int ejerciciosTotales) {
            this.experienciaTotal = experienciaTotal;
            this.conocimientoTotal = conocimientoTotal;
            this.leccionesCompletadas = leccionesCompletadas;
            this.ejerciciosCorrectos = ejerciciosCorrectos;
            this.ejerciciosTotales = ejerciciosTotales;
        }
        
        // Getters
        public int getExperienciaTotal() { return experienciaTotal; }
        public int getConocimientoTotal() { return conocimientoTotal; }
        public int getLeccionesCompletadas() { return leccionesCompletadas; }
        public int getEjerciciosCorrectos() { return ejerciciosCorrectos; }
        public int getEjerciciosTotales() { return ejerciciosTotales; }
        
        public double getPorcentajeAcierto() {
            return ejerciciosTotales > 0 ? (double) ejerciciosCorrectos / ejerciciosTotales : 0.0;
        }
    }
} 