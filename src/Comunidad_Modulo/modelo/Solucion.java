package modelo;

import enums.TipoSolucion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Solucion {
    private String idSolucion;
    private String titulo;
    private String contenido;
    private UsuarioTemp autor;
    private TipoSolucion tipoSolucion;
    private String archivo;
    private LocalDateTime fechaPublicacion;
    private Integer likes;
    private List<Comentario> comentarios;
    
    public Solucion(String titulo, String contenido, UsuarioTemp autor, TipoSolucion tipoSolucion) {
        this.idSolucion = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.contenido = contenido;
        this.autor = autor;
        this.tipoSolucion = tipoSolucion;
        this.fechaPublicacion = LocalDateTime.now();
        this.likes = 0;
        this.comentarios = new ArrayList<>();
    }
    
    // Getters y setters
    public String getIdSolucion() {
        return idSolucion;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
    
    public TipoSolucion getTipoSolucion() {
        return tipoSolucion;
    }
    
    public void setTipoSolucion(TipoSolucion tipoSolucion) {
        this.tipoSolucion = tipoSolucion;
    }
    
    public String getArchivo() {
        return archivo;
    }
    
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
    
    public Integer getLikes() {
        return likes;
    }
    
    public List<Comentario> getComentarios() {
        return new ArrayList<>(comentarios);
    }
    
    // Métodos de negocio
    public void darLike() {
        this.likes++;
    }
    
    public void quitarLike() {
        this.likes = Math.max(0, this.likes - 1);
    }
    
    public void comentar(String contenido, UsuarioTemp autor) {
        Comentario comentario = new Comentario(contenido, autor);
        comentarios.add(comentario);
    }
    
    public void eliminarComentario(String idComentario) {
        comentarios.removeIf(c -> c.getIdComentario().equals(idComentario));
    }
    
    @Override
    public String toString() {
        return String.format("Solución: %s por %s (%d likes, %d comentarios)", 
                           titulo, autor.getNombre(), likes, comentarios.size());
    }
}
