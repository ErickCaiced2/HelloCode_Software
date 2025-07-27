package GestorEjercicios.integracion;

import GestorEjercicios.model.Leccion;
import GestorEjercicios.model.ResultadoEvaluacion;
import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.LenguajeProgramacion;
import Modulo_Usuario.Clases.Usuario;

import java.util.List;

/**
 * Interfaz de integración para el módulo GestorEjercicios
 * Permite a otros módulos usar el gestor de ejercicios de manera independiente
 */
public interface IGestorEjercicios {
    
    /**
     * Crea una nueva lección con ejercicios proporcionados
     * @param nombre Nombre de la lección
     * @param ejercicios Lista de ejercicios (puede ser EjercicioSeleccion, EjercicioCompletarCodigo, etc.)
     * @param tipo Tipo de lección (NORMAL, PRUEBA)
     * @param experiencia Experiencia que otorga la lección
     * @param conocimiento Conocimiento que otorga la lección
     * @return Lección creada
     */
    Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento);

    /**
     * Crea una nueva lección con todos los parámetros incluyendo dificultad y lenguaje
     * @param nombre Nombre de la lección
     * @param ejercicios Lista de ejercicios
     * @param tipo Tipo de lección
     * @param experiencia Experiencia que otorga la lección
     * @param conocimiento Conocimiento que otorga la lección
     * @param dificultad Nivel de dificultad de la lección
     * @param lenguaje Lenguaje de programación de la lección
     * @return Lección creada
     */
    Leccion crearLeccion(String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento, 
                        NivelDificultad dificultad, LenguajeProgramacion lenguaje);
    
    /**
     * Obtiene una lección por su ID
     * @param id ID de la lección
     * @return Lección encontrada o null si no existe
     */
    Leccion obtenerLeccion(int id);
    
    /**
     * Obtiene todas las lecciones disponibles
     * @return Lista de todas las lecciones
     */
    List<Leccion> obtenerTodasLasLecciones();
    
    /**
     * Ejecuta una lección para un usuario específico
     * @param leccion Lección a ejecutar
     * @param usuario Usuario que ejecuta la lección
     * @return Resultado de la evaluación
     */
    ResultadoEvaluacion ejecutarLeccion(Leccion leccion, Usuario usuario);
    
    /**
     * Marca una lección como completada para un usuario
     * @param leccion Lección completada
     * @param usuario Usuario que completó la lección
     * @param aciertos Número de ejercicios correctos
     */
    void marcarLeccionCompletada(Leccion leccion, Usuario usuario, int aciertos);
    
    /**
     * Obtiene el progreso de un usuario en una lección específica
     * @param leccion Lección
     * @param usuario Usuario
     * @return Progreso (0.0 a 1.0)
     */
    double obtenerProgresoUsuario(Leccion leccion, Usuario usuario);
    
    /**
     * Obtiene las estadísticas de un usuario
     * @param usuario Usuario
     * @return Estadísticas del usuario
     */
    EstadisticasUsuario obtenerEstadisticasUsuario(Usuario usuario);
    
    /**
     * Clase interna para estadísticas del usuario
     */
    class EstadisticasUsuario {
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