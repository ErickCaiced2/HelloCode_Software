package integracion;

import modelo.UsuarioTemp;
import enums.NivelJava;

/**
 * Implementación simulada del módulo de usuarios para desarrollo y testing.
 * En un sistema real, esta implementación estaría en el módulo de usuarios.
 */
public class ModuloUsuariosSimulado implements IModuloUsuarios {
    
    private UsuarioTemp usuarioActual;
    
    public ModuloUsuariosSimulado() {
        // Simulamos que hay un usuario logueado por defecto
        this.usuarioActual = new UsuarioTemp("Usuario_Demo", NivelJava.INTERMEDIO);
    }
    
    @Override
    public UsuarioTemp obtenerUsuarioPorId(String idUsuario) {
        // En un sistema real, consultaría la base de datos del módulo de usuarios
        if ("user123".equals(idUsuario)) {
            return new UsuarioTemp("Juan Pérez", NivelJava.AVANZADO);
        } else if ("user456".equals(idUsuario)) {
            return new UsuarioTemp("María García", NivelJava.PRINCIPIANTE);
        }
        return null;
    }
    
    @Override
    public UsuarioTemp obtenerUsuarioPorEmail(String email) {
        // En un sistema real, consultaría la base de datos por email
        if ("juan@example.com".equals(email)) {
            return new UsuarioTemp("Juan Pérez", NivelJava.AVANZADO);
        } else if ("maria@example.com".equals(email)) {
            return new UsuarioTemp("María García", NivelJava.PRINCIPIANTE);
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
    public UsuarioTemp obtenerUsuarioActual() {
        return this.usuarioActual;
    }
    
    /**
     * Método para simular el cambio de usuario (solo para testing).
     * En un sistema real, esto lo haría el módulo de autenticación.
     */
    public void simularCambioUsuario(String nombre, NivelJava nivel) {
        this.usuarioActual = new UsuarioTemp(nombre, nivel);
        System.out.println("🔄 Simulación: Usuario cambiado a " + nombre + " (Nivel: " + nivel + ")");
    }
}
