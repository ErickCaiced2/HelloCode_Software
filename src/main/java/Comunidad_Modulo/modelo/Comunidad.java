package Comunidad_Modulo.modelo;

import Modulo_Usuario.Clases.UsuarioComunidad;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comunidad {
    private String idComunidad;
    private String nombre;
    private String descripcion;
    private ForoGeneral foroGeneral;
    private List<ChatPrivado> chatsPrivados;
    private List<UsuarioComunidad> usuariosConectados;
    private LocalDateTime fechaCreacion;
    private Moderador moderador;
    
    public Comunidad(String nombre, String descripcion) {
        this.idComunidad = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foroGeneral = new ForoGeneral();
        this.chatsPrivados = new ArrayList<>();
        this.usuariosConectados = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        
        // Inicializar moderador automático para la comunidad
        this.moderador = new Moderador("ModeradorBot_" + nombre);
        this.moderador.asignarComunidad(this);
    }
    
    // Getters y setters
    public String getIdComunidad() {
        return idComunidad;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public ForoGeneral getForoGeneral() {
        return foroGeneral;
    }
    
    public List<ChatPrivado> getChatsPrivados() {
        return new ArrayList<>(chatsPrivados);
    }
    
    public List<UsuarioComunidad> getUsuariosConectados() {
        return new ArrayList<>(usuariosConectados);
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public Moderador getModerador() {
        return moderador;
    }
    
    public void setModerador(Moderador moderador) {
        this.moderador = moderador;
        moderador.asignarComunidad(this);
    }
    
    // Métodos de negocio
    public ForoGeneral accederForo() {
        return foroGeneral;
    }
    
    public ChatPrivado iniciarChatPrivado(List<UsuarioComunidad> participantes) {
        // Verificar que todos los participantes estén conectados
        for (UsuarioComunidad participante : participantes) {
            if (!usuariosConectados.contains(participante)) {
                throw new IllegalArgumentException("Todos los participantes deben estar conectados");
            }
        }
        
        ChatPrivado chat = new ChatPrivado(participantes);
        chatsPrivados.add(chat);
        return chat;
    }
    
    public List<UsuarioComunidad> buscarUsuarios(String nombre) {
        return usuariosConectados.stream()
                                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public void conectarUsuario(UsuarioComunidad usuario) {
        if (!usuariosConectados.contains(usuario)) {
            usuariosConectados.add(usuario);
        }
    }
    
    public void desconectarUsuario(UsuarioComunidad usuario) {
        usuariosConectados.remove(usuario);
    }
    
    public String obtenerEstadisticas() {
        int totalUsuarios = usuariosConectados.size();
        int totalChats = chatsPrivados.size();
        int totalGruposDiscusion = foroGeneral.getGruposDiscusion().size();
        int totalGruposCompartir = foroGeneral.getGruposCompartir().size();
        
        return String.format(
            "Estadísticas de la Comunidad '%s':\n" +
            "- Usuarios conectados: %d\n" +
            "- Chats privados: %d\n" +
            "- Grupos de discusión: %d\n" +
            "- Grupos de compartir: %d\n" +
            "- Fecha de creación: %s",
            nombre, totalUsuarios, totalChats, totalGruposDiscusion, 
            totalGruposCompartir, fechaCreacion.toString()
        );
    }
    
    @Override
    public String toString() {
        return String.format("Comunidad: %s (%d usuarios conectados)", 
                           nombre, usuariosConectados.size());
    }
}
