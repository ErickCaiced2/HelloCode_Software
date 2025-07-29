package MetodosGlobales;

import Modulo_Usuario.Clases.Usuario;
import Modulo_Usuario.Clases.UsuarioComunidad;
import Modulo_Usuario.Clases.NivelJava;

import java.util.List;

/**
 * Gestor de sesión global para mantener el usuario autenticado
 * a través de todos los módulos del sistema.
 */
public class SesionManager {
    private static SesionManager instancia;
    private Usuario usuarioAutenticado;
    private UsuarioComunidad usuarioComunidad;
    private List<Usuario> usuarios;
    
    private SesionManager() {}
    
    public static SesionManager getInstancia() {
        if (instancia == null) {
            instancia = new SesionManager();
        }
        return instancia;
    }

    public void guardarUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Inicia sesión con un usuario
     */
    public void iniciarSesion(Usuario usuario) {
        this.usuarioAutenticado = usuario;
        // Convertir Usuario a UsuarioComunidad para uso en el módulo de comunidad
        this.usuarioComunidad = convertirAUsuarioComunidad(usuario);
        System.out.println("Sesión iniciada para: " + usuario.getUsername());
    }
    
    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioAutenticado = null;
        this.usuarioComunidad = null;
        System.out.println("Sesión cerrada");
    }
    public List<Usuario> getUsuarios() {return usuarios;}
    /**
     * Verifica si hay un usuario autenticado
     */
    public boolean hayUsuarioAutenticado() {
        return usuarioAutenticado != null;
    }
    
    /**
     * Obtiene el usuario autenticado (formato original)
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    /**
     * Obtiene el usuario autenticado en formato de comunidad
     */
    public UsuarioComunidad getUsuarioComunidad() {
        return usuarioComunidad;
    }
    
    /**
     * Obtiene el nombre del usuario actual
     */
    public String getNombreUsuarioActual() {
        if (usuarioAutenticado != null) {
            return usuarioAutenticado.getNombre() != null && !usuarioAutenticado.getNombre().equals("null") 
                   ? usuarioAutenticado.getNombre() 
                   : usuarioAutenticado.getUsername();
        }
        return "Usuario no autenticado";
    }
    
    /**
     * Obtiene el username del usuario actual
     */
    public String getUsernameActual() {
        return usuarioAutenticado != null ? usuarioAutenticado.getUsername() : null;
    }
    
    /**
     * Convierte un Usuario a UsuarioComunidad
     */
    private UsuarioComunidad convertirAUsuarioComunidad(Usuario usuario) {
        // Determinar nombre para mostrar
        String nombreDisplay = usuario.getNombre() != null && !usuario.getNombre().equals("null") 
                              ? usuario.getNombre() 
                              : usuario.getUsername();
        
        // Determinar email
        String emailDisplay = usuario.getEmail() != null && !usuario.getEmail().equals("null") 
                             ? usuario.getEmail() 
                             : "";
        
        // Crear UsuarioComunidad con datos del usuario original
        UsuarioComunidad usuarioComunidad = new UsuarioComunidad(
            usuario.getUsername(),
            usuario.getPassword(),
            nombreDisplay,
            emailDisplay,
            usuario.getUsername(), // usar username como ID
            NivelJava.PRINCIPIANTE, // nivel por defecto
            0 // reputación inicial
        );
        
        return usuarioComunidad;
    }
    
    /**
     * Actualiza el nivel Java del usuario en la sesión
     */
    public void actualizarNivelJava(NivelJava nuevoNivel) {
        if (usuarioComunidad != null) {
            usuarioComunidad.setNivelJava(nuevoNivel);
        }
    }
    
    /**
     * Actualiza la reputación del usuario en la sesión
     */
    public void actualizarReputacion(int nuevaReputacion) {
        if (usuarioComunidad != null) {
            usuarioComunidad.setReputacion(nuevaReputacion);
        }
    }
}
