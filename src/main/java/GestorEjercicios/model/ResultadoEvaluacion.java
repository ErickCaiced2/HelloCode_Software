package GestorEjercicios.model;

import MetodosGlobales.SesionManager;

public class ResultadoEvaluacion {
    private boolean esCorrecto;
    private int puntuacionObtenida;
    private int tiempoEmpleado;
    private String obtenerMensaje;
    private boolean hayEjercicioSiguiente;

    public ResultadoEvaluacion(boolean esCorrecto, int puntuacionObtenida, int tiempoEmpleado, String obtenerMensaje, boolean hayEjercicioSiguiente) {
        this.esCorrecto = esCorrecto;
        this.puntuacionObtenida = puntuacionObtenida;
        this.tiempoEmpleado = tiempoEmpleado;
        this.obtenerMensaje = obtenerMensaje;
        this.hayEjercicioSiguiente = hayEjercicioSiguiente;
    }

    public boolean isCorrecto() { return esCorrecto; }
    public int getPuntuacionObtenida() { return puntuacionObtenida; }
    public String getObtenerMensaje() { return obtenerMensaje; }
    public int getTiempoEmpleado() { return tiempoEmpleado; }
    public boolean isHayEjercicioSiguiente() { return hayEjercicioSiguiente; }

    public static boolean evaluarLeccion(int totalEjercicios, int ejerciciosCorrectos) {
        double porcentaje = (ejerciciosCorrectos * 100.0) / totalEjercicios;
        return porcentaje >= 60.0;
    }
    
    /**
     * 🎯 NUEVO: Evalúa lección Y guarda progreso automáticamente
     * PARA GAMIFICACIÓN: Este método actualiza todo el sistema
     * 
     * @param usuario El username del usuario (opcional, usa SesionManager si es null)
     * @param tituloLeccion Título de la lección
     * @param totalEjercicios Total de ejercicios en la lección
     * @param ejerciciosCorrectos Ejercicios respondidos correctamente
     * @param tiempoSegundos Tiempo empleado en segundos
     * @param lenguaje Lenguaje de programación
     * @param nivel Nivel de dificultad
     */
    public static ResultadoEvaluacion evaluarYGuardarLeccion(
            String usuario, String tituloLeccion, 
            int totalEjercicios, int ejerciciosCorrectos, 
            int tiempoSegundos, String lenguaje, String nivel) {
        
        // 🔑 Obtener usuario actual del SesionManager si no se proporciona
        String usuarioFinal = usuario;
        if (usuarioFinal == null || usuarioFinal.trim().isEmpty()) {
            if (SesionManager.getInstancia().hayUsuarioAutenticado()) {
                usuarioFinal = SesionManager.getInstancia().getUsernameActual();
            } else {
                usuarioFinal = "Usuario_Anonimo";
            }
        }
        
        // Evaluar si pasó la lección
        boolean aprobado = evaluarLeccion(totalEjercicios, ejerciciosCorrectos);
        
        // Calcular puntuación basada en correctos y tiempo
        int puntuacion = calcularPuntuacion(ejerciciosCorrectos, totalEjercicios, tiempoSegundos);
        
        // Generar mensaje según resultado
        String mensaje = generarMensaje(aprobado, ejerciciosCorrectos, totalEjercicios, puntuacion);
        
        if (aprobado) {
            // 💾 GUARDAR PROGRESO usando nuestro sistema integrado
            try {
                GestorEjercicios.model.ProgresoSimple.registrarLeccionCompletada(
                    usuarioFinal, 1, tituloLeccion, lenguaje, nivel,
                    puntuacion, tiempoSegundos, totalEjercicios
                );
                
                System.out.println("✅ Progreso guardado para " + usuarioFinal + " - " + tituloLeccion);
                
                // 🎮 NOTIFICAR A GAMIFICACIÓN (si está disponible)
                try {
                    // Usar reflexión para evitar dependencias directas
                    Class<?> gamificacionMain = Class.forName("Gamificacion_Modulo.Main");
                    java.lang.reflect.Method getUsuarios = gamificacionMain.getMethod("getUsuarios");
                    Object usuarios = getUsuarios.invoke(null);
                    
                    if (usuarios != null) {
                        java.lang.reflect.Method notificar = gamificacionMain.getMethod("notificarActualizacionInterface");
                        notificar.invoke(null);
                        System.out.println("🎮 Gamificación notificada de nueva lección completada");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Gamificación no disponible para notificación");
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error guardando progreso: " + e.getMessage());
            }
        }
        
        return new ResultadoEvaluacion(aprobado, puntuacion, tiempoSegundos, mensaje, false);
    }
    
    /**
     * 🎯 SOBRECARGA: Evalúa lección usando automáticamente el usuario actual del SesionManager
     */
    public static ResultadoEvaluacion evaluarYGuardarLeccion(
            String tituloLeccion, int totalEjercicios, int ejerciciosCorrectos, 
            int tiempoSegundos, String lenguaje, String nivel) {
        
        return evaluarYGuardarLeccion(null, tituloLeccion, totalEjercicios, 
                                      ejerciciosCorrectos, tiempoSegundos, lenguaje, nivel);
    }
    
    /**
     * 🧮 Calcula puntuación basada en aciertos y eficiencia de tiempo
     */
    private static int calcularPuntuacion(int correctos, int total, int tiempoSegundos) {
        // Puntuación base por aciertos
        double porcentajeAciertos = (correctos * 100.0) / total;
        int puntuacionBase = (int) (porcentajeAciertos * 10); // 0-1000 puntos
        
        // Bonus por velocidad (máximo 20% extra)
        int tiempoOptimo = total * 30; // 30 segundos por ejercicio óptimo
        if (tiempoSegundos < tiempoOptimo) {
            double bonusVelocidad = 1.0 - ((double) tiempoSegundos / tiempoOptimo);
            puntuacionBase += (int) (puntuacionBase * bonusVelocidad * 0.2);
        }
        
        return Math.max(0, Math.min(1200, puntuacionBase)); // Entre 0-1200 puntos
    }
    
    /**
     * 💬 Genera mensaje personalizado según el rendimiento
     */
    private static String generarMensaje(boolean aprobado, int correctos, int total, int puntuacion) {
        double porcentaje = (correctos * 100.0) / total;
        
        if (!aprobado) {
            return String.format("No aprobaste esta vez. Obtuviste %.1f%% (%d/%d). " +
                                "Necesitas al menos 60%% para continuar. ¡Inténtalo de nuevo!", 
                                porcentaje, correctos, total);
        }
        
        if (porcentaje >= 95) {
            return String.format("¡EXCELENTE! Perfecto %d/%d (%.1f%%). Puntuación: %d pts. " +
                                "¡Eres increíble!", correctos, total, porcentaje, puntuacion);
        } else if (porcentaje >= 85) {
            return String.format("¡MUY BIEN! %d/%d correctos (%.1f%%). Puntuación: %d pts. " +
                                "¡Sigue así!", correctos, total, porcentaje, puntuacion);
        } else if (porcentaje >= 70) {
            return String.format("¡BIEN! %d/%d correctos (%.1f%%). Puntuación: %d pts. " +
                                "Buen trabajo.", correctos, total, porcentaje, puntuacion);
        } else {
            return String.format("Aprobado. %d/%d correctos (%.1f%%). Puntuación: %d pts. " +
                                "Puedes mejorar en la siguiente.", correctos, total, porcentaje, puntuacion);
        }
    }
}