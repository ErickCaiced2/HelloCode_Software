package GestorEjercicios.model;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoLeccion;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Sistema de persistencia para el progreso del usuario
 */
public class GestorProgreso {
    private static final String ARCHIVO_PROGRESO = "progreso_usuario.json";
    private static final String ARCHIVO_CONFIGURACION = "configuracion_usuario.json";
    
    private Map<Integer, LeccionCompletada> leccionesCompletadas;
    private ConfiguracionUsuario configuracionActual;
    
    public GestorProgreso() {
        this.leccionesCompletadas = new HashMap<>();
        this.configuracionActual = new ConfiguracionUsuario();
        cargarProgreso();
    }
    
    /**
     * Guarda el progreso de una lección completada
     */
    public void guardarLeccionCompletada(int numeroLeccion, Leccion leccion, double puntuacion) {
        LeccionCompletada leccionCompletada = new LeccionCompletada(
            numeroLeccion,
            leccion.getTipo(),
            puntuacion,
            LocalDateTime.now(),
            leccion.getEjerciciosResueltos().size()
        );
        
        leccionesCompletadas.put(numeroLeccion, leccionCompletada);
        guardarProgreso();
    }
    
    /**
     * Verifica si una lección está completada
     */
    public boolean isLeccionCompletada(int numeroLeccion) {
        return leccionesCompletadas.containsKey(numeroLeccion);
    }
    
    /**
     * Obtiene la puntuación de una lección
     */
    public double getPuntuacionLeccion(int numeroLeccion) {
        LeccionCompletada leccion = leccionesCompletadas.get(numeroLeccion);
        return leccion != null ? leccion.getPuntuacion() : 0.0;
    }
    
    /**
     * Obtiene el progreso general (lecciones completadas / total)
     */
    public double getProgresoGeneral() {
        return (double) leccionesCompletadas.size() / 5.0 * 100.0;
    }
    
    /**
     * Obtiene estadísticas del usuario
     */
    public EstadisticasUsuario getEstadisticas() {
        double puntuacionPromedio = leccionesCompletadas.values().stream()
            .mapToDouble(LeccionCompletada::getPuntuacion)
            .average()
            .orElse(0.0);
        
        long ejerciciosCompletados = leccionesCompletadas.values().stream()
            .mapToLong(LeccionCompletada::getEjerciciosCompletados)
            .sum();
        
        return new EstadisticasUsuario(
            leccionesCompletadas.size(),
            puntuacionPromedio,
            ejerciciosCompletados,
            getProgresoGeneral()
        );
    }
    
    /**
     * Guarda la configuración actual del usuario
     */
    public void guardarConfiguracion(LenguajeProgramacion lenguaje, NivelDificultad nivel, TipoLeccion tipo) {
        configuracionActual = new ConfiguracionUsuario(lenguaje, nivel, tipo);
        guardarConfiguracion();
    }
    
    /**
     * Obtiene la configuración guardada
     */
    public ConfiguracionUsuario getConfiguracion() {
        return configuracionActual;
    }
    
