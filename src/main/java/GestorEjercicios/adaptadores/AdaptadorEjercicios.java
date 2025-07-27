package GestorEjercicios.adaptadores;

import java.util.List;

/**
 * Adaptador para manejar diferentes tipos de ejercicios de manera uniforme
 * Permite que el GestorEjercicios trabaje con cualquier tipo de ejercicio
 */
public interface AdaptadorEjercicios {
    
    /**
     * Obtiene el ID único del ejercicio
     * @return ID del ejercicio
     */
    String obtenerId();
    
    /**
     * Obtiene la instrucción del ejercicio
     * @return Instrucción del ejercicio
     */
    String obtenerInstruccion();
    
    /**
     * Obtiene el nivel de dificultad del ejercicio
     * @return Nivel de dificultad
     */
    String obtenerNivelDificultad();
    
    /**
     * Obtiene el lenguaje de programación del ejercicio
     * @return Lenguaje de programación
     */
    String obtenerLenguaje();
    
    /**
     * Evalúa las respuestas del usuario
     * @param respuestasUsuario Respuestas proporcionadas por el usuario
     * @return Resultado de la evaluación
     */
    ResultadoEvaluacion evaluarRespuestas(List<String> respuestasUsuario);
    
    /**
     * Obtiene las respuestas correctas del ejercicio
     * @return Lista de respuestas correctas
     */
    List<String> obtenerRespuestasCorrectas();
    
    /**
     * Obtiene el tipo de ejercicio
     * @return Tipo de ejercicio
     */
    String obtenerTipoEjercicio();
    
    /**
     * Clase para el resultado de evaluación
     */
    class ResultadoEvaluacion {
        private double porcentajeAcierto;
        private boolean esCorrecto;
        private String mensaje;
        private List<String> respuestasCorrectas;
        
        public ResultadoEvaluacion(double porcentajeAcierto, boolean esCorrecto, String mensaje, List<String> respuestasCorrectas) {
            this.porcentajeAcierto = porcentajeAcierto;
            this.esCorrecto = esCorrecto;
            this.mensaje = mensaje;
            this.respuestasCorrectas = respuestasCorrectas;
        }
        
        // Getters
        public double getPorcentajeAcierto() { return porcentajeAcierto; }
        public boolean isCorrecto() { return esCorrecto; }
        public String getMensaje() { return mensaje; }
        public List<String> getRespuestasCorrectas() { return respuestasCorrectas; }
    }
} 