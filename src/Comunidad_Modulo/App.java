import integracion.*;

/**
 * Punto de entrada principal del módulo de comunidad.
 * Esta clase es para testing y demostración local.
 * 
 * En un sistema de producción, el módulo sería llamado
 * desde ComunidadModuloEntry por el sistema principal.
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("🏛️ MÓDULO DE COMUNIDAD - MODO TESTING LOCAL");
        System.out.println("═".repeat(50));
        
        // Para testing local, usamos el módulo simulado
        IModuloUsuarios moduloUsuarios = new ModuloUsuariosSimulado();
        
        // Crear punto de entrada del módulo
        ComunidadModuloEntry moduloComunidad = new ComunidadModuloEntry(moduloUsuarios);
        
        // Iniciar el módulo de comunidad
        moduloComunidad.iniciarModuloComunidad();
        
        System.out.println("\n👋 ¡Gracias por usar el módulo de comunidad!");
    }
}
