package GestorEjercicios;

import GestorEjercicios.enums.TipoLeccion;

/**
 * Configuración del módulo GestorEjercicios
 * Contiene constantes y configuraciones del sistema
 */
public class ConfiguracionGestorEjercicios {
    
    // Configuración de experiencia por defecto
    public static final int EXPERIENCIA_LECCION_NORMAL = 15;
    public static final int EXPERIENCIA_LECCION_PRUEBA = 30;
    public static final int CONOCIMIENTO_LECCION_NORMAL = 5;
    public static final int CONOCIMIENTO_LECCION_PRUEBA = 0;
    
    // Configuración de bonus de rendimiento
    public static final double UMBRAL_BONUS_EXPERIENCIA = 0.8; // 80%
    public static final double UMBRAL_PENALIZACION_EXPERIENCIA = 0.6; // 60%
    public static final double MULTIPLICADOR_BONUS = 1.2; // +20%
    public static final double MULTIPLICADOR_PENALIZACION = 0.5; // 50%
    
    // Configuración de reputación
    public static final int DIVISOR_REPUTACION = 10; // XP / 10 = reputación
    
    // Configuración de límites
    public static final int MAX_EJERCICIOS_POR_LECCION_NORMAL = 5;
    public static final int MAX_EJERCICIOS_POR_LECCION_PRUEBA = 10;
    public static final int MIN_EJERCICIOS_POR_LECCION = 1;
    
    // Configuración de experiencia por ejercicio
    public static final int EXPERIENCIA_POR_EJERCICIO = 10;
    public static final int CONOCIMIENTO_POR_EJERCICIO = 5;
    
    // Configuración de nombres por defecto
    public static final String NOMBRE_LECCION_NORMAL_POR_DEFECTO = "Lección Normal";
    public static final String NOMBRE_LECCION_PRUEBA_POR_DEFECTO = "Lección de Prueba";
    
    // Configuración de mensajes
    public static final String MENSAJE_LECCION_CREADA = "Lección '%s' creada exitosamente";
    public static final String MENSAJE_LECCION_COMPLETADA = "Lección '%s' completada por %s: %d/%d ejercicios correctos";
    public static final String MENSAJE_EXPERIENCIA_GANADA = "XP: +%d, Conocimiento: +%d";
    public static final String MENSAJE_BONUS_RENDIMIENTO = "¡Bonus por buen rendimiento!";
    public static final String MENSAJE_PENALIZACION_RENDIMIENTO = "Rendimiento bajo, experiencia reducida";
    
    // Configuración de errores
    public static final String ERROR_NOMBRE_VACIO = "El nombre de la lección no puede estar vacío";
    public static final String ERROR_EJERCICIOS_VACIOS = "La lección debe contener al menos un ejercicio";
    public static final String ERROR_USUARIO_NULO = "El usuario no puede ser nulo";
    public static final String ERROR_LECCION_NULA = "La lección no puede ser nula";
    public static final String ERROR_ACIERTOS_INVALIDOS = "El número de aciertos debe estar entre 0 y %d";
    public static final String ERROR_TIPO_EJERCICIO_NO_SOPORTADO = "Tipo de ejercicio no soportado: %s";
    
    // Configuración de formato
    public static final String FORMATO_PORCENTAJE = "%.1f%%";
    public static final String FORMATO_ESTADO_MODULO = "GestorEjercicios - Total lecciones: %d (Normales: %d, Pruebas: %d)";
    public static final String FORMATO_RESUMEN_LECCION = "Lección '%s' (%s) con %d ejercicios.";
    
    // Configuración de inicialización
    public static final String MENSAJE_INICIALIZACION = "=== Módulo GestorEjercicios inicializado ===";
    public static final String MENSAJE_GESTOR_LISTO = "Gestor de ejercicios listo para recibir lecciones";
    public static final String MENSAJE_TIPOS_LECCIONES = "Tipos de lecciones soportados: NORMAL, PRUEBA";
    public static final String MENSAJE_TIPOS_EJERCICIOS = "Tipos de ejercicios soportados: Selección múltiple, Completar código";
    public static final String MENSAJE_SEPARADOR = "=============================================";
    
    /**
     * Calcula la experiencia ganada basada en el rendimiento
     */
    public static int calcularExperienciaGanada(int experienciaBase, double porcentajeAcierto) {
        if (porcentajeAcierto >= UMBRAL_BONUS_EXPERIENCIA) {
            return (int) (experienciaBase * MULTIPLICADOR_BONUS);
        } else if (porcentajeAcierto >= UMBRAL_PENALIZACION_EXPERIENCIA) {
            return experienciaBase;
        } else {
            return (int) (experienciaBase * MULTIPLICADOR_PENALIZACION);
        }
    }
    
    /**
     * Calcula el conocimiento ganado basado en el rendimiento
     */
    public static int calcularConocimientoGanado(int conocimientoBase, double porcentajeAcierto) {
        return (int) (conocimientoBase * porcentajeAcierto);
    }
    
    /**
     * Calcula la reputación ganada basada en la experiencia
     */
    public static int calcularReputacionGanada(int experienciaGanada) {
        return experienciaGanada / DIVISOR_REPUTACION;
    }
    
    /**
     * Valida el número de ejercicios para un tipo de lección
     */
    public static boolean validarNumeroEjercicios(int numeroEjercicios, TipoLeccion tipo) {
        if (numeroEjercicios < MIN_EJERCICIOS_POR_LECCION) {
            return false;
        }
        
        switch (tipo) {
            case NORMAL:
                return numeroEjercicios <= MAX_EJERCICIOS_POR_LECCION_NORMAL;
            case PRUEBA:
                return numeroEjercicios <= MAX_EJERCICIOS_POR_LECCION_PRUEBA;
            default:
                return true;
        }
    }
    
    /**
     * Obtiene el límite máximo de ejercicios para un tipo de lección
     */
    public static int obtenerMaximoEjercicios(TipoLeccion tipo) {
        switch (tipo) {
            case NORMAL:
                return MAX_EJERCICIOS_POR_LECCION_NORMAL;
            case PRUEBA:
                return MAX_EJERCICIOS_POR_LECCION_PRUEBA;
            default:
                return MAX_EJERCICIOS_POR_LECCION_NORMAL;
        }
    }
    
    /**
     * Obtiene la experiencia por defecto para un tipo de lección
     */
    public static int obtenerExperienciaPorDefecto(TipoLeccion tipo) {
        switch (tipo) {
            case NORMAL:
                return EXPERIENCIA_LECCION_NORMAL;
            case PRUEBA:
                return EXPERIENCIA_LECCION_PRUEBA;
            default:
                return EXPERIENCIA_LECCION_NORMAL;
        }
    }
    
    /**
     * Obtiene el conocimiento por defecto para un tipo de lección
     */
    public static int obtenerConocimientoPorDefecto(TipoLeccion tipo) {
        switch (tipo) {
            case NORMAL:
                return CONOCIMIENTO_LECCION_NORMAL;
            case PRUEBA:
                return CONOCIMIENTO_LECCION_PRUEBA;
            default:
                return CONOCIMIENTO_LECCION_NORMAL;
        }
    }
} 