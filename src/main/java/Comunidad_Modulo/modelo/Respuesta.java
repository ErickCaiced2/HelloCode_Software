package Comunidad_Modulo.modelo;

import Modulo_Usuario.Clases.UsuarioComunidad;
import java.time.LocalDateTime;
import java.util.UUID;

public class Respuesta {
    private String idRespuesta;
    private String contenido;
    private UsuarioComunidad autor;
    private LocalDateTime fechaPublicacion;
    private Integer votos;
    private Boolean esSolucion;
    
    public Respuesta(String contenido, UsuarioComunidad autor) {
        this.idRespuesta = UUID.randomUUID().toString();
        this.contenido = contenido;
        this.autor = autor;
        this.fechaPublicacion = LocalDateTime.now();
        this.votos = 0;
        this.esSolucion = false;
    }
    
    // Getters y setters
    public String getIdRespuesta() {
        return idRespuesta;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public UsuarioComunidad getAutor() {
        return autor;
    }
    
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
    
    public Integer getVotos() {
        return votos;
    }
    
    public Boolean getEsSolucion() {
        return esSolucion;
    }
    
    // Métodos de negocio
    public void votar(boolean esPositivo) {
        if (esPositivo) {
            this.votos++;
        } else {
            this.votos--;
        }
    }
    
    public void marcarComoSolucion() {
        this.esSolucion = true;
        // Otorgar puntos de reputación al autor
        autor.incrementarReputacion(10);
    }
    
    public void desmarcarComoSolucion() {
        this.esSolucion = false;
        // Quitar puntos de reputación al autor
        autor.decrementarReputacion(10);
    }
    
    @Override
    public String toString() {
        String solucionMark = esSolucion ? " [SOLUCIÓN]" : "";
        return String.format("Respuesta por %s (Votos: %d)%s: %s", 
                           autor.getNombre(), votos, solucionMark, contenido);
    }
}
