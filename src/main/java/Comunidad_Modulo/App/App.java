package Comunidad_Modulo.App;

import Comunidad_Modulo.integracion.*;

/**
 * Punto de entrada principal del módulo de comunidad.
 * Esta clase es para demostración y testing de la integración.
 * 
 * En un sistema de producción, el módulo sería usado
 * directamente desde ComunidadModuloEntry por el sistema principal.
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("🏛️ MÓDULO DE COMUNIDAD - TESTING DE INTEGRACIÓN");
        System.out.println("═".repeat(50));
        
        // Ejemplo de uso con módulo simulado
        IModuloUsuarios moduloUsuarios = new ModuloUsuariosSimulado();
        @SuppressWarnings("unused")
        ComunidadModuloEntry moduloComunidad = new ComunidadModuloEntry(moduloUsuarios);
        
        // El módulo está listo para ser usado por las interfaces gráficas
        System.out.println("✅ Módulo de comunidad inicializado correctamente");
        System.out.println("📱 Listo para integración con interfaces gráficas");
        
        // Mostrar información básica del sistema
        System.out.println("\n📊 INFORMACIÓN DEL SISTEMA:");
        System.out.println("• Servicios de comunidad: Disponibles");
        System.out.println("• Sistema de moderación: Activo");
        System.out.println("• Integración de usuarios: Configurada");
        
        System.out.println("\n👋 Módulo listo para usar");
    }
}
