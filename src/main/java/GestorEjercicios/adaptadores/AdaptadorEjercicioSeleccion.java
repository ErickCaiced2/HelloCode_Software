package GestorEjercicios.adaptadores;

import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import Modulo_Ejercicios.exercises.Respuesta;
import Modulo_Ejercicios.exercises.RespuestaString;
import Modulo_Ejercicios.exercises.ResultadoDeEvaluacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adaptador para ejercicios de selección múltiple
 */
public class AdaptadorEjercicioSeleccion implements AdaptadorEjercicios {
    
    private final EjercicioSeleccion ejercicio;
    private final String id;
    
    public AdaptadorEjercicioSeleccion(EjercicioSeleccion ejercicio) {
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
        // Convertir las respuestas del usuario al formato esperado por EjercicioSeleccion
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        for (String respuesta : respuestasUsuario) {
            respuestas.add(new RespuestaString(respuesta));
        }
        
        // Evaluar usando el método original del ejercicio
        ResultadoDeEvaluacion resultadoOriginal = ejercicio.evaluarRespuestas(respuestas);
        
        // Convertir el resultado al formato del adaptador
        boolean esCorrecto = resultadoOriginal.getPorcentajeDeAcerto() == 100;
        String mensaje = esCorrecto ? "¡Correcto! Todas las respuestas son correctas." : 
                                   "Incorrecto. Respuestas correctas: " + obtenerRespuestasCorrectas();
        
        return new ResultadoEvaluacion(
            resultadoOriginal.getPorcentajeDeAcerto(),
            esCorrecto,
            mensaje,
            obtenerRespuestasCorrectas()
        );
    }
    
    @Override
    public List<String> obtenerRespuestasCorrectas() {
        return ejercicio.obtenerRespuestasCorrectas();
    }
    
    @Override
    public String obtenerTipoEjercicio() {
        return "SELECCION_MULTIPLE";
    }
    
    /**
     * Obtiene las opciones disponibles para este ejercicio
     * @return Lista de opciones
     */
    public List<String> obtenerOpciones() {
        return ejercicio.getListOpciones();
    }
    
    /**
     * Obtiene el ejercicio original
     * @return Ejercicio de selección original
     */
    public EjercicioSeleccion obtenerEjercicioOriginal() {
        return ejercicio;
    }
} 