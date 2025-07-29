package GestorEjercicios.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 📝 Sistema SIMPLE de progreso usando archivos TXT COMPARTIDOS
 * Todos los usuarios en el mismo directorio con datos separados por prefijo
 */
public class ProgresoSimple {
    
    private static final String DIRECTORIO_PROGRESO = "src/main/resources/GestorEjercicios/data/";
    private static final String ARCHIVO_PROGRESO_COMPARTIDO = DIRECTORIO_PROGRESO + "todos_los_usuarios.txt";
    private static final String ARCHIVO_ESTADISTICAS_COMPARTIDO = DIRECTORIO_PROGRESO + "estadisticas_compartidas.txt";
    private static final String ARCHIVO_TOTALES_COMPARTIDO = DIRECTORIO_PROGRESO + "totales_compartidos.txt";
    
    /**
     * 💾 Guarda el progreso básico del usuario en archivo compartido
     */
    public static void guardarProgresoBasico(String usuario, String lenguaje, String nivel, int leccionActual) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(Paths.get(DIRECTORIO_PROGRESO));
            
            // Leer archivo existente y actualizar datos del usuario específico
            Map<String, String> todosLosUsuarios = cargarTodosLosUsuarios();
            
            // Actualizar datos del usuario actual
            todosLosUsuarios.put(usuario + "_LENGUAJE", lenguaje);
            todosLosUsuarios.put(usuario + "_NIVEL", nivel);
            todosLosUsuarios.put(usuario + "_LECCION_ACTUAL", String.valueOf(leccionActual));
            todosLosUsuarios.put(usuario + "_FECHA_ACTUALIZACION", 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Escribir archivo actualizado
            try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PROGRESO_COMPARTIDO))) {
                for (Map.Entry<String, String> entry : todosLosUsuarios.entrySet()) {
                    writer.println(entry.getKey() + "=" + entry.getValue());
                }
            }
            
            System.out.println("✅ Progreso compartido guardado para usuario: " + usuario);
            
        } catch (IOException e) {
            System.err.println("❌ Error guardando progreso compartido: " + e.getMessage());
        }
    }
    
    /**
     * 📖 Carga todos los usuarios del archivo compartido
     */
    private static Map<String, String> cargarTodosLosUsuarios() {
        Map<String, String> usuarios = new HashMap<>();
        
        try {
            if (Files.exists(Paths.get(ARCHIVO_PROGRESO_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_PROGRESO_COMPARTIDO));
                for (String linea : lineas) {
                    if (linea.contains("=")) {
                        String[] partes = linea.split("=", 2);
                        usuarios.put(partes[0], partes[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error cargando usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    /**
     * 🏆 Registra una lección completada en archivo compartido
     */
    public static void registrarLeccionCompletada(String usuario, int numeroLeccion, String titulo, 
                                                String lenguaje, String nivel, int puntuacion, 
                                                int tiempoSegundos, int ejerciciosResueltos) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(Paths.get(DIRECTORIO_PROGRESO));
            
            // Calcular XP y conocimiento
            int xpGanado = calcularXP(puntuacion, ejerciciosResueltos, nivel);
            int conocimientoGanado = calcularConocimiento(puntuacion, ejerciciosResueltos, nivel);
            
            // Escribir al final del archivo de estadísticas compartido
            try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_ESTADISTICAS_COMPARTIDO, true))) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                writer.println("LECCION_COMPLETADA");
                writer.println("Usuario=" + usuario);
                writer.println("NumeroLeccion=" + numeroLeccion);
                writer.println("Titulo=" + titulo);
                writer.println("Lenguaje=" + lenguaje);
                writer.println("Nivel=" + nivel);
                writer.println("Puntuacion=" + puntuacion);
                writer.println("TiempoSegundos=" + tiempoSegundos);
                writer.println("EjerciciosResueltos=" + ejerciciosResueltos);
                writer.println("XP_Ganado=" + xpGanado);
                writer.println("Conocimiento_Ganado=" + conocimientoGanado);
                writer.println("Fecha=" + fecha);
                writer.println("---"); // Separador
            }
            
            // Actualizar totales compartidos
            actualizarTotalesCompartidos(usuario, xpGanado, conocimientoGanado);
            
            System.out.println("🏆 Lección completada por " + usuario + ": " + titulo);
            System.out.println("   XP ganado: +" + xpGanado);
            System.out.println("   Conocimiento ganado: +" + conocimientoGanado);
            
        } catch (IOException e) {
            System.err.println("❌ Error registrando lección: " + e.getMessage());
        }
    }
    
    /**
     * 🌟 Calcula XP ganado
     */
    private static int calcularXP(int puntuacion, int ejercicios, String nivel) {
        int baseXP = 50;
        int bonusPuntuacion = puntuacion / 10;
        int bonusEjercicios = ejercicios * 5;
        
        double multiplicador = 1.0;
        switch (nivel.toLowerCase()) {
            case "basico": multiplicador = 1.0; break;
            case "intermedio": multiplicador = 1.5; break;
            case "avanzado": multiplicador = 2.0; break;
        }
        
        return (int) ((baseXP + bonusPuntuacion + bonusEjercicios) * multiplicador);
    }
    
    /**
     * 🧠 Calcula conocimiento ganado
     */
    private static int calcularConocimiento(int puntuacion, int ejercicios, String nivel) {
        int baseConocimiento = 20;
        int bonusPuntuacion = puntuacion / 20;
        int bonusEjercicios = ejercicios * 3;
        
        double multiplicador = 1.0;
        switch (nivel.toLowerCase()) {
            case "basico": multiplicador = 1.0; break;
            case "intermedio": multiplicador = 1.3; break;
            case "avanzado": multiplicador = 1.8; break;
        }
        
        return (int) ((baseConocimiento + bonusPuntuacion + bonusEjercicios) * multiplicador);
    }
    
    /**
     * 📊 Actualiza los totales compartidos del usuario
     */
    private static void actualizarTotalesCompartidos(String usuario, int xpGanado, int conocimientoGanado) {
        try {
            // Leer totales actuales de todos los usuarios
            Map<String, String> totales = cargarTotalesCompartidos();
            
            // Obtener totales actuales del usuario específico
            int xpTotal = Integer.parseInt(totales.getOrDefault(usuario + "_XP_TOTAL", "0"));
            int conocimientoTotal = Integer.parseInt(totales.getOrDefault(usuario + "_CONOCIMIENTO_TOTAL", "0"));
            
            // Sumar nuevos valores
            xpTotal += xpGanado;
            conocimientoTotal += conocimientoGanado;
            
            // Actualizar en el mapa
            totales.put(usuario + "_XP_TOTAL", String.valueOf(xpTotal));
            totales.put(usuario + "_CONOCIMIENTO_TOTAL", String.valueOf(conocimientoTotal));
            totales.put(usuario + "_ULTIMA_ACTUALIZACION", 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Guardar archivo actualizado
            try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_TOTALES_COMPARTIDO))) {
                for (Map.Entry<String, String> entry : totales.entrySet()) {
                    writer.println(entry.getKey() + "=" + entry.getValue());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error actualizando totales compartidos: " + e.getMessage());
        }
    }
    
    /**
     * 📖 Carga todos los totales del archivo compartido
     */
    private static Map<String, String> cargarTotalesCompartidos() {
        Map<String, String> totales = new HashMap<>();
        
        try {
            if (Files.exists(Paths.get(ARCHIVO_TOTALES_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_TOTALES_COMPARTIDO));
                for (String linea : lineas) {
                    if (linea.contains("=")) {
                        String[] partes = linea.split("=", 2);
                        totales.put(partes[0], partes[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error cargando totales compartidos: " + e.getMessage());
        }
        
        return totales;
    }
    
    /**
     * 📊 Obtiene XP total del usuario desde archivo compartido
     */
    public static int obtenerXPTotal(String usuario) {
        Map<String, String> totales = cargarTotalesCompartidos();
        try {
            return Integer.parseInt(totales.getOrDefault(usuario + "_XP_TOTAL", "0"));
        } catch (Exception e) {
            System.err.println("⚠️ Error leyendo XP total para " + usuario + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 🧠 Obtiene conocimiento total del usuario desde archivo compartido
     */
    public static int obtenerConocimientoTotal(String usuario) {
        Map<String, String> totales = cargarTotalesCompartidos();
        try {
            return Integer.parseInt(totales.getOrDefault(usuario + "_CONOCIMIENTO_TOTAL", "0"));
        } catch (Exception e) {
            System.err.println("⚠️ Error leyendo conocimiento total para " + usuario + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 📋 Muestra el progreso actual del usuario desde archivos compartidos
     */
    public static void mostrarProgreso(String usuario) {
        System.out.println("\n📊 === PROGRESO DE " + usuario.toUpperCase() + " ===");
        System.out.println("🌟 XP Total: " + obtenerXPTotal(usuario));
        System.out.println("🧠 Conocimiento Total: " + obtenerConocimientoTotal(usuario));
        
        // Contar lecciones completadas por este usuario específico
        try {
            if (Files.exists(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO));
                int leccionesCompletadas = 0;
                boolean esLeccionDelUsuario = false;
                
                for (String linea : lineas) {
                    if (linea.equals("LECCION_COMPLETADA")) {
                        esLeccionDelUsuario = false; // Reset para nueva lección
                    } else if (linea.startsWith("Usuario=") && linea.equals("Usuario=" + usuario)) {
                        esLeccionDelUsuario = true;
                    } else if (linea.equals("---") && esLeccionDelUsuario) {
                        leccionesCompletadas++;
                    }
                }
                System.out.println("📚 Lecciones Completadas: " + leccionesCompletadas);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error contando lecciones para " + usuario + ": " + e.getMessage());
        }
        
        System.out.println("===============================\n");
    }
    
    /**
     * 👥 Muestra el progreso de TODOS los usuarios
     */
    public static void mostrarProgresoTodos() {
        System.out.println("\n👥 === PROGRESO DE TODOS LOS USUARIOS ===");
        
        Map<String, String> totales = cargarTotalesCompartidos();
        Set<String> usuarios = new HashSet<>();
        
        // Extraer nombres únicos de usuarios
        for (String key : totales.keySet()) {
            if (key.contains("_XP_TOTAL")) {
                usuarios.add(key.replace("_XP_TOTAL", ""));
            }
        }
        
        for (String usuario : usuarios) {
            int xp = Integer.parseInt(totales.getOrDefault(usuario + "_XP_TOTAL", "0"));
            int conocimiento = Integer.parseInt(totales.getOrDefault(usuario + "_CONOCIMIENTO_TOTAL", "0"));
            String ultimaActualizacion = totales.getOrDefault(usuario + "_ULTIMA_ACTUALIZACION", "Nunca");
            
            System.out.println("👤 " + usuario + " - XP: " + xp + " | Conocimiento: " + conocimiento + " | Última: " + ultimaActualizacion);
        }
        
        System.out.println("===============================\n");
    }
    
    // ===== MÉTODOS PÚBLICOS PARA INTEGRACIÓN CON GAMIFICACIÓN =====
    
    /**
     * 📊 Obtiene el número de lecciones completadas por un usuario
     * PARA GAMIFICACIÓN: Usar este método para desafíos semanales/mensuales
     */
    public static int obtenerLeccionesCompletadas(String usuario) {
        try {
            if (Files.exists(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO));
                int leccionesCompletadas = 0;
                boolean esLeccionDelUsuario = false;
                
                for (String linea : lineas) {
                    if (linea.equals("LECCION_COMPLETADA")) {
                        esLeccionDelUsuario = false; // Reset para nueva lección
                    } else if (linea.startsWith("Usuario=") && linea.equals("Usuario=" + usuario)) {
                        esLeccionDelUsuario = true;
                    } else if (linea.equals("---") && esLeccionDelUsuario) {
                        leccionesCompletadas++;
                    }
                }
                return leccionesCompletadas;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error contando lecciones para " + usuario + ": " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * 📊 Obtiene las lecciones completadas en la última semana por un usuario
     * PARA GAMIFICACIÓN: Usar este método para desafíos semanales
     */
    public static int obtenerLeccionesUltimaSemana(String usuario) {
        try {
            if (Files.exists(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO));
                int leccionesSemanales = 0;
                boolean esLeccionDelUsuario = false;
                String fechaLeccion = "";
                
                // Calcular fecha límite (7 días atrás)
                LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
                
                for (String linea : lineas) {
                    if (linea.equals("LECCION_COMPLETADA")) {
                        esLeccionDelUsuario = false;
                        fechaLeccion = "";
                    } else if (linea.startsWith("Usuario=") && linea.equals("Usuario=" + usuario)) {
                        esLeccionDelUsuario = true;
                    } else if (linea.startsWith("Fecha=") && esLeccionDelUsuario) {
                        fechaLeccion = linea.substring(6);
                    } else if (linea.equals("---") && esLeccionDelUsuario && !fechaLeccion.isEmpty()) {
                        try {
                            LocalDateTime fechaLeccionDT = LocalDateTime.parse(fechaLeccion, 
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if (fechaLeccionDT.isAfter(hace7Dias)) {
                                leccionesSemanales++;
                            }
                        } catch (Exception e) {
                            // Si no se puede parsear la fecha, no contar la lección
                        }
                    }
                }
                return leccionesSemanales;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error contando lecciones semanales para " + usuario + ": " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * 📊 Obtiene las lecciones completadas en el último mes por un usuario
     * PARA GAMIFICACIÓN: Usar este método para desafíos mensuales
     */
    public static int obtenerLeccionesUltimoMes(String usuario) {
        try {
            if (Files.exists(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO));
                int leccionesMensuales = 0;
                boolean esLeccionDelUsuario = false;
                String fechaLeccion = "";
                
                // Calcular fecha límite (30 días atrás)
                LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
                
                for (String linea : lineas) {
                    if (linea.equals("LECCION_COMPLETADA")) {
                        esLeccionDelUsuario = false;
                        fechaLeccion = "";
                    } else if (linea.startsWith("Usuario=") && linea.equals("Usuario=" + usuario)) {
                        esLeccionDelUsuario = true;
                    } else if (linea.startsWith("Fecha=") && esLeccionDelUsuario) {
                        fechaLeccion = linea.substring(6);
                    } else if (linea.equals("---") && esLeccionDelUsuario && !fechaLeccion.isEmpty()) {
                        try {
                            LocalDateTime fechaLeccionDT = LocalDateTime.parse(fechaLeccion, 
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if (fechaLeccionDT.isAfter(hace30Dias)) {
                                leccionesMensuales++;
                            }
                        } catch (Exception e) {
                            // Si no se puede parsear la fecha, no contar la lección
                        }
                    }
                }
                return leccionesMensuales;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error contando lecciones mensuales para " + usuario + ": " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * 📊 Obtiene una lista de todos los usuarios que han completado lecciones
     * PARA GAMIFICACIÓN: Usar este método para saber qué usuarios existen
     */
    public static List<String> obtenerUsuariosActivos() {
        Set<String> usuariosUnicos = new HashSet<>();
        
        try {
            if (Files.exists(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO))) {
                List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_ESTADISTICAS_COMPARTIDO));
                
                for (String linea : lineas) {
                    if (linea.startsWith("Usuario=")) {
                        String usuario = linea.substring(8);
                        usuariosUnicos.add(usuario);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error obteniendo usuarios activos: " + e.getMessage());
        }
        
        return new ArrayList<>(usuariosUnicos);
    }
    
    /**
     * 📊 Obtiene estadísticas resumidas de un usuario para gamificación
     * PARA GAMIFICACIÓN: Un método todo-en-uno
     */
    public static Map<String, Integer> obtenerEstadisticasUsuario(String usuario) {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        estadisticas.put("xp_total", obtenerXPTotal(usuario));
        estadisticas.put("conocimiento_total", obtenerConocimientoTotal(usuario));
        estadisticas.put("lecciones_totales", obtenerLeccionesCompletadas(usuario));
        estadisticas.put("lecciones_ultima_semana", obtenerLeccionesUltimaSemana(usuario));
        estadisticas.put("lecciones_ultimo_mes", obtenerLeccionesUltimoMes(usuario));
        
        return estadisticas;
    }
}
