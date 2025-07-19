package modelo;

import java.time.LocalDateTime;

/**
 * Representa una sanción aplicada a un usuario por comportamiento inapropiado.
 */
public class SancionUsuario {
    private String idSancion;
    private UsuarioTemp usuario;
    private String razon;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activa;
    private String moderadorResponsable;
    
    public SancionUsuario(UsuarioTemp usuario, String razon, int duracionMinutos, String moderadorResponsable) {
        this.idSancion = java.util.UUID.randomUUID().toString();
        this.usuario = usuario;
        this.razon = razon;
        this.fechaInicio = LocalDateTime.now();
        this.fechaFin = fechaInicio.plusMinutes(duracionMinutos);
        this.activa = true;
        this.moderadorResponsable = moderadorResponsable;
    }
    
    /**
     * Verifica si la sanción sigue activa
     */
    public boolean estaActiva() {
        if (!activa) return false;
        
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(fechaFin)) {
            activa = false;
            return false;
        }
        return true;
    }
    
    /**
     * Levanta la sanción manualmente
     */
    public void levantarSancion() {
        this.activa = false;
        this.fechaFin = LocalDateTime.now();
    }
    
    /**
     * Obtiene los minutos restantes de la sanción
     */
    public long getMinutosRestantes() {
        if (!estaActiva()) return 0;
        
        LocalDateTime ahora = LocalDateTime.now();
        return java.time.Duration.between(ahora, fechaFin).toMinutes();
    }
    
    // Getters
    public String getIdSancion() { return idSancion; }
    public UsuarioTemp getUsuario() { return usuario; }
    public String getRazon() { return razon; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public boolean isActiva() { return activa; }
    public String getModeradorResponsable() { return moderadorResponsable; }
    
    @Override
    public String toString() {
        return String.format("Sanción: %s - Usuario: %s - Razón: %s - Restante: %d min", 
                           idSancion.substring(0, 8), usuario.getNombre(), razon, getMinutosRestantes());
    }
}
