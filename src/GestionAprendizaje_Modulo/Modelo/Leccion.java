package GestionAprendizaje_Modulo.Modelo;

public class Leccion {
    private final String id;
    private String titulo;
    private String descripcion;
    private String estado;

    public Leccion(String id, String titulo, String descripcion, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }

    // Setters y acciones
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void actualizarContenido(String nuevoContenido) {
        this.descripcion = nuevoContenido;
    }

    public void marcarComoCompletada() {
        this.estado = "COMPLETADA";
    }
}
