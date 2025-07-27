package Comunidad_Modulo.integracion;

import Modulo_Usuario.Clases.Usuario;
import Modulo_Usuario.Clases.UsuarioComunidad;
import Modulo_Usuario.Clases.NivelJava;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación real del módulo de usuarios que se conecta con el sistema de usuarios existente.
 * Lee usuarios desde el archivo usuarios.txt y proporciona funcionalidad de integración.
 */
public class ModuloUsuariosReal implements IModuloUsuarios {
    
    private static final String ARCHIVO_USUARIOS = "src/main/java/Modulo_Usuario/Usuarios/usuarios.txt";
    private List<Usuario> usuariosDelSistema;
    private UsuarioComunidad usuarioActual;
    
    public ModuloUsuariosReal() {
        this.usuariosDelSistema = new ArrayList<>();
        cargarUsuariosDelSistema();
    }
    
    /**
     * Carga usuarios desde el archivo del sistema principal
     */
    private void cargarUsuariosDelSistema() {
        try {
            File file = new File(ARCHIVO_USUARIOS);
            if (!file.exists()) {
                System.err.println("Advertencia: No se encontró el archivo usuarios.txt");
                return;
            }
            
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    if (!linea.isEmpty()) {
                        Usuario usuario = Usuario.fromString(linea);
                        if (usuario != null) {
                            usuariosDelSistema.add(usuario);
                        }
                    }
                }
            }
            System.out.println("ModuloUsuariosReal: Cargados " + usuariosDelSistema.size() + " usuarios del sistema");
            
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios del sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Convierte un Usuario del sistema principal a UsuarioComunidad
     */
    private UsuarioComunidad convertirAUsuarioComunidad(Usuario usuario) {
        if (usuario == null) return null;
        
        UsuarioComunidad usuarioComunidad = new UsuarioComunidad(
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getNombre() != null ? usuario.getNombre() : usuario.getUsername(),
            usuario.getEmail() != null ? usuario.getEmail() : ""
        );
        
        // Configurar propiedades adicionales
        usuarioComunidad.setIdUsuario(usuario.getUsername());
        usuarioComunidad.setNivelJava(NivelJava.PRINCIPIANTE); // Por defecto, se puede mejorar con lógica de progreso
        usuarioComunidad.setReputacion(100); // Reputación inicial para nuevos usuarios
        
        return usuarioComunidad;
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioPorId(String idUsuario) {
        for (Usuario usuario : usuariosDelSistema) {
            if (usuario.getUsername().equals(idUsuario)) {
                return convertirAUsuarioComunidad(usuario);
            }
        }
        return null;
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioPorEmail(String email) {
        for (Usuario usuario : usuariosDelSistema) {
            if (email.equals(usuario.getEmail())) {
                return convertirAUsuarioComunidad(usuario);
            }
        }
        return null;
    }
    
    @Override
    public boolean usuarioEstaAutenticado(String idUsuario) {
        // En este sistema, verificamos si el usuario está en la lista
        return usuariosDelSistema.stream()
                .anyMatch(u -> u.getUsername().equals(idUsuario));
    }
    
    @Override
    public NivelJava obtenerNivelProgreso(String idUsuario) {
        // Por ahora retornamos un nivel basado en el nombre de usuario
        // En un sistema real, esto consultaría el progreso del usuario
        if (idUsuario.toLowerCase().contains("admin")) {
            return NivelJava.AVANZADO;
        } else if (idUsuario.toLowerCase().contains("user")) {
            return NivelJava.INTERMEDIO;
        } else {
            return NivelJava.PRINCIPIANTE;
        }
    }
    
    @Override
    public void notificarParticipacionComunidad(String idUsuario, String tipoParticipacion, int puntosObtenidos) {
        // En un sistema real, esto actualizaría las estadísticas del usuario
        System.out.println(String.format("Usuario %s participó en comunidad: %s (+%d puntos)", 
                                        idUsuario, tipoParticipacion, puntosObtenidos));
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioActual() {
        // Si hay un usuario actual configurado, lo retornamos
        if (usuarioActual != null) {
            return usuarioActual;
        }
        
        // Si no hay usuario actual, retornamos el primer usuario disponible como ejemplo
        // En un sistema real, esto obtendría el usuario de la sesión activa
        if (!usuariosDelSistema.isEmpty()) {
            Usuario primerUsuario = usuariosDelSistema.get(0);
            usuarioActual = convertirAUsuarioComunidad(primerUsuario);
            return usuarioActual;
        }
        
        return null;
    }
    
    /**
     * Método para establecer el usuario actual desde el módulo de usuarios
     */
    public void setUsuarioActual(String username) {
        UsuarioComunidad usuario = obtenerUsuarioPorId(username);
        if (usuario != null) {
            this.usuarioActual = usuario;
        }
    }
    
    /**
     * Obtiene todos los usuarios del sistema convertidos a UsuarioComunidad
     */
    public List<UsuarioComunidad> obtenerTodosLosUsuarios() {
        List<UsuarioComunidad> usuariosComunidad = new ArrayList<>();
        for (Usuario usuario : usuariosDelSistema) {
            usuariosComunidad.add(convertirAUsuarioComunidad(usuario));
        }
        return usuariosComunidad;
    }
    
    /**
     * Recargar usuarios del archivo (útil si se modifican)
     */
    public void recargarUsuarios() {
        usuariosDelSistema.clear();
        cargarUsuariosDelSistema();
    }
}
