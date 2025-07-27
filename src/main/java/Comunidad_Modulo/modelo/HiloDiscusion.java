package Comunidad_Modulo.modelo;

import Modulo_Usuario.Clases.UsuarioComunidad;
import Comunidad_Modulo.enums.EstadoHilo;
import Comunidad_Modulo.servicios.ModeracionService.ResultadoModeracion;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HiloDiscusion {
    private String idHilo;
    private String titulo;
    private String problema;
    private UsuarioComunidad autor;
    private EstadoHilo estado;
    private List<Respuesta> respuestas;
    private Integer votos;
    
    public HiloDiscusion(String titulo, String problema, UsuarioComunidad autor) {
        this.idHilo = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.problema = problema;
        this.autor = autor;
        this.estado = EstadoHilo.ABIERTO;
        this.respuestas = new ArrayList<>();
        this.votos = 0;
    }
    
    // Getters y setters
    public String getIdHilo() {
        return idHilo;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getProblema() {
        return problema;
    }
    
    public void setProblema(String problema) {
        this.problema = problema;
    }
    
    public UsuarioComunidad getAutor() {
        return autor;
    }
    
    public EstadoHilo getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoHilo estado) {
        this.estado = estado;
    }
    
    public List<Respuesta> getRespuestas() {
        return new ArrayList<>(respuestas);
    }
    
    public Integer getVotos() {
        return votos;
    }
    
    // Métodos de negocio
    public boolean responder(String contenido, UsuarioComunidad autor, Moderador moderador) {
        if (estado == EstadoHilo.CERRADO) {
            System.out.println("🚫 No se puede responder a un hilo cerrado");
            return false;
        }
        
        // Verificar si el usuario está sancionado
        if (moderador.usuarioEstaSancionado(autor)) {
            SancionUsuario sancion = moderador.getSancionActiva(autor);
            System.out.println("🚫 RESPUESTA BLOQUEADA - Usuario " + autor.getNombre() + 
                             " está sancionado. Tiempo restante: " + 
                             sancion.getMinutosRestantes() + " minutos");
            System.out.println("   Razón: " + sancion.getRazon());
            return false;
        }
        
        // Moderar el contenido de la respuesta
        ResultadoModeracion resultado = moderador.moderarMensaje(contenido, autor);
        
        if (!resultado.isAprobado()) {
            System.out.println("🚫 RESPUESTA BLOQUEADA EN HILO DE DISCUSIÓN");
            System.out.println("   Usuario: " + autor.getNombre());
            System.out.println("   Hilo: " + this.titulo);
            System.out.println("   Contenido: \"" + contenido + "\"");
            System.out.println("   Razón: " + resultado.getMensaje());
            return false;
        }
        
        // Si la respuesta es aprobada, agregarla al hilo
        Respuesta respuesta = new Respuesta(contenido, autor);
        respuestas.add(respuesta);
        
        // Otorgar puntos de reputación por participar
        autor.incrementarReputacion(2);
        
        System.out.println("✅ Respuesta agregada al hilo \"" + this.titulo + "\" por " + autor.getNombre());
        return true;
    }
    
    // Método legacy para compatibilidad (sin moderación)
    @Deprecated
    public void responder(String contenido, UsuarioComunidad autor) {
        if (estado == EstadoHilo.CERRADO) {
            throw new IllegalStateException("No se puede responder a un hilo cerrado");
        }
        
        Respuesta respuesta = new Respuesta(contenido, autor);
        respuestas.add(respuesta);
        
        // Otorgar puntos de reputación por participar
        autor.incrementarReputacion(2);
    }
    
    public void votar(boolean esPositivo) {
        if (esPositivo) {
            this.votos++;
            autor.incrementarReputacion(1);
        } else {
            this.votos--;
            autor.decrementarReputacion(1);
        }
    }
    
    public void marcarResuelto() {
        this.estado = EstadoHilo.RESUELTO;
        // Otorgar puntos de reputación por resolver problema
        autor.incrementarReputacion(5);
    }
    
    public void cerrar() {
        this.estado = EstadoHilo.CERRADO;
    }
    
    public void reabrir() {
        if (estado == EstadoHilo.CERRADO) {
            this.estado = EstadoHilo.ABIERTO;
        }
    }
    
    public boolean tieneSolucion() {
        return respuestas.stream().anyMatch(Respuesta::getEsSolucion);
    }
    
    public List<Respuesta> getSoluciones() {
        return respuestas.stream()
                        .filter(Respuesta::getEsSolucion)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public String toString() {
        return String.format("Hilo: %s por %s [%s] (%d votos, %d respuestas)", 
                           titulo, autor.getNombre(), estado, votos, respuestas.size());
    }
}
