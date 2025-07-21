package Comunidad_Modulo.integracion;

import Comunidad_Modulo.controladores.ContextoSistema;
import Comunidad_Modulo.servicios.ComunidadService;

/**
 * Punto de entrada principal para el módulo de comunidad.
 * Esta clase es llamada desde el módulo principal del sistema HelloCode.
 * Gestiona la inicialización y coordinación del sistema de comunidad con interfaz gráfica.
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
    }
    
    /**
     * Obtiene el servicio de comunidad para ser usado por los controladores GUI.
     * @return El servicio de comunidad configurado
     */
    public ComunidadService getComunidadService() {
        return comunidadService;
    }
    
    /**
     * Obtiene el contexto del sistema para ser usado por los controladores GUI.
     * @return El contexto del sistema configurado
     */
    public ContextoSistema getContexto() {
        return contexto;
    }
    
    /**
     * Obtiene la integración con el módulo de usuarios.
     * @return La interfaz de integración con usuarios
     */
    public IModuloUsuarios getModuloUsuarios() {
        return moduloUsuarios;
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
     * Verifica si el módulo está correctamente inicializado.
     * @return true si está listo para usar, false en caso contrario
     */
    public boolean estaInicializado() {
        return moduloUsuarios != null && contexto != null && comunidadService != null;
    }
    
    /**
     * Verifica si hay un usuario autenticado y activo.
     * @return true si hay usuario activo, false en caso contrario
     */
    public boolean hayUsuarioActivo() {
        return moduloUsuarios.obtenerUsuarioActual() != null;
    }
    
    /**
     * Método para cerrar el módulo de comunidad y limpiar recursos.
     */
    public void cerrarModulo() {
        // Limpiar recursos si es necesario
        System.out.println("✅ Módulo de comunidad cerrado correctamente.");
    }
    
    private void cargarUsuarioActivo() {
        var usuarioActual = moduloUsuarios.obtenerUsuarioActual();
        if (usuarioActual != null) {
            contexto.setUsuarioActivo(usuarioActual);
        }
    }
}