    /**
     * Carga el progreso desde archivo
     */
    private void cargarProgreso() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PROGRESO))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }
            
            // Parsear JSON simple (implementación básica)
            parsearProgreso(contenido.toString());
        } catch (IOException e) {
            System.out.println("No se encontró archivo de progreso, iniciando nuevo progreso");
        }
    }
    
    /**
     * Guarda el progreso en archivo
     */
    private void guardarProgreso() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PROGRESO))) {
            pw.println(generarJSONProgreso());
        } catch (IOException e) {
            System.err.println("Error guardando progreso: " + e.getMessage());
        }
    }
    
    /**
     * Guarda la configuración en archivo
     */
    private void guardarConfiguracion() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CONFIGURACION))) {
            pw.println(generarJSONConfiguracion());
        } catch (IOException e) {
            System.err.println("Error guardando configuración: " + e.getMessage());
        }
    }
    
    /**
     * Genera JSON simple para el progreso
     */
    private String generarJSONProgreso() {
        StringBuilder json = new StringBuilder("{\n  \"lecciones_completadas\": [\n");
        
        boolean primero = true;
        for (Map.Entry<Integer, LeccionCompletada> entry : leccionesCompletadas.entrySet()) {
            if (!primero) json.append(",\n");
            LeccionCompletada leccion = entry.getValue();
            json.append("    {\n");
            json.append("      \"numero\": ").append(entry.getKey()).append(",\n");
            json.append("      \"tipo\": \"").append(leccion.getTipo()).append("\",\n");
            json.append("      \"puntuacion\": ").append(leccion.getPuntuacion()).append(",\n");
            json.append("      \"fecha\": \"").append(leccion.getFechaCompletado()).append("\",\n");
            json.append("      \"ejercicios\": ").append(leccion.getEjerciciosCompletados()).append("\n");
            json.append("    }");
            primero = false;
        }
        
        json.append("\n  ]\n}");
        return json.toString();
    }
    
    /**
     * Genera JSON simple para la configuración
     */
    private String generarJSONConfiguracion() {
        return String.format(
            "{\n  \"lenguaje\": \"%s\",\n  \"nivel\": \"%s\",\n  \"tipo\": \"%s\"\n}",
            configuracionActual.getLenguaje(),
            configuracionActual.getNivel(),
            configuracionActual.getTipo()
        );
    }
    
    /**
     * Parsea el progreso desde JSON (implementación básica)
     */
    private void parsearProgreso(String json) {
        // Implementación básica de parsing JSON
        // En una implementación real, usarías una librería como Gson o Jackson
        if (json.contains("\"lecciones_completadas\"")) {
            // Parsear lecciones completadas
            String[] lecciones = json.split("\"numero\":");
            for (int i = 1; i < lecciones.length; i++) {
                // Extraer información básica
                String leccionStr = lecciones[i];
                if (leccionStr.contains("\"puntuacion\":")) {
                    // Extraer número de lección y puntuación
                    String[] partes = leccionStr.split(",");
                    for (String parte : partes) {
                        if (parte.contains("\"puntuacion\":")) {
                            String puntuacionStr = parte.split(":")[1].trim();
                            double puntuacion = Double.parseDouble(puntuacionStr);
                            leccionesCompletadas.put(i, new LeccionCompletada(i, TipoLeccion.NORMAL, puntuacion, LocalDateTime.now(), 5));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Clase interna para representar una lección completada
     */
    public static class LeccionCompletada {
        private int numeroLeccion;
        private TipoLeccion tipo;
        private double puntuacion;
        private LocalDateTime fechaCompletado;
        private long ejerciciosCompletados;
        
        public LeccionCompletada(int numeroLeccion, TipoLeccion tipo, double puntuacion, 
                                LocalDateTime fechaCompletado, long ejerciciosCompletados) {
            this.numeroLeccion = numeroLeccion;
            this.tipo = tipo;
            this.puntuacion = puntuacion;
            this.fechaCompletado = fechaCompletado;
            this.ejerciciosCompletados = ejerciciosCompletados;
        }
        
        // Getters
        public int getNumeroLeccion() { return numeroLeccion; }
        public TipoLeccion getTipo() { return tipo; }
        public double getPuntuacion() { return puntuacion; }
        public LocalDateTime getFechaCompletado() { return fechaCompletado; }
        public long getEjerciciosCompletados() { return ejerciciosCompletados; }
    }
    
    /**
     * Clase para la configuración del usuario
     */
    public static class ConfiguracionUsuario {
        private LenguajeProgramacion lenguaje;
        private NivelDificultad nivel;
        private TipoLeccion tipo;
        
        public ConfiguracionUsuario() {
            this.lenguaje = LenguajeProgramacion.JAVA;
            this.nivel = NivelDificultad.BASICO;
            this.tipo = TipoLeccion.NORMAL;
        }
        
        public ConfiguracionUsuario(LenguajeProgramacion lenguaje, NivelDificultad nivel, TipoLeccion tipo) {
            this.lenguaje = lenguaje;
            this.nivel = nivel;
            this.tipo = tipo;
        }
        
        // Getters
        public LenguajeProgramacion getLenguaje() { return lenguaje; }
        public NivelDificultad getNivel() { return nivel; }
        public TipoLeccion getTipo() { return tipo; }
    }
    
    /**
     * Clase para estadísticas del usuario
     */
    public static class EstadisticasUsuario {
        private int leccionesCompletadas;
        private double puntuacionPromedio;
        private long ejerciciosCompletados;
        private double progresoGeneral;
        
        public EstadisticasUsuario(int leccionesCompletadas, double puntuacionPromedio, 
                                 long ejerciciosCompletados, double progresoGeneral) {
            this.leccionesCompletadas = leccionesCompletadas;
            this.puntuacionPromedio = puntuacionPromedio;
            this.ejerciciosCompletados = ejerciciosCompletados;
            this.progresoGeneral = progresoGeneral;
        }
        
        // Getters
        public int getLeccionesCompletadas() { return leccionesCompletadas; }
        public double getPuntuacionPromedio() { return puntuacionPromedio; }
        public long getEjerciciosCompletados() { return ejerciciosCompletados; }
        public double getProgresoGeneral() { return progresoGeneral; }
    }
} 