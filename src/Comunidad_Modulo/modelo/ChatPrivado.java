package modelo;

import servicios.ModeracionService.ResultadoModeracion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatPrivado {
    private String idChat;
    private List<UsuarioTemp> participantes;
    private List<Mensaje> mensajes;
    private LocalDateTime fechaCreacion;
    
    public ChatPrivado(List<UsuarioTemp> participantes) {
        this.idChat = UUID.randomUUID().toString();
        this.participantes = new ArrayList<>(participantes);
        this.mensajes = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters
    public String getIdChat() {
        return idChat;
    }
    
    public List<UsuarioTemp> getParticipantes() {
        return new ArrayList<>(participantes);
    }
    
    public List<Mensaje> getMensajes() {
        return new ArrayList<>(mensajes);
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    // Métodos de negocio
    public boolean enviarMensaje(String contenido, UsuarioTemp emisor, Moderador moderador) {
        if (!participantes.contains(emisor)) {
            throw new IllegalArgumentException("El usuario no es participante del chat");
        }
        
        // Verificar si el usuario está sancionado
        if (moderador.usuarioEstaSancionado(emisor)) {
            SancionUsuario sancion = moderador.getSancionActiva(emisor);
            System.out.println("🚫 MENSAJE BLOQUEADO - Usuario " + emisor.getNombre() + 
                             " está sancionado. Tiempo restante: " + 
                             sancion.getMinutosRestantes() + " minutos");
            System.out.println("   Razón: " + sancion.getRazon());
            return false;
        }
        
        // Moderar el contenido del mensaje
        ResultadoModeracion resultado = moderador.moderarMensaje(contenido, emisor);
        
        if (!resultado.isAprobado()) {
            System.out.println("🚫 MENSAJE BLOQUEADO EN CHAT PRIVADO");
            System.out.println("   Usuario: " + emisor.getNombre());
            System.out.println("   Contenido: \"" + contenido + "\"");
            System.out.println("   Razón: " + resultado.getMensaje());
            return false;
        }
        
        // Si el mensaje es aprobado, agregarlo al chat
        Mensaje mensaje = new Mensaje(contenido, emisor);
        mensajes.add(mensaje);
        
        System.out.println("✅ Mensaje enviado en chat privado por " + emisor.getNombre());
        return true;
    }
    
    // Método legacy para compatibilidad (sin moderación)
    @Deprecated
    public void enviarMensaje(String contenido, UsuarioTemp emisor) {
        if (participantes.contains(emisor)) {
            Mensaje mensaje = new Mensaje(contenido, emisor);
            mensajes.add(mensaje);
        } else {
            throw new IllegalArgumentException("El usuario no es participante del chat");
        }
    }
    
    public List<Mensaje> obtenerHistorial() {
        // Marcar todos los mensajes como leídos
        mensajes.forEach(Mensaje::marcarComoLeido);
        return new ArrayList<>(mensajes);
    }
    
    public void agregarParticipante(UsuarioTemp usuario) {
        if (!participantes.contains(usuario)) {
            participantes.add(usuario);
        }
    }
    
    public void eliminarParticipante(UsuarioTemp usuario) {
        participantes.remove(usuario);
    }
    
    public int getMensajesNoLeidos() {
        return (int) mensajes.stream()
                           .filter(m -> !m.isLeido())
                           .count();
    }
    
    @Override
    public String toString() {
        return String.format("Chat: %s participantes, %d mensajes", 
                           participantes.size(), mensajes.size());
    }
}
