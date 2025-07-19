package integracion;

import controladores.*;
import servicios.ComunidadService;
import utilidades.InputHelper;
import utilidades.MenuHelper;

/**
 * Punto de entrada principal para el módulo de comunidad.
 * Esta clase sería llamada desde el módulo principal del sistema HelloCode.
 * Gestiona la inicialización y coordinación de todos los controladores de comunidad.
 */
public class ComunidadModuloEntry {
    
    private final IModuloUsuarios moduloUsuarios;
    private final ContextoSistema contexto;
    private final ComunidadService comunidadService;
    
    /**
     * Constructor que inicializa el módulo de comunidad con la integración de usuarios.
     * @param moduloUsuarios Implementación del módulo de usuarios del sistema principal
     */
    public ComunidadModuloEntry(IModuloUsuarios moduloUsuarios) {
        this.moduloUsuarios = moduloUsuarios;
        this.contexto = ContextoSistema.getInstance();
        this.comunidadService = new ComunidadService();
        
        // Establecer la integración con el módulo de usuarios
        this.contexto.setModuloUsuarios(moduloUsuarios);
        
        // Verificar si hay un usuario activo y cargarlo
        cargarUsuarioActivo();
        
        // El sistema está listo para funcionar sin datos de ejemplo
    }
    
    /**
     * Método principal para iniciar el módulo de comunidad.
     * Este método sería llamado desde el menú principal del sistema Duolingo.
     */
    public void iniciarModuloComunidad() {
        System.out.println("🏛️ Bienvenido al Módulo de Comunidad");
        
        // Verificar que hay un usuario autenticado
        if (moduloUsuarios.obtenerUsuarioActual() == null) {
            System.out.println("❌ Error: No hay usuario autenticado en el sistema.");
            return;
        }
        
        // Mostrar información del usuario actual
        mostrarInfoUsuarioActual();
        
        // Mostrar menú principal de comunidad
        boolean salir = false;
        while (!salir) {
            MenuHelper.mostrarMenuPrincipal();
            int opcion = InputHelper.leerEntero("Seleccione una opción");
            
            switch (opcion) {
                case 1:
                    // Gestión de Usuarios
                    new UsuarioControlador(comunidadService, contexto).ejecutar();
                    break;
                case 2:
                    // Gestión de Comunidad
                    new ComunidadControlador(comunidadService, contexto).ejecutar();
                    break;
                case 3:
                    // Foros de Discusión
                    new ForoControlador(comunidadService, contexto).ejecutar();
                    break;
                case 4:
                    // Chat Privado
                    new ChatControlador(comunidadService, contexto).ejecutar();
                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        
        cerrarModulo();
    }
    
    /**
     * Método para acceder directamente a un foro específico.
     * Útil cuando el usuario viene desde una notificación o enlace directo.
     */
    public void accederForoDirecto(String idForo) {
        System.out.println("🎯 Acceso directo al foro: " + idForo);
        // Crear y ejecutar controlador de foro
        new ForoControlador(comunidadService, contexto).ejecutar();
    }
    
    /**
     * Método para acceder directamente a un chat específico.
     * Útil para notificaciones de mensajes privados.
     */
    public void accederChatDirecto(String idChat) {
        System.out.println("💬 Acceso directo al chat: " + idChat);
        // Crear y ejecutar controlador de chat
        new ChatControlador(comunidadService, contexto).ejecutar();
    }
    
    /**
     * Método para obtener estadísticas rápidas del usuario en la comunidad.
     * Útil para mostrar en el dashboard principal del sistema.
     */
    public String obtenerEstadisticasUsuario() {
        var usuario = moduloUsuarios.obtenerUsuarioActual();
        if (usuario == null) return "Usuario no disponible";
        
        return String.format(
            "Comunidad - Nivel: %s, Participaciones: %d, Puntos: %d",
            usuario.getNivelJava(),
            contexto.getParticipacionesUsuario(usuario.getNombre()),
            contexto.getPuntosUsuario(usuario.getNombre())
        );
    }
    
    /**
     * Método para cerrar el módulo de comunidad y limpiar recursos.
     */
    public void cerrarModulo() {
        System.out.println("🔒 Cerrando módulo de comunidad...");
        // Guardar estado si es necesario
        // Limpiar recursos
        System.out.println("✅ Módulo de comunidad cerrado correctamente.");
    }
    
    private void cargarUsuarioActivo() {
        var usuarioActual = moduloUsuarios.obtenerUsuarioActual();
        if (usuarioActual != null) {
            contexto.setUsuarioActivo(usuarioActual);
        }
    }
    
    private void mostrarInfoUsuarioActual() {
        var usuario = moduloUsuarios.obtenerUsuarioActual();
        if (usuario != null) {
            System.out.println("👤 Usuario activo: " + usuario.getNombre());
            System.out.println("📊 Nivel de Java: " + usuario.getNivelJava());
            System.out.println("🎯 ¡Listo para participar en la comunidad!");
            System.out.println("─".repeat(50));
        }
    }
}
