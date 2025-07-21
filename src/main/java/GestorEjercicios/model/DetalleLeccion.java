package GestorEjercicios.model;

public class DetalleLeccion {
    private Ejercicio ejercicio;
    private ResultadoEvaluacion resultado;

    public DetalleLeccion(Ejercicio ejercicio, ResultadoEvaluacion resultado) {
        this.ejercicio = ejercicio;
        this.resultado = resultado;
    }

    public Ejercicio getEjercicio() { return ejercicio; }
    public ResultadoEvaluacion getResultado() { return resultado; }

    public void resolverEjercicio(String respuestaUsuario) {
        this.resultado = ejercicio.resolver(respuestaUsuario);
    }
}