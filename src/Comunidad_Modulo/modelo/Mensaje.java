package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Mensaje {
    private String idMensaje;
    private String contenido;
    private UsuarioTemp emisor;
    private LocalDateTime fechaEnvio;
    private boolean leido;
    
    public Mensaje(String contenido, UsuarioTemp emisor) {
        this.idMensaje = UUID.randomUUID().toString();
        this.contenido = contenido;
        this.emisor = emisor;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }
    
    // Getters y setters
    public String getIdMensaje() {
        return idMensaje;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public UsuarioTemp getEmisor() {
        return emisor;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void marcarComoLeido() {
        this.leido = true;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", 
                           fechaEnvio.toString(), 
                           emisor.getNombre(), 
                           contenido);
    }
}
