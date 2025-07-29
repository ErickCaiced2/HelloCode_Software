package GestorEjercicios.model;

import GestorEjercicios.enums.TipoLeccion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.strategy.EstrategiaLeccion;
import GestorEjercicios.strategy.EstrategiaLeccionPrueba;
import GestorEjercicios.strategy.FabricaEstrategiasLeccion;
import GestorEjercicios.adaptadores.AdaptadorEjercicios;
import GestorEjercicios.adaptadores.FabricaAdaptadores;
import GestorEjercicios.ConfiguracionGestorEjercicios;

import java.util.List;
import java.util.ArrayList;

public class Leccion {
    private int id;
    private String nombre;
    private List<AdaptadorEjercicios> ejercicios; // Lista unificada de ejercicios usando adaptadores
    private TipoLeccion tipo;
    private EstrategiaLeccion estrategia;
    private int experiencia; // Experiencia otorgada por la lección
    private int conocimiento; // Puntos de conocimiento otorgados por la lección
    private boolean completada = false; // Estado de la lección
    private NivelDificultad dificultad; // Nivel de dificultad de la lección
    private LenguajeProgramacion lenguaje; // Lenguaje de programación de la lección
    private boolean desbloqueada = true; // Estado de desbloqueo de la lección

    /**
     * Constructor para lecciones con experiencia y conocimiento explícitos
     */
    public Leccion(int id, String nombre, List<?> ejercicios, TipoLeccion tipo, int experiencia, int conocimiento) {
        this.id = id;
        this.nombre = nombre;
        this.ejercicios = convertirEjercicios(ejercicios);
        this.tipo = tipo;
        this.estrategia = FabricaEstrategiasLeccion.crearEstrategia(this.tipo);
        this.experiencia = experiencia;
        this.conocimiento = conocimiento;
        this.dificultad = calcularDificultadPorDefecto();
        this.lenguaje = calcularLenguajePorDefecto();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Constructor para lecciones con ejercicios mixtos
     */
    public Leccion(int id, String nombre, List<?> ejercicios) {
        this.id = id;
        this.nombre = nombre;
        this.ejercicios = convertirEjercicios(ejercicios);
        this.tipo = TipoLeccion.NORMAL; // Por defecto
        this.estrategia = FabricaEstrategiasLeccion.crearEstrategia(this.tipo);
        this.experiencia = calcularExperienciaPorDefecto();
        this.conocimiento = calcularConocimientoPorDefecto();
        this.dificultad = calcularDificultadPorDefecto();
        this.lenguaje = calcularLenguajePorDefecto();
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public Leccion(int id, String nombre, List<?> ejercicios, TipoLeccion tipo, 
                   int experiencia, int conocimiento, NivelDificultad dificultad, 
                   LenguajeProgramacion lenguaje) {
        this.id = id;
        this.nombre = nombre;
        this.ejercicios = convertirEjercicios(ejercicios);
        this.tipo = tipo;
        this.estrategia = FabricaEstrategiasLeccion.crearEstrategia(this.tipo);
        this.experiencia = experiencia;
        this.conocimiento = conocimiento;
        this.dificultad = dificultad;
        this.lenguaje = lenguaje;
    }

    /**
     * Convierte una lista de ejercicios a adaptadores
     */
    private List<AdaptadorEjercicios> convertirEjercicios(List<?> ejercicios) {
        List<AdaptadorEjercicios> adaptadores = new ArrayList<>();
        for (Object ejercicio : ejercicios) {
            adaptadores.add(FabricaAdaptadores.crearAdaptador(ejercicio));
        }
        return adaptadores;
    }

    /**
     * Calcula la experiencia por defecto basada en el número de ejercicios
     */
    private int calcularExperienciaPorDefecto() {
        return ejercicios.size() * ConfiguracionGestorEjercicios.EXPERIENCIA_POR_EJERCICIO;
    }

    /**
     * Calcula el conocimiento por defecto basado en el número de ejercicios
     */
    private int calcularConocimientoPorDefecto() {
        return ejercicios.size() * ConfiguracionGestorEjercicios.CONOCIMIENTO_POR_EJERCICIO;
    }

    /**
     * Calcula la dificultad por defecto basada en los ejercicios de la lección
     */
    private NivelDificultad calcularDificultadPorDefecto() {
        if (ejercicios.isEmpty()) {
            return NivelDificultad.BASICO;
        }
        
        // Contar ejercicios por nivel de dificultad
        int basicos = 0, intermedios = 0, avanzados = 0;
        
        for (AdaptadorEjercicios ejercicio : ejercicios) {
            String dificultadStr = ejercicio.obtenerNivelDificultad();
            switch (dificultadStr.toUpperCase()) {
                case "BASICO":
                    basicos++;
                    break;
                case "INTERMEDIO":
                    intermedios++;
                    break;
                case "AVANZADO":
                    avanzados++;
                    break;
            }
        }
        
        // Determinar dificultad predominante
        if (avanzados > intermedios && avanzados > basicos) {
            return NivelDificultad.AVANZADO;
        } else if (intermedios > basicos) {
            return NivelDificultad.INTERMEDIO;
        } else {
            return NivelDificultad.BASICO;
        }
    }

    /**
     * Calcula el lenguaje de programación por defecto basado en los ejercicios de la lección
     */
    private LenguajeProgramacion calcularLenguajePorDefecto() {
        if (ejercicios.isEmpty()) {
            return LenguajeProgramacion.JAVA;
        }
        
        // Contar ejercicios por lenguaje
        int java = 0, python = 0, javascript = 0;
        
        for (AdaptadorEjercicios ejercicio : ejercicios) {
            String lenguajeStr = ejercicio.obtenerLenguaje();
            switch (lenguajeStr.toUpperCase()) {
                case "JAVA":
                    java++;
                    break;
                case "PYTHON":
                    python++;
                    break;
                case "JAVASCRIPT":
                    javascript++;
                    break;
            }
        }
        
        // Determinar lenguaje predominante
        if (python > java && python > javascript) {
            return LenguajeProgramacion.PYTHON;
        } else if (javascript > java) {
            return LenguajeProgramacion.JAVASCRIPT;
        } else {
            return LenguajeProgramacion.JAVA;
        }
    }

    public List<AdaptadorEjercicios> getEjercicios() {
        return new ArrayList<>(ejercicios);
    }

    public String obtenerResumen() {
        return "Lección '" + nombre + "' (" + tipo + ") - " + dificultad + " - " + lenguaje + 
               " con " + ejercicios.size() + " ejercicios.";
    }

    public TipoLeccion getTipo() {
        return tipo;
    }

    public EstrategiaLeccion getEstrategia() {
        return estrategia;
    }

    public boolean tieneEjerciciosPendientes() {
        return estrategia.tieneEjerciciosPendientes();
    }

    public void marcarCompletada() {
        if (tipo == TipoLeccion.PRUEBA) {
            ((EstrategiaLeccionPrueba) estrategia).marcarLeccionCompletada();
        }
    }
    /*
    public Leccion crearLeccionRepaso(String nombreRepaso) {
        if (tipo == TipoLeccion.PRUEBA) {
            return ((EstrategiaLeccionPrueba) estrategia).crearLeccionRepaso(nombreRepaso);
        }
        return null;
    }

     */

    public int getExperiencia() {
        return experiencia;
    }

    public int getConocimiento() {
        return conocimiento;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    /**
     * Obtiene el nivel de dificultad de la lección
     * @return Nivel de dificultad
     */
    public NivelDificultad getDificultad() {
        return dificultad;
    }

    /**
     * Establece el nivel de dificultad de la lección
     * @param dificultad Nivel de dificultad
     */
    public void setDificultad(NivelDificultad dificultad) {
        this.dificultad = dificultad;
    }

    /**
     * Obtiene el lenguaje de programación de la lección
     * @return Lenguaje de programación
     */
    public LenguajeProgramacion getLenguaje() {
        return lenguaje;
    }

    /**
     * Establece el lenguaje de programación de la lección
     * @param lenguaje Lenguaje de programación
     */
    public void setLenguaje(LenguajeProgramacion lenguaje) {
        this.lenguaje = lenguaje;
    }

    /**
     * Verifica si la lección está desbloqueada
     * @return true si está desbloqueada, false en caso contrario
     */
    public boolean isDesbloqueada() {
        return desbloqueada;
    }

    /**
     * Establece el estado de desbloqueo de la lección
     * @param desbloqueada Estado de desbloqueo
     */
    public void setDesbloqueada(boolean desbloqueada) {
        this.desbloqueada = desbloqueada;
    }

    /**
     * Para diagnóstico: calcular conocimiento al finalizar la lección.
     * @param aciertos cantidad de ejercicios correctos
     * @param maxConocimiento máximo conocimiento posible para diagnóstico
     */
    public void calcularConocimientoDiagnostico(int aciertos, int maxConocimiento) {
        if (tipo == TipoLeccion.DIAGNOSTICO && ejercicios.size() > 0) {
            this.conocimiento = Math.round(((float) aciertos / ejercicios.size()) * maxConocimiento);
        }
    }

    /**
     * Obtiene el número total de ejercicios en la lección
     * @return Número de ejercicios
     */
    public int getNumeroEjercicios() {
        return ejercicios.size();
    }

    /**
     * Obtiene un ejercicio específico por índice
     * @param indice Índice del ejercicio
     * @return Ejercicio en el índice especificado
     */
    public AdaptadorEjercicios getEjercicio(int indice) {
        if (indice >= 0 && indice < ejercicios.size()) {
            return ejercicios.get(indice);
        }
        return null;
    }
}
