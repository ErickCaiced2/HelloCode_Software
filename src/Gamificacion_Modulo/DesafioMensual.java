package Gamificacion_Modulo;

import java.util.List;

public class DesafioMensual extends Desafio {
    private Integer objetivoMensual;
    private Integer actividadesCompletadas;
    private Integer leccionesCompletadas;

    public DesafioMensual(Integer objetivoMensual, List<Logro> logros) {
        super("Desafio Mensual", "Completar " + objetivoMensual + " actividades en el mes", logros);
        this.objetivoMensual = objetivoMensual;
        this.actividadesCompletadas = 0;
        this.leccionesCompletadas = 0;
    }

    @Override
    public String definirCriterios() {
        return "Completar " + objetivoMensual + " actividades mensuales";
    }

    @Override
    public Boolean estaCompletado() {
        return actividadesCompletadas >= objetivoMensual;
    }

    public void actualizarActividades(Integer cantidad) {
        this.actividadesCompletadas += cantidad;
        System.out.println(">> Actividades mensuales: " + actividadesCompletadas + "/" + objetivoMensual);
    }

    public void registrarLeccion() {
        this.leccionesCompletadas++;
        actualizarActividades(1);
    }

    public Double getProgreso() {
        if (objetivoMensual == 0) return 0.0;
        double progreso = (actividadesCompletadas * 100.0) / objetivoMensual;
        return Math.min(progreso, 100.0);
    }

    // Getters
    public Integer getObjetivoMensual() { return objetivoMensual; }
    public Integer getActividadesCompletadas() { return actividadesCompletadas; }
} 