package Gamificacion_Modulo;

import java.util.Date;

public class Logro {
    private static Long contadorId = 1L;
    private Long id;
    private String nombre;
    private String descripcion;
    private String criteriosDesbloqueo;
    private Integer puntos;
    private Date fecha;

    public Logro(String nombre, String descripcion, String criterios, Integer puntos) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.criteriosDesbloqueo = criterios;
        this.puntos = puntos;
        this.fecha = new Date();
    }

    public Boolean cumpleCriterios(ProgresoEstudiante progreso) {
        // Sistema escalable de criterios
        String[] partes = criteriosDesbloqueo.split(":");
        if (partes.length < 2) return false;
        
        String tipo = partes[0];
        int valor = Integer.parseInt(partes[1]);
        
        switch (tipo) {
            case "desafios_completados":
                return progreso.getDesafiosCompletados() >= valor;
            case "puntos_totales":
                return progreso.getPuntosTotal() >= valor;
            case "logros_obtenidos":
                return progreso.getLogros().size() >= valor;
            default:
                return false;
        }
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Integer getPuntos() { return puntos; }
    public Long getId() { return id; }
    public String getCriteriosDesbloqueo() { return criteriosDesbloqueo; }
} 