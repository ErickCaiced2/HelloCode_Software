package Modulo_Usuario.Clases;

import java.util.ArrayList;
import java.util.List;

public class UsuarioComunidad extends Usuario {
    private String idUsuario;
    private NivelJava nivelJava;
    private Integer reputacion;
    private List<UsuarioTemp> amigos;

    // Constructor por defecto
    public UsuarioComunidad() {
        super();
        this.amigos = new ArrayList<>();
        this.reputacion = 0;
        this.nivelJava = NivelJava.PRINCIPIANTE;
    }

    // Constructor con parámetros básicos
    public UsuarioComunidad(String username, String password, String nombre, String email) {
        super(username, password, nombre, email);
        this.amigos = new ArrayList<>();
        this.reputacion = 0;
        this.nivelJava = NivelJava.PRINCIPIANTE;
        this.idUsuario = username; // Usamos username como idUsuario por defecto
    }

    // Constructor completo
    public UsuarioComunidad(String username, String password, String nombre, String email,
                            String idUsuario, NivelJava nivelJava, Integer reputacion) {
        super(username, password, nombre, email);
        this.idUsuario = idUsuario;
        this.nivelJava = nivelJava;
        this.reputacion = reputacion;
        this.amigos = new ArrayList<>();
    }

    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
        return amigos;
    }

    public void setAmigos(List<UsuarioTemp> amigos) {
        this.amigos = amigos;
    }

    // Métodos para gestionar amigos
    public void agregarAmigo(UsuarioTemp amigo) {
        if (amigo != null && !amigos.contains(amigo)) {
            amigos.add(amigo);
        }
    }

    public void eliminarAmigo(UsuarioTemp amigo) {
        amigos.remove(amigo);
    }

    public void incrementarReputacion(int puntos) {
        this.reputacion += puntos;
    }

    public void decrementarReputacion(int puntos) {
        this.reputacion = Math.max(0, this.reputacion - puntos);
    }

    @Override
    public String toString() {
        return "UsuarioComunidad{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", nivelJava=" + nivelJava +
                ", reputacion=" + reputacion +
                ", amigos=" + amigos.size() +
                '}';
    }
} 