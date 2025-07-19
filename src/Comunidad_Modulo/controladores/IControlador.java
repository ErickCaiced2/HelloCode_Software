package controladores;

/**
 * Interfaz base para todos los controladores del sistema.
 * Cada módulo debe implementar esta interfaz para mantener consistencia.
 */
public interface IControlador {
    /**
     * Ejecuta el controlador mostrando el menú y manejando las opciones.
     */
    void ejecutar();
    
    /**
     * Obtiene el nombre del módulo que maneja este controlador.
     * @return Nombre del módulo
     */
    String getNombreModulo();
}
