package GestorEjercicios.adaptadores;

import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.Respuesta;
import Modulo_Ejercicios.exercises.RespuestaString;
import Modulo_Ejercicios.exercises.ResultadoDeEvaluacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adaptador para ejercicios de completar código
 */
public class AdaptadorEjercicioCompletarCodigo implements AdaptadorEjercicios {
    
    private final EjercicioCompletarCodigo ejercicio;
    private final String id;
    
    public AdaptadorEjercicioCompletarCodigo(EjercicioCompletarCodigo ejercicio) {
        this.ejercicio = ejercicio;
        this.id = UUID.randomUUID().toString();
    }
    
    @Override
    public String obtenerId() {
        return id;
    }
    
    @Override
    public String obtenerInstruccion() {
        return ejercicio.getInstruccion();
    }
    
    @Override
    public String obtenerNivelDificultad() {
        return ejercicio.getNivel().toString();
    }
    
    @Override
    public String obtenerLenguaje() {
        return ejercicio.getLenguaje().toString();
    }
    
    @Override
    public ResultadoEvaluacion evaluarRespuestas(List<String> respuestasUsuario) {
        // Convertir las respuestas del usuario al formato esperado por EjercicioCompletarCodigo
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        for (String respuesta : respuestasUsuario) {
            respuestas.add(new RespuestaString(respuesta));
        }
        
        // Evaluar usando el método original del ejercicio
        ResultadoDeEvaluacion resultadoOriginal = ejercicio.evaluarRespuestas(respuestas);
        
        // Convertir el resultado al formato del adaptador
        boolean esCorrecto = resultadoOriginal.getPorcentajeDeAcerto() == 100;
        String mensaje = esCorrecto ? "¡Correcto! El código está completo y correcto." : 
                                   "Incorrecto. Respuestas esperadas: " + obtenerRespuestasCorrectas();
        
        return new ResultadoEvaluacion(
            resultadoOriginal.getPorcentajeDeAcerto(),
            esCorrecto,
            mensaje,
            obtenerRespuestasCorrectas()
        );
    }
    
    @Override
    public List<String> obtenerRespuestasCorrectas() {
        return ejercicio.obtenerRespuestasEsperadas();
    }
    
    @Override
    public String obtenerTipoEjercicio() {
        return "COMPLETAR_CODIGO";
    }
    
    /**
     * Obtiene el código incompleto del ejercicio
     * @return Código incompleto
     */
    public String obtenerCodigoIncompleto() {
        return ejercicio.obtenerCodigoIncompleto();
    }
    
    /**
     * Obtiene las partes faltantes del código
     * @return Lista de partes faltantes
     */
    public List<String> obtenerPartesFaltantes() {
        return ejercicio.obtenerPartesFaltantes();
    }
    
    /**
     * Obtiene el número de partes faltantes
     * @return Número de partes faltantes
     */
    public int obtenerNumeroPartesFaltantes() {
        return ejercicio.obtenerNumeroPartesFaltantes();
    }
    
    /**
     * Obtiene el ejercicio original
     * @return Ejercicio de completar código original
     */
    public EjercicioCompletarCodigo obtenerEjercicioOriginal() {
        return ejercicio;
    }
} 