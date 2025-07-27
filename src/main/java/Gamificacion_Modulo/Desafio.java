package Gamificacion_Modulo;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Desafio {
    private static Long contadorId = 1L;
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer puntosRecompensa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean estaActivo;
    private List<Logro> logrosDisponibles;

    public Desafio(String nombre, String descripcion, List<Logro> logros) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.logrosDisponibles = logros;
        this.estaActivo = false;
        this.puntosRecompensa = 200; // Puntos base por completar desafío
    }

    public void activar() {
        this.estaActivo = true;
        this.fechaInicio = LocalDateTime.now();
        System.out.println(">>> Desafio '" + nombre + "' activado!");
    }

    public void desactivar() {
        this.estaActivo = false;
        this.fechaFin = LocalDateTime.now();
    }

    public Boolean verificarComplecion(ProgresoEstudiante estudiante) {
        if (!estaCompletado()) {
            return false;
        }

        boolean logroDesbloqueado = false;
        // Iterar por logros disponibles
        for (Logro logro : logrosDisponibles) {
            if (logro.cumpleCriterios(estudiante)) {
                if (desbloquearLogro(logro, estudiante)) {
                    logroDesbloqueado = true;
                }
            }
        }
        return true; // Desafío verificado
    }

    public Boolean desbloquearLogro(Logro logro, ProgresoEstudiante estudiante) {
        // Verificar que el logro no esté ya desbloqueado
        if (!estudiante.getLogros().contains(logro)) {
            estudiante.agregarLogro(logro);
            return true;
        }
        return false;
    }

    public void completarDesafio(ProgresoEstudiante estudiante) {
        // Sumar puntos de recompensa
        estudiante.sumarPuntos(puntosRecompensa);
        // Desactivar el desafío
        desactivar();
        System.out.println(">>> Desafio '" + nombre + "' completado! (+" + puntosRecompensa + " puntos)");
    }

    public List<Logro> getLogrosDisponibles() {
        return logrosDisponibles;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Boolean getEstaActivo() { return estaActivo; }

    public abstract String definirCriterios();
    public abstract Boolean estaCompletado();
} 