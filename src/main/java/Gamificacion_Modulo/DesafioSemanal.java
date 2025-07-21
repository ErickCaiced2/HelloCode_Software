package Gamificacion_Modulo;

import java.util.List;

public class DesafioSemanal extends Desafio {
    private Integer metaSemanal;
    private Integer actividadesCompletadas;
    private Integer leccionesCompletadas;

    public DesafioSemanal(Integer metaSemanal, List<Logro> logros) {
        super("Desafio Semanal", "Completar " + metaSemanal + " actividades en la semana", logros);
        this.metaSemanal = metaSemanal;
        this.actividadesCompletadas = 0;
        this.leccionesCompletadas = 0;
    }

    @Override
    public String definirCriterios() {
        return "Completar " + metaSemanal + " actividades semanales";
    }

    @Override
    public Boolean estaCompletado() {
        return actividadesCompletadas >= metaSemanal;
    }

    public void actualizarActividades(Integer cantidad) {
        this.actividadesCompletadas += cantidad;
        System.out.println(">> Actividades completadas: " + actividadesCompletadas + "/" + metaSemanal);
    }

    public void registrarLeccion() {
        this.leccionesCompletadas++;
        // También cuenta como actividad
        actualizarActividades(1);
    }

    public Double getProgreso() {
        if (metaSemanal == 0) return 0.0;
        double progreso = (actividadesCompletadas * 100.0) / metaSemanal;
        return Math.min(progreso, 100.0); // Máximo 100%
    }

    // Getters
    public Integer getMetaSemanal() { return metaSemanal; }
    public Integer getActividadesCompletadas() { return actividadesCompletadas; }
} 