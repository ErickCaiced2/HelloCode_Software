package Comunidad_Modulo.modelo;

import Modulo_Usuario.Clases.UsuarioComunidad;
import Comunidad_Modulo.servicios.ModeracionService;
import Comunidad_Modulo.servicios.ModeracionService.ResultadoModeracion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Moderador {
    private String idModerador;
    private String nombre;
    private List<Comunidad> comunidadesGestionadas;
    private ModeracionService moderacionService;
    
    public Moderador(String nombre) {
        this.idModerador = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.comunidadesGestionadas = new ArrayList<>();
        this.moderacionService = new ModeracionService();
    }
    
    // Getters y setters
    public String getIdModerador() {
        return idModerador;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Comunidad> getComunidadesGestionadas() {
        return new ArrayList<>(comunidadesGestionadas);
    }
    
    // Métodos de negocio
    public void asignarComunidad(Comunidad comunidad) {
        if (!comunidadesGestionadas.contains(comunidad)) {
            comunidadesGestionadas.add(comunidad);
        }
    }
    
    public void removerComunidad(Comunidad comunidad) {
        comunidadesGestionadas.remove(comunidad);
    }
    
    public void moderarForo(ForoGeneral foro) {
        // Lógica de moderación del foro
        System.out.println("Moderando foro: " + foro.toString());
    }
    
    public void supervisarChats(List<ChatPrivado> chats) {
        // Lógica de supervisión de chats
        System.out.println("Supervisando " + chats.size() + " chats privados");
    }
    
    public void cerrarHilo(HiloDiscusion hilo) {
        hilo.cerrar();
        System.out.println("Hilo cerrado por moderador: " + hilo.getTitulo());
    }
    
    public void eliminarMensaje(ChatPrivado chat, String idMensaje) {
        // Lógica para eliminar mensaje (simulada)
        System.out.println("Mensaje eliminado por moderador en chat: " + chat.getIdChat());
    }
    
    public void suspenderUsuario(UsuarioComunidad usuario) {
        // Lógica para suspender usuario (simulada)
        System.out.println("Usuario suspendido: " + usuario.getNombre());
    }
    
    // === NUEVOS MÉTODOS DE MODERACIÓN AUTOMÁTICA ===
    
    /**
     * Modera un mensaje automáticamente antes de publicarlo
     */
    public ResultadoModeracion moderarMensaje(String contenido, UsuarioComunidad autor) {
        return moderacionService.moderarMensaje(contenido, autor, this.nombre);
    }
    
    /**
     * Aplica una sanción manual a un usuario
     */
    public SancionUsuario aplicarSancionManual(UsuarioComunidad usuario, String razon, int duracionMinutos) {
        return moderacionService.aplicarSancion(usuario, razon, duracionMinutos, this.nombre);
    }
    
    /**
     * Levanta una sanción de un usuario
     */
    public boolean levantarSancion(UsuarioComunidad usuario) {
        return moderacionService.levantarSancion(usuario, this.nombre);
    }
    
    /**
     * Verifica si un usuario está sancionado
     */
    public boolean usuarioEstaSancionado(UsuarioComunidad usuario) {
        return moderacionService.usuarioEstaSancionado(usuario);
    }
    
    /**
     * Obtiene información de la sanción activa de un usuario
     */
    public SancionUsuario getSancionActiva(UsuarioComunidad usuario) {
        return moderacionService.obtenerSancionActiva(usuario);
    }
    
    /**
     * Obtiene todas las sanciones activas
     */
    public List<SancionUsuario> getSancionesActivas() {
        return moderacionService.getSancionesActivas();
    }
    
    /**
     * Obtiene el historial de sanciones de un usuario
     */
    public List<SancionUsuario> getHistorialSanciones(UsuarioComunidad usuario) {
        return moderacionService.getHistorialSanciones(usuario);
    }
    
    /**
     * Obtiene estadísticas de moderación
     */
    public ModeracionService.EstadisticasModeración getEstadisticasModeración() {
        return moderacionService.getEstadisticas();
    }
    
    /**
     * Método auxiliar para mostrar el estado de moderación
     */
    public void mostrarEstadoModeración() {
        System.out.println("\n🛡️ === ESTADO DE MODERACIÓN ===");
        System.out.println("Moderador: " + this.nombre);
        
        ModeracionService.EstadisticasModeración stats = getEstadisticasModeración();
        System.out.println(stats.toString());
        
        List<SancionUsuario> sancionesActivas = getSancionesActivas();
        if (!sancionesActivas.isEmpty()) {
            System.out.println("\n🚫 Sanciones activas:");
            for (SancionUsuario sancion : sancionesActivas) {
                System.out.println("  - " + sancion.toString());
            }
        } else {
            System.out.println("\n✅ No hay sanciones activas");
        }
        System.out.println("=".repeat(35));
    }
    
    // Getter para el servicio de moderación (por si se necesita acceso directo)
    public ModeracionService getModeracionService() {
        return moderacionService;
    }
    
    @Override
    public String toString() {
        return String.format("Moderador: %s (gestiona %d comunidades)", 
                           nombre, comunidadesGestionadas.size());
    }
}
