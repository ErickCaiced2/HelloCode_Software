package GestorEjercicios.model;

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

    public static boolean evaluarLeccion(int totalEjercicios, int ejerciciosCorrectos) {
        double porcentaje = (ejerciciosCorrectos * 100.0) / totalEjercicios;
        return porcentaje >= 60.0;
    }
}