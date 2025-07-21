package GestorEjercicios.model;

import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.enums.TipoEjercicio;

import java.util.List;

public class Ejercicio {
    private int id;
    private String titulo;
    private NivelDificultad dificultad;
    private LenguajeProgramacion lenguaje;
    private TipoEjercicio tipo;
    private List<String> etiquetas;
    private String enunciado;
    private String respuestaCorrecta;

    public Ejercicio(int id, String titulo, NivelDificultad dificultad, LenguajeProgramacion lenguaje, TipoEjercicio tipo, List<String> etiquetas) {
        this.id = id;
        this.titulo = titulo;
        this.dificultad = dificultad;
        this.lenguaje = lenguaje;
        this.tipo = tipo;
        this.etiquetas = etiquetas;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public NivelDificultad getDificultad() { return dificultad; }
    public LenguajeProgramacion getLenguaje() { return lenguaje; }
    public TipoEjercicio getTipo() { return tipo; }
    public List<String> getEtiquetas() { return etiquetas; }

    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getEnunciado() { return enunciado; }

    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }

    public ResultadoEvaluacion resolver(String respuestaUsuario) {
        boolean esCorrecta = respuestaUsuario.trim().equalsIgnoreCase(respuestaCorrecta.trim());
        int puntuacion = esCorrecta ? 100 : 0;
        String mensaje = esCorrecta ? "¡Correcto!" : "Incorrecto. Respuesta esperada: " + respuestaCorrecta;
        return new ResultadoEvaluacion(esCorrecta, puntuacion, 30, mensaje, false);
    }

    @Override
    public String toString() {
        return titulo + " [" + lenguaje + ", " + dificultad + ", " + tipo + "]";
    }
}