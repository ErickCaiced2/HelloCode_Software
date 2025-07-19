package integracion;

import modelo.UsuarioTemp;
import enums.NivelJava;

/**
 * Interfaz para la integración con el módulo de usuarios principal.
 * Define los métodos que debe implementar el módulo de usuarios para 
 * permitir que la comunidad acceda a los usuarios del sistema.
 */
public interface IModuloUsuarios {
    
    /**
     * Obtiene un usuario del sistema principal por su ID.
     * @param idUsuario ID único del usuario en el sistema principal
     * @return Usuario adaptado para el módulo de comunidad, null si no existe
     */
    UsuarioTemp obtenerUsuarioPorId(String idUsuario);
    
    /**
     * Obtiene un usuario del sistema principal por su email/nombre de usuario.
     * @param email Email o nombre de usuario en el sistema principal
     * @return Usuario adaptado para el módulo de comunidad, null si no existe
     */
    UsuarioTemp obtenerUsuarioPorEmail(String email);
    
    /**
     * Verifica si un usuario está autenticado en el sistema.
     * @param idUsuario ID del usuario a verificar
     * @return true si está autenticado, false en caso contrario
     */
    boolean usuarioEstaAutenticado(String idUsuario);
    
    /**
     * Obtiene el nivel de programación del usuario desde sus datos de progreso.
     * @param idUsuario ID del usuario
     * @return Nivel de Java basado en su progreso en el módulo de aprendizaje
     */
    NivelJava obtenerNivelProgreso(String idUsuario);
    
    /**
     * Notifica al módulo de usuarios que el usuario participó en la comunidad.
     * Esto puede afectar su progreso o estadísticas.
     * @param idUsuario ID del usuario
     * @param tipoParticipacion Tipo de participación (respuesta, like, etc.)
     * @param puntosObtenidos Puntos obtenidos por la participación
     */
    void notificarParticipacionComunidad(String idUsuario, String tipoParticipacion, int puntosObtenidos);
    
    /**
     * Obtiene la información básica del usuario actual logueado.
     * @return Usuario actual del sistema, null si no hay usuario logueado
     */
    UsuarioTemp obtenerUsuarioActual();
}
