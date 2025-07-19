package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comentario {
    private String idComentario;
    private String contenido;
    private UsuarioTemp autor;
    private LocalDateTime fechaPublicacion;
    
    public Comentario(String contenido, UsuarioTemp autor) {
        this.idComentario = UUID.randomUUID().toString();
        this.contenido = contenido;
        this.autor = autor;
        this.fechaPublicacion = LocalDateTime.now();
    }
    
    // Getters y setters
    public String getIdComentario() {
        return idComentario;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public UsuarioTemp getAutor() {
        return autor;
    }
    
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
    
    // Métodos de negocio
    public void editar(String nuevoContenido) {
        this.contenido = nuevoContenido;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", 
                           fechaPublicacion.toString(), 
                           autor.getNombre(), 
                           contenido);
    }
}
