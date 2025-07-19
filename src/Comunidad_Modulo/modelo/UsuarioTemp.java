package modelo;

import enums.NivelJava;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioTemp {
    private String idUsuario;
    private String nombre;
    private NivelJava nivelJava;
    private Integer reputacion;
    private List<UsuarioTemp> amigos;
    
    public UsuarioTemp(String nombre, NivelJava nivelJava) {
        this.idUsuario = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.nivelJava = nivelJava;
        this.reputacion = 0;
        this.amigos = new ArrayList<>();
    }
    
    // Getters y setters
    public String getIdUsuario() {
        return idUsuario;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public NivelJava getNivelJava() {
        return nivelJava;
    }
    
    public void setNivelJava(NivelJava nivelJava) {
        this.nivelJava = nivelJava;
    }
    
    public Integer getReputacion() {
        return reputacion;
    }
    
    public void setReputacion(Integer reputacion) {
        this.reputacion = reputacion;
    }
    
    public List<UsuarioTemp> getAmigos() {
        return new ArrayList<>(amigos);
    }
    
    public void agregarAmigo(UsuarioTemp amigo) {
        if (!amigos.contains(amigo)) {
            amigos.add(amigo);
        }
    }
    
    public void eliminarAmigo(UsuarioTemp amigo) {
        amigos.remove(amigo);
    }
    
    public void aumentarReputacion(int puntos) {
        this.reputacion += puntos;
    }
    
    public void disminuirReputacion(int puntos) {
        this.reputacion = Math.max(0, this.reputacion - puntos);
    }
    
    /**
     * Verifica si el usuario puede interactuar (enviar mensajes, crear hilos, etc.)
     * Este método debe ser usado junto con un moderador para verificar sanciones.
     */
    public boolean puedeInteractuar(Moderador moderador) {
        return !moderador.usuarioEstaSancionado(this);
    }
    
    @Override
    public String toString() {
        return String.format("Usuario: %s (Nivel: %s, Reputación: %d)", 
                           nombre, nivelJava, reputacion);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UsuarioTemp usuario = (UsuarioTemp) obj;
        return idUsuario.equals(usuario.idUsuario);
    }
    
    @Override
    public int hashCode() {
        return idUsuario.hashCode();
    }
}
