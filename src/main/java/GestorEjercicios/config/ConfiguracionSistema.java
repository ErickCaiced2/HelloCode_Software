package GestorEjercicios.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 🔧 Configuración centralizada del sistema GestorEjercicios
 */
public class ConfiguracionSistema {
    private static final Properties properties = new Properties();
    private static boolean cargado = false;
    
    // Configuración por defecto
    private static final String DIRECTORIO_DATA_DEFAULT = "src/main/resources/GestorEjercicios/data/";
    private static final String ARCHIVO_PROGRESO_DEFAULT = "progreso_usuario.json";
    private static final int MAX_EJERCICIOS_POR_LECCION_DEFAULT = 5;
    private static final int MAX_EJERCICIOS_POR_PRUEBA_DEFAULT = 10;
    private static final int MAX_EJERCICIOS_POR_DIAGNOSTICO_DEFAULT = 8;
    
    static {
        cargarConfiguracion();
    }
    
    /**
     * Carga la configuración desde archivo properties o usa valores por defecto
     */
    private static void cargarConfiguracion() {
        if (cargado) return;
        
        try (InputStream input = ConfiguracionSistema.class.getClassLoader()
                .getResourceAsStream("GestorEjercicios/config/aplicacion.properties")) {
            
            if (input != null) {
                properties.load(input);
                System.out.println("✅ Configuración cargada desde archivo properties");
            } else {
                System.out.println("⚠️ Archivo de configuración no encontrado, usando valores por defecto");
                cargarConfiguracionPorDefecto();
            }
        } catch (IOException e) {
            System.err.println("❌ Error cargando configuración: " + e.getMessage());
            cargarConfiguracionPorDefecto();
        }
        
        cargado = true;
    }
    
    /**
     * Carga configuración por defecto cuando no existe archivo
     */
    private static void cargarConfiguracionPorDefecto() {
        properties.setProperty("directorio.data", DIRECTORIO_DATA_DEFAULT);
        properties.setProperty("archivo.progreso", ARCHIVO_PROGRESO_DEFAULT);
        properties.setProperty("ejercicios.max.leccion", String.valueOf(MAX_EJERCICIOS_POR_LECCION_DEFAULT));
        properties.setProperty("ejercicios.max.prueba", String.valueOf(MAX_EJERCICIOS_POR_PRUEBA_DEFAULT));
        properties.setProperty("ejercicios.max.diagnostico", String.valueOf(MAX_EJERCICIOS_POR_DIAGNOSTICO_DEFAULT));
        properties.setProperty("debug.enabled", "true");
        properties.setProperty("persistencia.auto.guardar", "true");
    }
    
    // ===== MÉTODOS DE ACCESO A CONFIGURACIÓN =====
    
    public static String getDirectorioData() {
        return properties.getProperty("directorio.data", DIRECTORIO_DATA_DEFAULT);
    }
    
    public static String getArchivoProgreso() {
        String directorio = getDirectorioData();
        String archivo = properties.getProperty("archivo.progreso", ARCHIVO_PROGRESO_DEFAULT);
        return directorio + archivo;
    }
    
    public static int getMaxEjerciciosPorLeccion() {
        return Integer.parseInt(properties.getProperty("ejercicios.max.leccion", 
                String.valueOf(MAX_EJERCICIOS_POR_LECCION_DEFAULT)));
    }
    
    public static int getMaxEjerciciosPorPrueba() {
        return Integer.parseInt(properties.getProperty("ejercicios.max.prueba", 
                String.valueOf(MAX_EJERCICIOS_POR_PRUEBA_DEFAULT)));
    }
    
    public static int getMaxEjerciciosPorDiagnostico() {
        return Integer.parseInt(properties.getProperty("ejercicios.max.diagnostico", 
                String.valueOf(MAX_EJERCICIOS_POR_DIAGNOSTICO_DEFAULT)));
    }
    
    public static boolean isDebugEnabled() {
        return Boolean.parseBoolean(properties.getProperty("debug.enabled", "true"));
    }
    
    public static boolean isAutoGuardarEnabled() {
        return Boolean.parseBoolean(properties.getProperty("persistencia.auto.guardar", "true"));
    }
    
    /**
     * Obtiene una propiedad personalizada
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Establece una propiedad en tiempo de ejecución
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Información del sistema
     */
    public static void mostrarConfiguracion() {
        if (isDebugEnabled()) {
            System.out.println("🔧 === CONFIGURACIÓN DEL SISTEMA ===");
            System.out.println("   📁 Directorio datos: " + getDirectorioData());
            System.out.println("   💾 Archivo progreso: " + getArchivoProgreso());
            System.out.println("   📚 Max ejercicios lección: " + getMaxEjerciciosPorLeccion());
            System.out.println("   🧪 Max ejercicios prueba: " + getMaxEjerciciosPorPrueba());
            System.out.println("   🔍 Max ejercicios diagnóstico: " + getMaxEjerciciosPorDiagnostico());
            System.out.println("   🐛 Debug habilitado: " + isDebugEnabled());
            System.out.println("   💾 Auto-guardar: " + isAutoGuardarEnabled());
            System.out.println("🔧 === FIN CONFIGURACIÓN ===");
        }
    }
}
