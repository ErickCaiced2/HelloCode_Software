package Comunidad_Modulo.integracion;

import Modulo_Usuario.Clases.UsuarioComunidad;
import Modulo_Usuario.Clases.NivelJava;

/**
 * Implementación simulada del módulo de usuarios para desarrollo y testing.
 * En un sistema real, esta implementación estaría en el módulo de usuarios.
 */
public class ModuloUsuariosSimulado implements IModuloUsuarios {
    
    private UsuarioComunidad usuarioActual;
    
    public ModuloUsuariosSimulado() {
        // Simulamos que hay un usuario logueado por defecto
        this.usuarioActual = new UsuarioComunidad("demo_user", "password123", "Usuario Demo", "demo@example.com");
        this.usuarioActual.setIdUsuario("demo_001");
        this.usuarioActual.setNivelJava(NivelJava.INTERMEDIO);
        this.usuarioActual.setReputacion(150);
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioPorId(String idUsuario) {
        // En un sistema real, consultaría la base de datos del módulo de usuarios
        if ("user123".equals(idUsuario)) {
            UsuarioComunidad user = new UsuarioComunidad("juan_perez", "pass123", "Juan Pérez", "juan@example.com");
            user.setIdUsuario("user123");
            user.setNivelJava(NivelJava.AVANZADO);
            user.setReputacion(500);
            return user;
        } else if ("user456".equals(idUsuario)) {
            UsuarioComunidad user = new UsuarioComunidad("maria_garcia", "pass456", "María García", "maria@example.com");
            user.setIdUsuario("user456");
            user.setNivelJava(NivelJava.PRINCIPIANTE);
            user.setReputacion(50);
            return user;
        }
        return null;
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioPorEmail(String email) {
        // En un sistema real, consultaría la base de datos por email
        if ("juan@example.com".equals(email)) {
            UsuarioComunidad user = new UsuarioComunidad("juan_perez", "pass123", "Juan Pérez", "juan@example.com");
            user.setIdUsuario("user123");
            user.setNivelJava(NivelJava.AVANZADO);
            user.setReputacion(500);
            return user;
        } else if ("maria@example.com".equals(email)) {
            UsuarioComunidad user = new UsuarioComunidad("maria_garcia", "pass456", "María García", "maria@example.com");
            user.setIdUsuario("user456");
            user.setNivelJava(NivelJava.PRINCIPIANTE);
            user.setReputacion(50);
            return user;
        }
        return usuarioActual; // Usuario por defecto
    }
    
    @Override
    public boolean usuarioEstaAutenticado(String idUsuario) {
        // En un sistema real, verificaría la sesión activa
        return idUsuario != null && !idUsuario.trim().isEmpty();
    }
    
    @Override
    public NivelJava obtenerNivelProgreso(String idUsuario) {
        // En un sistema real, consultaría el progreso del módulo de aprendizaje
        if ("user123".equals(idUsuario)) {
            return NivelJava.AVANZADO;
        } else if ("user456".equals(idUsuario)) {
            return NivelJava.PRINCIPIANTE;
        }
        return NivelJava.INTERMEDIO; // Nivel por defecto
    }
    
    @Override
    public void notificarParticipacionComunidad(String idUsuario, String tipoParticipacion, int puntosObtenidos) {
        // En un sistema real, actualizaría las estadísticas del usuario
        System.out.println("📊 Notificación al módulo de usuarios:");
        System.out.println("   Usuario: " + idUsuario);
        System.out.println("   Participación: " + tipoParticipacion);
        System.out.println("   Puntos obtenidos: " + puntosObtenidos);
    }
    
    @Override
    public UsuarioComunidad obtenerUsuarioActual() {
        return this.usuarioActual;
    }
    
    /**
     * Método para simular el cambio de usuario (solo para testing).
     * En un sistema real, esto lo haría el módulo de autenticación.
     */
    public void simularCambioUsuario(String nombre, NivelJava nivel) {
        this.usuarioActual = new UsuarioComunidad("test_user", "test123", nombre, "test@example.com");
        this.usuarioActual.setNivelJava(nivel);
        System.out.println("🔄 Simulación: Usuario cambiado a " + nombre + " (Nivel: " + nivel + ")");
    }
}
